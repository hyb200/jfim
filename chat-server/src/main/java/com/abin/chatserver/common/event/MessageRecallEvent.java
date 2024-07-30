package com.abin.chatserver.common.event;

import com.abin.chatserver.chat.domain.vo.resp.ChatMessageResp;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class MessageRecallEvent extends ApplicationEvent {

    private final ChatMessageResp chatMessageResp;

    public MessageRecallEvent(Object source, ChatMessageResp chatMsgRecallDTO) {
        super(source);
        this.chatMessageResp = chatMsgRecallDTO;
    }
}
