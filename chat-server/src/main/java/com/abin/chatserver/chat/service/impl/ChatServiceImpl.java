package com.abin.chatserver.chat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.abin.chatserver.chat.dao.GroupMemberDao;
import com.abin.chatserver.chat.dao.MessageDao;
import com.abin.chatserver.chat.dao.SessionSingleDao;
import com.abin.chatserver.chat.domain.entity.*;
import com.abin.chatserver.chat.domain.vo.req.ChatMessageReq;
import com.abin.chatserver.chat.domain.vo.resp.ChatMessageResp;
import com.abin.chatserver.chat.service.ChatService;
import com.abin.chatserver.chat.service.cache.SessionCache;
import com.abin.chatserver.chat.service.cache.SessionGroupCache;
import com.abin.chatserver.chat.service.strategy.msg.AbstractMsgHandler;
import com.abin.chatserver.chat.service.strategy.msg.MsgHandlerFactory;
import com.abin.chatserver.common.domain.enums.CommonStatusEnum;
import com.abin.chatserver.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final GroupMemberDao groupMemberDao;

    private final SessionSingleDao sessionSingleDao;

    private final SessionCache sessionCache;

    private final SessionGroupCache sessionGroupCache;

    private final MessageDao messageDao;

    @Override
    public Long sendMsg(Long uid, ChatMessageReq req) {
        check(uid, req);
        AbstractMsgHandler<?> handler = MsgHandlerFactory.getHandler(req.getMsgType());
        Long msgId = handler.checkAndSaveMsg(uid, req);
        //  todo 发布消息发送事件
        return msgId;
    }

    @Override
    public ChatMessageResp getMsgResp(Long receiver, Long msgId) {
        Message msg = messageDao.getById(msgId);
        return getMsgResp(receiver, msg);
    }

    @Override
    public ChatMessageResp getMsgResp(Long receiver, Message msg) {
        return CollUtil.getFirst(getMsgRespBatch(Collections.singletonList(msg), receiver));
    }

    private List<ChatMessageResp> getMsgRespBatch(List<Message> messages, Long receiver) {
        if (CollUtil.isEmpty(messages)) {
            return new ArrayList<>();
        }

        return messages.stream()
                .map(o -> {
                    ChatMessageResp.UserInfo fromUser = ChatMessageResp.UserInfo.builder().uid(o.getFromUid()).build();

                    ChatMessageResp.Message messageVO = new ChatMessageResp.Message();
                    BeanUtil.copyProperties(o, messageVO);
                    messageVO.setSendTime(o.getCreateTime());
                    AbstractMsgHandler<?> handler = MsgHandlerFactory.getHandler(o.getType());
                    if (Objects.nonNull(handler)) {
                        messageVO.setBody(handler.showMsg(o));
                    }

                    return ChatMessageResp.builder()
                            .fromUser(fromUser)
                            .message(messageVO)
                            .build();
                })
                .sorted(Comparator.comparing(o -> o.getMessage().getSendTime()))
                .toList();
    }


    private void check(Long uid, ChatMessageReq req) {
        Session session = sessionCache.get(req.getSessionId());
        if (session.isHotSession()) return;

        if (session.isSingleSession()) {
            SessionSingle sessionSingle = sessionSingleDao.getBySessionId(req.getSessionId());
            Integer status = sessionSingle.getStatus();
            if (ObjectUtil.equal(status, CommonStatusEnum.NOT_NORMAL.getStatus())) {
                throw new BusinessException("您已经被对方拉黑");
            }
        }

        if (session.isGroupSession()) {
            SessionGroup sessionGroup = sessionGroupCache.get(req.getSessionId());
            GroupMember groupMember = groupMemberDao.getMember(sessionGroup.getSessionId(), uid);
            if (Objects.isNull(groupMember)) {
                throw new BusinessException("您已经被移除该群");
            }
        }
    }
}
