package com.abin.chatserver.common.event.listener;

import cn.hutool.core.bean.BeanUtil;
import com.abin.chatserver.chat.domain.vo.resp.ChatMessageResp;
import com.abin.chatserver.chat.service.cache.MsgCache;
import com.abin.chatserver.common.domain.enums.WSResqTypeEnum;
import com.abin.chatserver.common.domain.vo.response.WSBaseResp;
import com.abin.chatserver.common.domain.vo.response.WSMessage;
import com.abin.chatserver.common.event.MessageRecallEvent;
import com.abin.chatserver.user.service.impl.PushMsgService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageRecallListener {

    private final PushMsgService pushMsgService;

    private final MsgCache msgCache;

    @Async
    @TransactionalEventListener(classes = MessageRecallEvent.class, fallbackExecution = true)
    public void evictMsg(MessageRecallEvent event) {
        ChatMessageResp chatMsgRecallDTO = event.getChatMessageResp();
        msgCache.evictMsg(chatMsgRecallDTO.getMessage().getId());
    }

    @Async
    @TransactionalEventListener(classes = MessageRecallEvent.class, fallbackExecution = true)
    public void sendToAll(MessageRecallEvent event) {
        WSBaseResp<WSMessage> wsBaseResp = new WSBaseResp<>();
        wsBaseResp.setType(WSResqTypeEnum.RECALL.getType());
//        WSMsgRecall wsMsgRecall = new WSMsgRecall();
        WSMessage wsMessage = new WSMessage();
        BeanUtil.copyProperties(event.getChatMessageResp(), wsMessage);
        wsBaseResp.setData(wsMessage);
        pushMsgService.sendPushMsg(wsBaseResp);
    }
}
