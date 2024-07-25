package com.abin.chatserver.common.event.listener;

import cn.hutool.core.bean.BeanUtil;
import com.abin.chatserver.chat.domain.dto.ChatMsgRecallDTO;
import com.abin.chatserver.chat.service.cache.MsgCache;
import com.abin.chatserver.common.domain.enums.WSResqTypeEnum;
import com.abin.chatserver.common.domain.vo.response.WSBaseResp;
import com.abin.chatserver.common.domain.vo.response.WSMsgRecall;
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
        ChatMsgRecallDTO chatMsgRecallDTO = event.getChatMsgRecallDTO();
        msgCache.evictMsg(chatMsgRecallDTO.getMsgId());
    }

    @Async
    @TransactionalEventListener(classes = MessageRecallEvent.class, fallbackExecution = true)
    public void sendToAll(MessageRecallEvent event) {
        WSBaseResp<WSMsgRecall> wsBaseResp = new WSBaseResp<>();
        wsBaseResp.setType(WSResqTypeEnum.RECALL.getType());
        WSMsgRecall wsMsgRecall = new WSMsgRecall();
        BeanUtil.copyProperties(event.getChatMsgRecallDTO(), wsMsgRecall);
        wsBaseResp.setData(wsMsgRecall);
        pushMsgService.sendPushMsg(wsBaseResp);
    }
}
