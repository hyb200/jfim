package com.abin.chatserver.user.service.cache;

import cn.hutool.core.bean.BeanUtil;
import com.abin.chatserver.common.constants.RedisKey;
import com.abin.chatserver.common.service.cache.AbstractRedisStringCache;
import com.abin.chatserver.user.dao.UserDao;
import com.abin.chatserver.user.domain.dto.UserInfoDTO;
import com.abin.chatserver.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserInfoCache extends AbstractRedisStringCache<Long, UserInfoDTO> {

    private final UserDao userDao;

    @Override
    protected String getKey(Long uid) {
        return RedisKey.getKey(RedisKey.USER_INFO_STRING, uid);
    }

    @Override
    protected Long getExpireTime() {
        return 5 * 60L;
    }

    @Override
    protected Map<Long, UserInfoDTO> load(List<Long> uids) {
        List<User> needLoadUsers = userDao.listByIds(uids);
        return needLoadUsers.stream()
                .map(user -> BeanUtil.copyProperties(user, UserInfoDTO.class))
                .collect(Collectors.toMap(UserInfoDTO::getUid, Function.identity()));
    }
}
