package com.abin.chatserver.chat.service.strategy.msg;

import com.abin.chatserver.chat.domain.entity.Message;
import com.abin.chatserver.chat.domain.enums.MessageTypeEnum;
import org.springframework.stereotype.Component;

@Component
public class SystemMsgHandler extends AbstractMsgHandler<String> {
    
    @Override
    MessageTypeEnum getMessageTypeEnum() {
        return MessageTypeEnum.SYSTEM;
    }

    @Override
    protected void checkMsg(String body, Long sessionId, Long uid) {

    }

    @Override
    protected void saveMsg(Message message, String body) {

    }

    @Override
    public Object showMsg(Message msg) {
        return msg.getContent();
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
