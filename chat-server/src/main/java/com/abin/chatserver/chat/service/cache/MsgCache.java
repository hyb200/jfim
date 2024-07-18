package com.abin.chatserver.chat.service.cache;

import com.abin.chatserver.chat.dao.MessageDao;
import com.abin.chatserver.chat.domain.entity.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MsgCache {

    private final MessageDao messageDao;

    @Cacheable(cacheNames = "msg", key = "'msg'+#msgId")
    public Message getMsg(Long msgId) {
        return messageDao.getById(msgId);
    }

    @CacheEvict(cacheNames = "msg", key = "'msg'+#msgId")
    public void evictMsg(Long msgId) {}
}
