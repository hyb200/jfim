package com.abin.chatserver.common.event;

import com.abin.chatserver.chat.domain.dto.ChatMsgRecallDTO;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class MessageRecallEvent extends ApplicationEvent {

    private final ChatMsgRecallDTO chatMsgRecallDTO;

    public MessageRecallEvent(Object source, ChatMsgRecallDTO chatMsgRecallDTO) {
        super(source);
        this.chatMsgRecallDTO = chatMsgRecallDTO;
    }
}
