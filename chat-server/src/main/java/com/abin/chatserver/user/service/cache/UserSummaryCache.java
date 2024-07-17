package com.abin.chatserver.user.service.cache;

import com.abin.chatserver.common.constants.RedisKey;
import com.abin.chatserver.common.service.cache.AbstractRedisStringCache;
import com.abin.chatserver.user.domain.dto.SummaryInfoDTO;
import com.abin.chatserver.user.domain.dto.UserInfoDTO;
import com.abin.chatserver.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserSummaryCache extends AbstractRedisStringCache<Long, SummaryInfoDTO> {

    private final UserInfoCache userInfoCache;

    @Override
    protected String getKey(Long key) {
        return RedisKey.getKey(RedisKey.USER_SUMMARY_STRING, key);
    }

    @Override
    protected Long getExpireTime() {
        return 10 * 60L;
    }

    @Override
    protected Map<Long, SummaryInfoDTO> load(List<Long> uids) {
        Map<Long, UserInfoDTO> userMap = userInfoCache.getBatch(uids);
        return uids.stream()
                .map(uid -> {
                    UserInfoDTO user = userMap.get(uid);
                    if (Objects.isNull(user)) {
                        return null;
                    }
                    return SummaryInfoDTO.builder()
                            .uid(user.getUid())
                            .nickname(user.getNickname())
                            .avatar(user.getAvatar())
                            .build();
                }).filter(Objects::nonNull)
                .collect(Collectors.toMap(SummaryInfoDTO::getUid, Function.identity()));
    }
}
