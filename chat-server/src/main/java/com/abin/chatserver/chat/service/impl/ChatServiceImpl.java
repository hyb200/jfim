package com.abin.chatserver.chat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.abin.chatserver.chat.dao.ContactDao;
import com.abin.chatserver.chat.dao.GroupMemberDao;
import com.abin.chatserver.chat.dao.MessageDao;
import com.abin.chatserver.chat.dao.SessionSingleDao;
import com.abin.chatserver.chat.domain.entity.*;
import com.abin.chatserver.chat.domain.enums.MessageTypeEnum;
import com.abin.chatserver.chat.domain.vo.req.ChatMessagePageReq;
import com.abin.chatserver.chat.domain.vo.req.ChatMessageReq;
import com.abin.chatserver.chat.domain.vo.req.RecallMsgReq;
import com.abin.chatserver.chat.domain.vo.resp.ChatMessageResp;
import com.abin.chatserver.chat.service.ChatService;
import com.abin.chatserver.chat.service.cache.SessionCache;
import com.abin.chatserver.chat.service.cache.SessionGroupCache;
import com.abin.chatserver.chat.service.strategy.msg.AbstractMsgHandler;
import com.abin.chatserver.chat.service.strategy.msg.MsgHandlerFactory;
import com.abin.chatserver.chat.service.strategy.msg.RecallMsgHandler;
import com.abin.chatserver.common.domain.enums.CommonStatusEnum;
import com.abin.chatserver.common.event.MessageSendEvent;
import com.abin.chatserver.common.exception.BusinessException;
import com.abin.chatserver.user.domain.entity.User;
import com.abin.chatserver.user.domain.vo.resp.CursorPageBaseResp;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final GroupMemberDao groupMemberDao;

    private final SessionSingleDao sessionSingleDao;

    private final SessionCache sessionCache;

    private final SessionGroupCache sessionGroupCache;

    private final MessageDao messageDao;

    private final RecallMsgHandler recallMsgHandler;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final ContactDao contactDao;

    @Override
    @Transactional
    public Long sendMsg(Long uid, ChatMessageReq req) {
        check(uid, req);
        AbstractMsgHandler<?> handler = MsgHandlerFactory.getHandler(req.getMsgType());
        Long msgId = handler.checkAndSaveMsg(uid, req);

        applicationEventPublisher.publishEvent(new MessageSendEvent(this, msgId));
        return msgId;
    }

    @Override
    public ChatMessageResp getMsgResp(Long msgId) {
        Message msg = messageDao.getById(msgId);
        return getMsgResp(msg);
    }

    public ChatMessageResp getMsgResp(Message msg) {
        return CollUtil.getFirst(getMsgRespBatch(Collections.singletonList(msg)));
    }

    @Override
    public void recallMsg(Long uid, RecallMsgReq req) {
        Message message = messageDao.getById(req.getMsgId());
        recallCheck(uid, message);
        recallMsgHandler.recall(uid, message);
    }

    @Override
    public CursorPageBaseResp<ChatMessageResp> getMsgPage(Long receiverUid, ChatMessagePageReq req) {
        Long lastMsgId = getLastMsgId(req.getSessionId(), receiverUid);
        CursorPageBaseResp<Message> cursorPage = messageDao.getCursorPage(req.getSessionId(), req, lastMsgId);
        if (cursorPage.isEmpty()) {
            return CursorPageBaseResp.empty();
        }
        return CursorPageBaseResp.init(cursorPage, getMsgRespBatch(cursorPage.getList()));
    }

    private Long getLastMsgId(Long sessionId, Long receiverUid) {
        Session session = sessionCache.get(sessionId);
        if (Objects.isNull(session)) {
            throw new BusinessException("会话id有误");
        }
        if (session.isHotSession()) return null;
        if (Objects.isNull(receiverUid)) {
            throw new BusinessException("请先登录");
        }
        Contact contact = contactDao.get(receiverUid, sessionId);
        return contact.getLastMsgId();
    }

    private void recallCheck(Long uid, Message message) {
        if (Objects.isNull(message) || message.getType().equals(MessageTypeEnum.RECALL.getType())) {
            throw new BusinessException("消息撤回失败");
        }

        if (!uid.equals(message.getFromUid())) {
            throw new BusinessException("抱歉，没有权限");
        }

        long between = DateUtil.between(message.getCreateTime(), new Date(), DateUnit.MINUTE);
        if (between > 2) {
            throw new BusinessException("消息超过2分钟无法撤回");
        }
    }

    private List<ChatMessageResp> getMsgRespBatch(List<Message> messages) {
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
        if (uid.equals(User.SYSTEM_UID)) return;
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
            GroupMember groupMember = groupMemberDao.getMember(sessionGroup.getId(), uid);
            if (Objects.isNull(groupMember)) {
                throw new BusinessException("您已经被移除该群");
            }
        }
    }
}
