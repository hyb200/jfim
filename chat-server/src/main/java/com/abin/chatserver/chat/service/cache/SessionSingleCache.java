package com.abin.chatserver.chat.service.cache;

import com.abin.chatserver.chat.dao.SessionSingleDao;
import com.abin.chatserver.chat.domain.entity.SessionSingle;
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
public class SessionSingleCache extends AbstractRedisStringCache<Long, SessionSingle> {

    private final SessionSingleDao sessionSingleDao;

    @Override
    protected String getKey(Long key) {
        return RedisKey.getKey(RedisKey.SINGLE_INFO_STRING, key);
    }

    @Override
    protected Long getExpireTime() {
        return 5 * 60L;
    }

    @Override
    protected Map<Long, SessionSingle> load(List<Long> sessionIds) {
        List<SessionSingle> sessionSingles = sessionSingleDao.listBySessionIds(sessionIds);
        return sessionSingles.stream().collect(Collectors.toMap(SessionSingle::getSessionId, Function.identity()));
    }
}
