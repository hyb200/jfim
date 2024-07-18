package com.abin.chatserver.chat.service.cache;

import com.abin.chatserver.chat.dao.SessionGroupDao;
import com.abin.chatserver.chat.domain.entity.SessionGroup;
import com.abin.chatserver.common.constants.RedisKey;
import com.abin.chatserver.common.service.cache.AbstractRedisStringCache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SessionGroupCache extends AbstractRedisStringCache<Long, SessionGroup> {

    private final SessionGroupDao sessionGroupDao;

    @Override
    protected String getKey(Long sessionId) {
        return RedisKey.getKey(RedisKey.GROUP_INFO_STRING, sessionId);
    }

    @Override
    protected Long getExpireTime() {
        return 5 * 60L;
    }

    @Override
    protected Map<Long, SessionGroup> load(List<Long> sessionIds) {
        List<SessionGroup> sessionGroups = sessionGroupDao.listByIds(sessionIds);
        return sessionGroups.stream().collect(Collectors.toMap(SessionGroup::getSessionId, Function.identity()));
    }
}
