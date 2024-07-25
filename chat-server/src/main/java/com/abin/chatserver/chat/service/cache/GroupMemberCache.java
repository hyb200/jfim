package com.abin.chatserver.chat.service.cache;

import com.abin.chatserver.chat.dao.GroupMemberDao;
import com.abin.chatserver.chat.dao.SessionGroupDao;
import com.abin.chatserver.chat.domain.entity.SessionGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class GroupMemberCache {

    private final SessionGroupDao sessionGroupDao;
    private final GroupMemberDao groupMemberDao;

    @Cacheable(cacheNames = "member", key = "'groupMember'+#sessionId")
    public List<Long> getMemberUids(Long sessionId) {
        SessionGroup sessionGroup = sessionGroupDao.getBySessionId(sessionId);
        if (Objects.isNull(sessionGroup)) {
            return null;
        }
        return groupMemberDao.getMemberUids(sessionGroup.getId());
    }

    @CacheEvict(cacheNames = "member", key = "'groupMember'+#sessionId")
    public List<Long> evictMemberUids(Long sessionId) {
        return null;
    }
}
