package com.abin.chatserver.chat.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.abin.chatserver.chat.dao.GroupMemberDao;
import com.abin.chatserver.chat.dao.SessionDao;
import com.abin.chatserver.chat.dao.SessionSingleDao;
import com.abin.chatserver.chat.domain.entity.GroupMember;
import com.abin.chatserver.chat.domain.entity.Session;
import com.abin.chatserver.chat.domain.entity.SessionGroup;
import com.abin.chatserver.chat.domain.entity.SessionSingle;
import com.abin.chatserver.chat.domain.enums.GroupRoleEnum;
import com.abin.chatserver.chat.domain.enums.HotFlagEnum;
import com.abin.chatserver.chat.domain.enums.SessionTypeEnum;
import com.abin.chatserver.chat.domain.vo.resp.GroupMemberResp;
import com.abin.chatserver.chat.domain.vo.req.MemberDelReq;
import com.abin.chatserver.chat.service.SessionService;
import com.abin.chatserver.chat.service.cache.GroupMemberCache;
import com.abin.chatserver.chat.service.cache.SessionCache;
import com.abin.chatserver.chat.service.cache.SessionGroupCache;
import com.abin.chatserver.common.domain.enums.CommonStatusEnum;
import com.abin.chatserver.common.domain.enums.WSResqTypeEnum;
import com.abin.chatserver.common.domain.vo.response.WSBaseResp;
import com.abin.chatserver.common.domain.vo.response.WSMemberChange;
import com.abin.chatserver.common.exception.BusinessException;
import com.abin.chatserver.user.service.impl.PushMsgService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final SessionSingleDao sessionSingleDao;

    private final SessionDao sessionDao;

    private final SessionCache sessionCache;

    private final SessionGroupCache sessionGroupCache;

    private final GroupMemberDao groupMemberDao;
    private final GroupMemberCache groupMemberCache;
    private final PushMsgService pushMsgService;

    @Override
    public SessionSingle newSingleSession(List<Long> uids) {
        if (CollUtil.isEmpty(uids) || uids.size() != 2) {
            throw new BusinessException("会话创建失败");
        }
        String key = generateSessionKey(uids);
        SessionSingle sessionSingle = sessionSingleDao.getByKey(key);
        if (Objects.nonNull(sessionSingle)) {
            //  已经存在就恢复
            if (sessionSingle.getStatus().equals(CommonStatusEnum.NOT_NORMAL.getStatus())) {
                sessionSingleDao.renewSession(sessionSingle.getId());
            }
        } else {//  新建会话
            Session session = createSession(SessionTypeEnum.SINGLE);
            sessionSingle = createSingleSession(session.getId(), uids);
        }
        return sessionSingle;
    }

    @Override
    public void banSingleSession(List<Long> uids) {
        if (uids.size() != 2) {
            throw new BusinessException("禁用会话失败，参数错误");
        }
        String key = generateSessionKey(uids);
        sessionSingleDao.banSession(key);
    }

    @Override
    public SessionSingle getSingleSession(Long uid, Long friendUid) {
        String key = generateSessionKey(Arrays.asList(uid, friendUid));
        return sessionSingleDao.getByKey(key);
    }

    @Override
    public GroupMemberResp getGroupMember(Long sessionId) {
        Session session = sessionCache.get(sessionId);
        if (Objects.isNull(session)) {
            throw new BusinessException("sessionId错误");
        }

        List<Long> members, managers;
        if (session.isHotSession()) {
            members = null;
            managers = null;
        } else {
            SessionGroup sessionGroup = sessionGroupCache.get(sessionId);
            List<GroupMember> groupMembers = groupMemberDao.getMembers(sessionGroup.getId());
            members = groupMembers.stream()
                    .map(GroupMember::getUid)
                    .toList();
            managers = groupMembers.stream()
                    .filter(groupMember -> groupMember.getRole().equals(GroupRoleEnum.MANAGER.getType()))
                    .map(GroupMember::getUid)
                    .toList();
        }
        return GroupMemberResp.builder()
                .uids(members)
                .managerUids(managers)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delMember(Long uid, MemberDelReq req) {
        Long sessionId = req.getSessionId();
        Long delUid = req.getUid();

        Session session = sessionCache.get(sessionId);
        if (Objects.isNull(session)) {
            throw new BusinessException("sessionId错误");
        }
        SessionGroup sessionGroup = sessionGroupCache.get(sessionId);
        GroupMember delMember = groupMemberDao.getMember(sessionGroup.getId(), delUid);
        if (Objects.isNull(delMember)) {
            throw new BusinessException("待删除用户不在群组内");
        }
        boolean hasPower = groupMemberDao.isManager(sessionGroup.getId(), uid);
        if (!hasPower) {
            throw new BusinessException("您无权限移除用户");
        }
        groupMemberDao.removeById(delMember.getId());
        //  发送消息通知群成员
        List<Long> memberUids = groupMemberCache.getMemberUids(sessionGroup.getSessionId());

        WSBaseResp<WSMemberChange> wsBaseResp = new WSBaseResp<>();

        wsBaseResp.setType(WSResqTypeEnum.MEMBER_CHANGE.getType());
        WSMemberChange wsMemberChange = WSMemberChange.builder()
                .sessionId(sessionGroup.getSessionId())
                .uid(delMember.getUid())
                .changeType(WSMemberChange.CHANGE_TYPE_REMOVE)
                .build();
        wsBaseResp.setData(wsMemberChange);
        pushMsgService.sendPushMsg(wsBaseResp, memberUids);
        groupMemberCache.evictMemberUids(sessionGroup.getSessionId());
    }

    private SessionSingle createSingleSession(Long sessionId, List<Long> uids) {
        uids = uids.stream().sorted().toList();
        SessionSingle insert = SessionSingle.builder()
                .sessionId(sessionId)
                .sessionKey(generateSessionKey(uids))
                .uid1(uids.get(0))
                .uid2(uids.get(1))
                .status(CommonStatusEnum.NORMAL.getStatus())
                .build();
        sessionSingleDao.save(insert);
        return insert;
    }

    private Session createSession(SessionTypeEnum sessionTypeEnum) {
        Session insert = Session.builder()
                .type(sessionTypeEnum.getType())
                .hotFlag(HotFlagEnum.NOT.getType())
                .build();
        sessionDao.save(insert);
        return insert;
    }

    private String generateSessionKey(List<Long> uids) {
        return uids.stream()
                .sorted()
                .map(String::valueOf)
                .collect(Collectors.joining("#"));
    }
}
