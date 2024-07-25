package com.abin.chatserver.chat.consumer;

import com.abin.chatserver.chat.dao.*;
import com.abin.chatserver.chat.domain.entity.Message;
import com.abin.chatserver.chat.domain.entity.Session;
import com.abin.chatserver.chat.domain.entity.SessionSingle;
import com.abin.chatserver.chat.domain.enums.SessionTypeEnum;
import com.abin.chatserver.chat.domain.vo.resp.ChatMessageResp;
import com.abin.chatserver.chat.service.ChatService;
import com.abin.chatserver.chat.service.cache.HotSessionCache;
import com.abin.chatserver.chat.service.cache.SessionCache;
import com.abin.chatserver.common.constants.MQConstant;
import com.abin.chatserver.common.domain.dto.MsgSendMessageDTO;
import com.abin.chatserver.common.domain.enums.WSResqTypeEnum;
import com.abin.chatserver.common.domain.vo.response.WSBaseResp;
import com.abin.chatserver.user.service.impl.PushMsgService;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RocketMQMessageListener(consumerGroup = MQConstant.SEND_MSG_GROUP, topic = MQConstant.SEND_MSG_TOPIC)
@Component
@RequiredArgsConstructor
public class MsgSendConsumer implements RocketMQListener<MsgSendMessageDTO> {

    private final MessageDao messageDao;

    private final SessionCache sessionCache;

    private final ChatService chatService;

    private final SessionDao sessionDao;

    private final HotSessionCache hotSessionCache;

    private final PushMsgService pushMsgService;

    private final GroupMemberDao groupMemberDao;

    private final SessionSingleDao sessionSingleDao;

    private final ContactDao contactDao;

    @Override
    public void onMessage(MsgSendMessageDTO dto) {
        Message message = messageDao.getById(dto.getMsgId());
        Session session = sessionCache.get(message.getSessionId());
        ChatMessageResp msgResp = chatService.getMsgResp(message);

        sessionDao.refreshActiveTime(session.getId(), message.getId(), message.getCreateTime());
        sessionCache.delete(session.getId());
        WSBaseResp<ChatMessageResp> pushMsg = WSBaseResp.<ChatMessageResp>builder()
                .type(WSResqTypeEnum.MESSAGE.getType())
                .data(msgResp)
                .build();
        if (session.isHotSession()) {
            hotSessionCache.refreshActiveTime(session.getId(), message.getCreateTime());
            //  全员推送
            pushMsgService.sendPushMsg(pushMsg);
        } else {
            List<Long> uids = new ArrayList<>();
            if (Objects.equals(session.getType(), SessionTypeEnum.GROUP.getType())) {
                uids = groupMemberDao.getMemberUids(session.getId());
            } else if (Objects.equals(session.getType(), SessionTypeEnum.SINGLE.getType())) {
                SessionSingle sessionSingle = sessionSingleDao.getBySessionId(session.getId());
                uids = Arrays.asList(sessionSingle.getUid1(), sessionSingle.getUid2());
            }
            contactDao.refreshOrCreateActiveTime(session.getId(), uids, message.getId(), message.getCreateTime());
            pushMsgService.sendPushMsg(pushMsg, uids);
        }
    }
}
