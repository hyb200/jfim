package com.abin.chatserver.chat.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.abin.chatserver.chat.domain.entity.Message;
import generator.service.MessageService;
import com.abin.chatserver.chat.mapper.MessageMapper;
import org.springframework.stereotype.Service;

/**
* @author Yibin Huang
* @description 针对表【message(消息表)】的数据库操作
* @createDate 2024-07-17 23:23:22
*/
@Service
public class MessageDao extends ServiceImpl<MessageMapper, Message> {

    public Integer getGapCount(Long sessionId, Long from, Long to) {
        return Math.toIntExact(lambdaQuery().eq(Message::getSessionId, sessionId)
                .gt(Message::getId, from)
                .le(Message::getId, to)
                .count());
    }
}




