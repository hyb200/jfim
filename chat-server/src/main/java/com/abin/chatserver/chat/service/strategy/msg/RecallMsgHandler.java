package com.abin.chatserver.chat.service.strategy.msg;

import com.abin.chatserver.chat.domain.entity.Message;
import com.abin.chatserver.chat.domain.enums.MessageTypeEnum;
import org.springframework.stereotype.Component;

@Component
public class RecallMsgHandler extends AbstractMsgHandler<Object> {
    
    @Override
    MessageTypeEnum getMessageTypeEnum() {
        return MessageTypeEnum.RECALL;
    }

    @Override
    protected void checkMsg(Object body, Long sessionId, Long uid) {

    }

    @Override
    protected void saveMsg(Message message, Object body) {

    }

    @Override
    public Object showMsg(Message msg) {
        return null;
    }

    @Override
    public Object showReplyMsg(Message msg) {
        return null;
    }

    @Override
    public String showContactMsg(Message msg) {
        return "";
    }
}
