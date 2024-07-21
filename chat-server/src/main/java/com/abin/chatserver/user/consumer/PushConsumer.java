package com.abin.chatserver.user.consumer;

import com.abin.chatserver.common.constants.MQConstant;
import com.abin.chatserver.common.domain.dto.PushMessageDTO;
import com.abin.chatserver.common.domain.enums.WSPushTypeEnum;
import com.abin.chatserver.websocket.WebSocketService;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@RocketMQMessageListener(consumerGroup = MQConstant.PUSH_GROUP, topic = MQConstant.PUSH_TOPIC, messageModel = MessageModel.BROADCASTING)
@Component
@RequiredArgsConstructor
public class PushConsumer implements RocketMQListener<PushMessageDTO> {

    private final WebSocketService webSocketService;

    @Override
    public void onMessage(PushMessageDTO message) {
        WSPushTypeEnum wsPushTypeEnum = WSPushTypeEnum.of(message.getPushType());
        switch (wsPushTypeEnum) {
            case USER -> message.getUids().forEach(uid -> webSocketService.sendToUid(message.getMsg(), uid));
            case ALL -> webSocketService.sendToAllOnline(message.getMsg(), null);
        }
    }
}
