package com.abin.chatserver.chat.service.strategy.msg;

import com.abin.chatserver.chat.dao.MessageDao;
import com.abin.chatserver.chat.domain.entity.Message;
import com.abin.chatserver.chat.domain.entity.msg.FileMsgDTO;
import com.abin.chatserver.chat.domain.entity.msg.MessageExtra;
import com.abin.chatserver.chat.domain.enums.MessageTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FileMsgHandler extends AbstractMsgHandler<FileMsgDTO> {

    private final MessageDao messageDao;

    @Override
    MessageTypeEnum getMessageTypeEnum() {
        return MessageTypeEnum.FILE;
    }

    @Override
    protected void saveMsg(Message message, FileMsgDTO body) {
        MessageExtra extra = Optional.ofNullable(message.getExtra()).orElse(new MessageExtra());
        Message update = Message.builder()
                .id(message.getId())
                .extra(extra)
                .build();
        extra.setFileMsgDTO(body);
        messageDao.updateById(update);
    }

    @Override
    public Object showMsg(Message msg) {
        return msg.getExtra().getFileMsgDTO();
    }

    @Override
    public Object showReplyMsg(Message msg) {
        return "文件：" + msg.getExtra().getFileMsgDTO().getFileName();
    }

    @Override
    public String showContactMsg(Message msg) {
        return "[文件]" + msg.getExtra().getFileMsgDTO().getFileName();
    }
}
