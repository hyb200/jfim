package com.abin.chatserver.chat.service.strategy.msg;

import com.abin.chatserver.chat.domain.entity.Message;
import com.abin.chatserver.chat.domain.entity.msg.EmojisMsgDTO;
import com.abin.chatserver.chat.domain.enums.MessageTypeEnum;
import org.springframework.stereotype.Component;

@Component
public class EmojiMsgHandler extends AbstractMsgHandler<EmojisMsgDTO> {

    @Override
    MessageTypeEnum getMessageTypeEnum() {
        return MessageTypeEnum.EMOJI;
    }

    @Override
    protected void checkMsg(EmojisMsgDTO body, Long sessionId, Long uid) {

    }

    @Override
    protected void saveMsg(Message message, EmojisMsgDTO body) {

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
