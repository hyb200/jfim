package com.abin.chatserver.chat.service.strategy.msg;

import com.abin.chatserver.chat.domain.entity.Message;
import com.abin.chatserver.chat.domain.entity.msg.VideoMsgDTO;
import com.abin.chatserver.chat.domain.enums.MessageTypeEnum;
import org.springframework.stereotype.Component;

@Component
public class VideoMsgHandler extends AbstractMsgHandler<VideoMsgDTO> {
    
    @Override
    MessageTypeEnum getMessageTypeEnum() {
        return MessageTypeEnum.VIDEO;
    }

    @Override
    protected void checkMsg(VideoMsgDTO body, Long sessionId, Long uid) {

    }

    @Override
    protected void saveMsg(Message message, VideoMsgDTO body) {

    }

    @Override
    public Object showMsg(Message msg) {
        return msg.getExtra().getVideoMsgDTO();
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
