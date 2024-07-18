package com.abin.chatserver.chat.service.strategy.msg;

import com.abin.chatserver.chat.domain.entity.Message;
import com.abin.chatserver.chat.domain.entity.msg.FileMsgDTO;
import com.abin.chatserver.chat.domain.enums.MessageTypeEnum;
import org.springframework.stereotype.Component;

@Component
public class FileMsgHandler extends AbstractMsgHandler<FileMsgDTO> {
    
    @Override
    MessageTypeEnum getMessageTypeEnum() {
        return MessageTypeEnum.FILE;
    }

    @Override
    protected void checkMsg(FileMsgDTO body, Long sessionId, Long uid) {

    }

    @Override
    protected void saveMsg(Message message, FileMsgDTO body) {

    }

    @Override
    public Object showMsg(Message msg) {
        return msg.getExtra().getFileMsg();
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
