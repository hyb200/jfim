package com.abin.chatserver.chat.service.cache;

import com.abin.chatserver.chat.dao.SessionDao;
import com.abin.chatserver.chat.domain.entity.Session;
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
public class SessionCache extends AbstractRedisStringCache<Long, Session> {

    private final SessionDao sessionDao;

    @Override
    protected String getKey(Long sessionId) {
        return RedisKey.getKey(RedisKey.SESSION_INFO_STRING, sessionId);
    }

    @Override
    protected Long getExpireTime() {
        return 5 * 60L;
    }

    @Override
    protected Map<Long, Session> load(List<Long> sessionIds) {
        List<Session> sessions = sessionDao.listByIds(sessionIds);
        return sessions.stream().collect(Collectors.toMap(Session::getId, Function.identity()));
    }
}
