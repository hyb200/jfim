package com.abin.chatserver.chat.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Pair;
import com.abin.chatserver.chat.dao.ContactDao;
import com.abin.chatserver.chat.dao.MessageDao;
import com.abin.chatserver.chat.domain.dto.SessionBaseInfo;
import com.abin.chatserver.chat.domain.entity.*;
import com.abin.chatserver.chat.domain.enums.SessionTypeEnum;
import com.abin.chatserver.chat.domain.vo.resp.ChatSessionResp;
import com.abin.chatserver.chat.service.ContactService;
import com.abin.chatserver.chat.service.SessionService;
import com.abin.chatserver.chat.service.cache.HotSessionCache;
import com.abin.chatserver.chat.service.cache.SessionCache;
import com.abin.chatserver.chat.service.cache.SessionGroupCache;
import com.abin.chatserver.chat.service.cache.SessionSingleCache;
import com.abin.chatserver.chat.service.strategy.msg.AbstractMsgHandler;
import com.abin.chatserver.chat.service.strategy.msg.MsgHandlerFactory;
import com.abin.chatserver.common.exception.BusinessException;
import com.abin.chatserver.user.domain.dto.UserInfoDTO;
import com.abin.chatserver.user.domain.entity.User;
import com.abin.chatserver.user.domain.vo.req.CursorPageBaseReq;
import com.abin.chatserver.user.domain.vo.resp.CursorPageBaseResp;
import com.abin.chatserver.user.service.cache.UserInfoCache;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

    private final ContactDao contactDao;

    private final HotSessionCache hotSessionCache;

    private final MessageDao messageDao;

    private final UserInfoCache userInfoCache;

    private final SessionCache sessionCache;

    private final SessionGroupCache sessionGroupCache;

    private final SessionSingleCache sessionSingleCache;

    private final SessionService sessionService;

    @Override
    public CursorPageBaseResp<ChatSessionResp> getContactPage(Long uid, CursorPageBaseReq req) {
        CursorPageBaseResp<Long> page;
        Double hotEnd = Optional.ofNullable(req.getCursor()).map(Double::parseDouble).orElse(null);
        Double hotStart = null;
        //  获取普通会话
        CursorPageBaseResp<Contact> contactPage = contactDao.getContactPage(uid, req);
        List<Long> baseSessionIds = new ArrayList<>(contactPage.getList().stream().map(Contact::getSessionId).toList());
        if (!contactPage.getIsLast()) {
            hotStart = Optional.ofNullable(contactPage.getCursor()).map(Double::parseDouble).orElse(null);
        }
        Set<ZSetOperations.TypedTuple<String>> typedTuples = hotSessionCache.getSessionRange(hotStart, hotEnd);
        List<Long> hotSessionIds = typedTuples.stream()
                .map(ZSetOperations.TypedTuple::getValue)
                .filter(Objects::nonNull)
                .map(Long::parseLong)
                .toList();
        baseSessionIds.addAll(hotSessionIds);
        page = CursorPageBaseResp.init(contactPage, baseSessionIds);

        if (baseSessionIds.isEmpty()) {
            return CursorPageBaseResp.empty();
        }
        List<ChatSessionResp> result = buildContactResp(uid, baseSessionIds);
        return CursorPageBaseResp.init(page, result);
    }

    @Override
    public ChatSessionResp getContactDetail(Long uid, Long sessionId) {
        Session session = sessionCache.get(sessionId);
        if (Objects.isNull(session)) {
            throw new BusinessException("会话ID错误");
        }
        return buildContactResp(uid, Collections.singletonList(sessionId)).get(0);
    }

    @Override
    public ChatSessionResp getContactDetailByFriend(Long uid, Long friendUid) {
        SessionSingle sessionSingle = sessionService.getSingleSession(uid, friendUid);
        return buildContactResp(uid, Collections.singletonList(sessionSingle.getSessionId())).get(0);
    }

    @NotNull
    private List<ChatSessionResp> buildContactResp(Long uid, List<Long> sessionIds) {
        Map<Long, SessionBaseInfo> sessionBaseInfoMap = getSessionBaseInfoMap(uid, sessionIds);
        List<Long> lastMsgIds = sessionBaseInfoMap.values().stream().map(SessionBaseInfo::getLastMsgId).toList();
        List<Message> messages = lastMsgIds.isEmpty() ? new ArrayList<>() : messageDao.listByIds(lastMsgIds);
        Map<Long, Message> msgMap = messages.stream().collect(Collectors.toMap(Message::getId, Function.identity()));
        Map<Long, UserInfoDTO> lastMsgUids = userInfoCache.getBatch(messages.stream().map(Message::getFromUid).toList());
        //  消息未读数
        Map<Long, Integer> unreadCountMap = getUnreadMap(uid, sessionIds);
        return sessionBaseInfoMap.values().stream()
                .map(session -> {
                    ChatSessionResp chatSessionResp = new ChatSessionResp();
                    chatSessionResp.setSessionId(session.getSessionId());
                    chatSessionResp.setType(session.getType());
                    chatSessionResp.setHotFlag(session.getHotFlag());
                    chatSessionResp.setName(session.getName());
                    chatSessionResp.setAvatar(session.getAvatar());
                    chatSessionResp.setActiveTime(session.getActiveTime());
                    chatSessionResp.setUnreadCount(unreadCountMap.getOrDefault(session.getSessionId(), 0));

                    Message message = msgMap.get(session.getLastMsgId());
                    if (Objects.nonNull(message)) {
                        AbstractMsgHandler<?> handler = MsgHandlerFactory.getHandler(message.getType());
                        String text = message.getFromUid().equals(User.SYSTEM_UID) ? "" : lastMsgUids.get(message.getFromUid()).getNickname() + ":";
                        chatSessionResp.setText(text + handler.showContactMsg(message));
                    }
                    return chatSessionResp;

                }).sorted(Comparator.comparing(ChatSessionResp::getActiveTime).reversed()).toList();
    }

    private Map<Long, Integer> getUnreadMap(Long uid, List<Long> sessionIds) {
        if (Objects.isNull(uid)) return new HashMap<>();

        List<Contact> contacts = contactDao.getBySessionIds(sessionIds, uid);
        return contacts.parallelStream()
                .map(contact -> Pair.of(contact.getSessionId(), messageDao.getUnreadCount(contact.getSessionId(), contact.getReadTime())))
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    private Map<Long, SessionBaseInfo> getSessionBaseInfoMap(Long uid, List<Long> sessionIds) {
        Map<Long, Session> sessionMap = sessionCache.getBatch(sessionIds);
        Map<Integer, List<Long>> groupTypeMap = sessionMap.values().stream().collect(Collectors.groupingBy(Session::getType,
                Collectors.mapping(Session::getId, Collectors.toList())));
        List<Long> groupSessionIds = groupTypeMap.get(SessionTypeEnum.GROUP.getType());
        Map<Long, SessionGroup> groupMap = sessionGroupCache.getBatch(groupSessionIds);

        List<Long> singleSessionIds = groupTypeMap.get(SessionTypeEnum.SINGLE.getType());
        Map<Long, UserInfoDTO> userInfoDTOMap = getSingleGroupMap(uid, singleSessionIds);

        return sessionMap.values().stream().map(session -> {
            SessionBaseInfo sessionBaseInfo = new SessionBaseInfo();
            sessionBaseInfo.setSessionId(session.getId());
            sessionBaseInfo.setType(session.getType());
            sessionBaseInfo.setHotFlag(session.getHotFlag());
            sessionBaseInfo.setActiveTime(session.getActiveTime());
            sessionBaseInfo.setLastMsgId(session.getLastMsgId());
            if (session.getType().equals(SessionTypeEnum.GROUP.getType())) {
                SessionGroup sessionGroup = groupMap.get(session.getId());
                sessionBaseInfo.setName(sessionGroup.getName());
                sessionBaseInfo.setAvatar(sessionGroup.getAvatar());
            } else if (session.getType().equals(SessionTypeEnum.SINGLE.getType())) {
                UserInfoDTO userInfoDTO = userInfoDTOMap.get(session.getId());
                sessionBaseInfo.setName(userInfoDTO.getNickname());
                sessionBaseInfo.setAvatar(userInfoDTO.getAvatar());
            }
            return sessionBaseInfo;
        }).collect(Collectors.toMap(SessionBaseInfo::getSessionId, Function.identity()));
    }

    private Map<Long, UserInfoDTO> getSingleGroupMap(Long uid, List<Long> singleSessionIds) {
        if (CollUtil.isEmpty(singleSessionIds)) {
            return new HashMap<>();
        }
        Map<Long, SessionSingle> cacheBatch = sessionSingleCache.getBatch(singleSessionIds);
        Set<Long> friendUidSet = cacheBatch.values().stream().map(sessionSingle -> {
            Long uid1 = sessionSingle.getUid1();
            Long uid2 = sessionSingle.getUid2();
            return uid.equals(uid1) ? uid2: uid1;
        }).collect(Collectors.toSet());
        Map<Long, UserInfoDTO> userInfoBatch = userInfoCache.getBatch(new ArrayList<>(friendUidSet));

        return cacheBatch.values().stream()
                .collect(Collectors.toMap(SessionSingle::getSessionId, singleSession -> {
                    Long uid1 = singleSession.getUid1();
                    Long uid2 = singleSession.getUid2();
                    Long friendUid = uid.equals(uid1) ? uid2: uid1;
                    return userInfoBatch.get(friendUid);
                }));
    }
}
