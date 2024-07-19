package com.abin.chatserver.chat.service.strategy.msg;

import com.abin.chatserver.chat.dao.MessageDao;
import com.abin.chatserver.chat.domain.entity.Message;
import com.abin.chatserver.chat.domain.entity.msg.EmojisMsgDTO;
import com.abin.chatserver.chat.domain.entity.msg.MessageExtra;
import com.abin.chatserver.chat.domain.enums.MessageTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EmojiMsgHandler extends AbstractMsgHandler<EmojisMsgDTO> {

    private final MessageDao messageDao;

    @Override
    MessageTypeEnum getMessageTypeEnum() {
        return MessageTypeEnum.EMOJI;
    }

    @Override
    protected void saveMsg(Message message, EmojisMsgDTO body) {
        MessageExtra extra = Optional.ofNullable(message.getExtra()).orElse(new MessageExtra());
        Message update = Message.builder()
                .id(message.getId())
                .extra(extra)
                .build();
        extra.setEmojisMsgDTO(body);
        messageDao.updateById(update);
    }

    @Override
    public Object showMsg(Message msg) {
        return msg.getExtra().getEmojisMsgDTO();
    }

    @Override
    public Object showReplyMsg(Message msg) {
        return "表情";
    }

    @Override
    public String showContactMsg(Message msg) {
        return "[表情包]";
    }
}
