package com.abin.chatserver.user.service.cache;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.abin.chatserver.common.constants.RedisKey;
import com.abin.chatserver.common.utils.RedisUtils;
import com.abin.chatserver.user.dao.UserDao;
import com.abin.chatserver.user.domain.dto.UserInfoDTO;
import com.abin.chatserver.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserCache {

    private final UserDao userDao;
    private final UserSummaryCache userSummaryCache;

    /**
     * 获取用户信息，旁路缓存
     */
    public User getUserInfo(Long uid) {
        return getUserInfoBatch(Collections.singleton(uid)).get(uid);
    }

    /**
     * 批量获取用户信息，旁路缓存
     */
    private Map<Long, User> getUserInfoBatch(Set<Long> uids) {
        List<String> keys = uids.stream()
                .map(uid -> RedisKey.getKey(RedisKey.USER_INFO_STRING, uid)).toList();

        List<User> mget = RedisUtils.mget(keys, User.class);
        Map<Long, User> map = mget.stream()
                .filter(Objects::nonNull).collect(Collectors.toMap(User::getUid, Function.identity()));
        //  需要更新的 uid
        List<Long> needRefresh = uids.stream().filter(uid -> !map.containsKey(uid)).toList();
        if (CollUtil.isNotEmpty(needRefresh)) {
            List<User> update = userDao.listByIds(needRefresh);
            Map<String, UserInfoDTO> toRedis = update.stream()
                    .map(user -> BeanUtil.copyProperties(user, UserInfoDTO.class))
                    .collect(Collectors.toMap(o -> RedisKey.getKey(RedisKey.USER_INFO_STRING, o.getUid()), Function.identity()));
            //  更新回 redis
            RedisUtils.mset(toRedis, 5 * 60);
            map.putAll(update.stream().collect(Collectors.toMap(User::getUid, Function.identity())));
        }
        return map;
    }

    public List<Long> getModifyTime(List<Long> uids) {
        List<String> keys = uids.stream().map(uid -> RedisKey.getKey(RedisKey.USER_MODIFY_STRING, uid)).toList();
        return RedisUtils.mget(keys, Long.class);
    }

    public void refreshModifyTime(Long uid) {
        String key = RedisKey.getKey(RedisKey.USER_MODIFY_STRING, uid);
        RedisUtils.set(key, new Date().getTime());
    }

    public void userInfoChange(Long uid) {
        delUserInfo(uid);
        userSummaryCache.delete(uid);
        refreshModifyTime(uid);
    }

    private void delUserInfo(Long uid) {
        String key = RedisKey.getKey(RedisKey.USER_INFO_STRING, uid);
        RedisUtils.del(key);
    }
}
