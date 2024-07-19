package com.abin.chatserver.chat.service.strategy.msg;

import com.abin.chatserver.chat.dao.MessageDao;
import com.abin.chatserver.chat.domain.entity.Message;
import com.abin.chatserver.chat.domain.entity.msg.MessageExtra;
import com.abin.chatserver.chat.domain.entity.msg.VideoMsgDTO;
import com.abin.chatserver.chat.domain.enums.MessageTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class VideoMsgHandler extends AbstractMsgHandler<VideoMsgDTO> {

    private final MessageDao messageDao;

    @Override
    MessageTypeEnum getMessageTypeEnum() {
        return MessageTypeEnum.VIDEO;
    }

    @Override
    protected void saveMsg(Message message, VideoMsgDTO body) {
        MessageExtra extra = Optional.ofNullable(message.getExtra()).orElse(new MessageExtra());
        Message update = Message.builder()
                .id(message.getId())
                .extra(extra)
                .build();
        extra.setVideoMsgDTO(body);
        messageDao.updateById(update);
    }

    @Override
    public Object showMsg(Message msg) {
        return msg.getExtra().getVideoMsgDTO();
    }

    @Override
    public Object showReplyMsg(Message msg) {
        return "视频";
    }

    @Override
    public String showContactMsg(Message msg) {
        return "[视频]";
    }
}
