package com.abin.chatserver.user.service.impl;

import com.abin.chatserver.common.constants.MQConstant;
import com.abin.chatserver.common.domain.dto.PushMessageDTO;
import com.abin.chatserver.user.domain.vo.req.WSBaseReq;
import com.abin.chatserver.user.domain.vo.resp.WSBaseResp;
import com.abin.transaction.service.MQProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PushMsgService {

    private final MQProducer mqProducer;

    public void sendPushMsg(WSBaseResp<?> msg, List<Long> uids) {
        mqProducer.sendMsg(MQConstant.PUSH_TOPIC, new PushMessageDTO(msg, uids));
    }

    public void sendPushMsg(WSBaseResp<?> msg, Long uid) {
        mqProducer.sendMsg(MQConstant.PUSH_TOPIC, new PushMessageDTO(msg, uid));
    }

    public void sendPushMsg(WSBaseResp<?> msg) {
        mqProducer.sendMsg(MQConstant.PUSH_TOPIC, new PushMessageDTO(msg));
    }
}
