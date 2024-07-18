package com.abin.chatserver.chat.service.strategy.msg;

import com.abin.chatserver.chat.domain.entity.Message;
import com.abin.chatserver.chat.domain.entity.msg.ImgMsgDTO;
import com.abin.chatserver.chat.domain.enums.MessageTypeEnum;
import org.springframework.stereotype.Component;

@Component
public class ImgMsgHandler extends AbstractMsgHandler<ImgMsgDTO> {
    
    @Override
    MessageTypeEnum getMessageTypeEnum() {
        return MessageTypeEnum.IMG;
    }

    @Override
    protected void checkMsg(ImgMsgDTO body, Long sessionId, Long uid) {

    }

    @Override
    protected void saveMsg(Message message, ImgMsgDTO body) {

    }

    @Override
    public Object showMsg(Message msg) {
        return msg.getExtra().getImgMsgDTO();
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
