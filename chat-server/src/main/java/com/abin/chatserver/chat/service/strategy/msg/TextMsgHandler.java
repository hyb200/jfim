package com.abin.chatserver.chat.service.strategy.msg;

import cn.hutool.core.collection.CollUtil;
import com.abin.chatserver.chat.dao.GroupMemberDao;
import com.abin.chatserver.chat.dao.MessageDao;
import com.abin.chatserver.chat.dao.SessionDao;
import com.abin.chatserver.chat.domain.entity.Message;
import com.abin.chatserver.chat.domain.entity.Session;
import com.abin.chatserver.chat.domain.entity.msg.MessageExtra;
import com.abin.chatserver.chat.domain.enums.MessageStatusEnum;
import com.abin.chatserver.chat.domain.vo.req.TextMsgReq;
import com.abin.chatserver.chat.domain.enums.MessageTypeEnum;
import com.abin.chatserver.chat.domain.vo.resp.TextMsgResp;
import com.abin.chatserver.chat.service.cache.MsgCache;
import com.abin.chatserver.common.exception.BusinessException;
import com.abin.chatserver.user.domain.dto.UserInfoDTO;
import com.abin.chatserver.user.domain.enums.YesOrNoEnum;
import com.abin.chatserver.user.service.cache.UserCache;
import com.abin.chatserver.user.service.cache.UserInfoCache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class TextMsgHandler extends AbstractMsgHandler<TextMsgReq> {

    private final MessageDao messageDao;
    private final UserInfoCache userInfoCache;
    private final SessionDao sessionDao;
    private final GroupMemberDao groupMemberDao;
    private final MsgCache msgCache;
    private final UserCache userCache;

    @Override
    MessageTypeEnum getMessageTypeEnum() {
        return MessageTypeEnum.TEXT;
    }

    @Override
    protected void checkMsg(TextMsgReq body, Long sessionId, Long uid) {
        if (Objects.nonNull(body.getReplyMsgId())) {
            Message repliedMsg = messageDao.getById(body.getReplyMsgId());
            if (Objects.isNull(repliedMsg)) throw new BusinessException("回复消息不存在");
            if (!repliedMsg.getSessionId().equals(sessionId)) throw new BusinessException("只能回复相同会话内的消息");
        }

        if (CollUtil.isNotEmpty(body.getAtUidList())) {
            List<Long> atUidList = body.getAtUidList().stream().distinct().toList();
            Map<Long, UserInfoDTO> batch = userInfoCache.getBatch(atUidList);
            long batchCount = batch.values().stream().filter(Objects::nonNull).count();
            if (batchCount != atUidList.size()) throw new BusinessException("@的用户不存在");

            //  艾特全员权限判断
            boolean atAll = body.getAtUidList().contains(0L);
            if (atAll && isGroupSession(sessionId) && !isGroupManager(sessionId, uid)) {
                throw new BusinessException("没有权限");
            }
        }
    }

    private boolean isGroupManager(Long sessionId, Long uid) {
        return groupMemberDao.isManager(sessionId, uid);
    }

    private boolean isGroupSession(Long sessionId) {
        Session session = sessionDao.getBySessionId(sessionId);
        return session.isGroupSession();
    }

    @Override
    protected void saveMsg(Message message, TextMsgReq body) {
        MessageExtra extra = Optional.ofNullable(message.getExtra()).orElse(new MessageExtra());

        Message update = Message.builder().id(message.getId())
                .content(body.getContent())
                .extra(extra).build();

        if (Objects.nonNull(body.getReplyMsgId())) {
            Integer gapCount = messageDao.getGapCount(message.getSessionId(), body.getReplyMsgId(), message.getId());
            update.setGapCount(gapCount);
            update.setReplyMsgId(body.getReplyMsgId());
        }
        //  todo url 解析卡片
        if (CollUtil.isNotEmpty(body.getAtUidList())) {
            extra.setAtUidList(body.getAtUidList());
        }

        messageDao.updateById(update);
    }

    @Override
    public Object showMsg(Message msg) {
        TextMsgResp textMsgResp = new TextMsgResp();
        textMsgResp.setContent(msg.getContent());
        textMsgResp.setAtUidList(Optional.ofNullable(msg.getExtra()).map(MessageExtra::getAtUidList).orElse(null));
        //  回复消息
        Optional<Message> reply = Optional.ofNullable(msg.getReplyMsgId())
                .map(msgCache::getMsg)
                .filter(o -> Objects.equals(o.getStatus(), MessageStatusEnum.NORMAL.getStatus()));

        if (reply.isPresent()) {
            Message replyMsg = reply.get();
            TextMsgResp.ReplyMsg replyMsgVO = new TextMsgResp.ReplyMsg();
            replyMsgVO.setId(replyMsg.getId());
            replyMsgVO.setUid(replyMsg.getFromUid());
            replyMsgVO.setUsername(userCache.getUserInfo(replyMsg.getFromUid()).getNickname());
            replyMsgVO.setType(replyMsg.getType());
            replyMsgVO.setBody(MsgHandlerFactory.getHandler(replyMsg.getType()).showReplyMsg(replyMsg));
            replyMsgVO.setCanCallback(YesOrNoEnum.toStatus(Objects.nonNull(msg.getGapCount()) && msg.getGapCount() <= 100));
            replyMsgVO.setGapCount(msg.getGapCount());
            textMsgResp.setReply(replyMsgVO);
        }
        return textMsgResp;
    }

    @Override
    public Object showReplyMsg(Message msg) {
        return msg.getContent();
    }

    @Override
    public String showContactMsg(Message msg) {
        return msg.getContent();
    }
}
