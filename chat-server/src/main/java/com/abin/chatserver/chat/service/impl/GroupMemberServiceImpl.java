package com.abin.chatserver.chat.service.impl;

import com.abin.chatserver.chat.dao.*;
import com.abin.chatserver.chat.domain.entity.GroupMember;
import com.abin.chatserver.chat.domain.entity.SessionGroup;
import com.abin.chatserver.chat.domain.vo.req.MemberExitReq;
import com.abin.chatserver.chat.service.GroupMemberService;
import com.abin.chatserver.chat.service.cache.GroupMemberCache;
import com.abin.chatserver.common.domain.enums.WSResqTypeEnum;
import com.abin.chatserver.common.domain.vo.response.WSBaseResp;
import com.abin.chatserver.common.domain.vo.response.WSMemberChange;
import com.abin.chatserver.common.exception.BusinessException;
import com.abin.chatserver.user.service.impl.PushMsgService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class GroupMemberServiceImpl implements GroupMemberService {

    private final SessionGroupDao sessionGroupDao;
    private final GroupMemberDao groupMemberDao;
    private final ContactDao contactDao;
    private final GroupMemberCache groupMemberCache;
    private final PushMsgService pushMsgService;
    private final SessionDao sessionDao;
    private final MessageDao messageDao;

    @Override
    public void exitGroup(Long uid, MemberExitReq req) {
        Long sessionId = req.getSessionId();
        //  群聊是否存在
        SessionGroup sessionGroup = sessionGroupDao.getBySessionId(sessionId);
        if (Objects.isNull(sessionGroup)) {
            throw new BusinessException("群聊不存在");
        }
        //  是否是群成员
        GroupMember member = groupMemberDao.getMember(sessionGroup.getId(), uid);
        if (Objects.isNull(member)) {
            throw new BusinessException("不在群聊中");
        }
        boolean isManager = groupMemberDao.isManager(sessionGroup.getId(), uid);
        if (!isManager) {
            //  删除会话
            contactDao.removeBySessionId(sessionId, Collections.singletonList(uid));
            //  删除群成员
            groupMemberDao.removeByGroupId(sessionGroup.getId(), Collections.singletonList(uid));
            //  通知群成员
            List<Long> memberUids = groupMemberCache.getMemberUids(sessionGroup.getSessionId());
            WSBaseResp<WSMemberChange> wsBaseResp = new WSBaseResp<>();
            wsBaseResp.setType(WSResqTypeEnum.MEMBER_CHANGE.getType());
            WSMemberChange memberChange = WSMemberChange.builder()
                    .uid(uid)
                    .changeType(WSMemberChange.CHANGE_TYPE_REMOVE)
                    .sessionId(sessionGroup.getSessionId())
                    .build();
            wsBaseResp.setData(memberChange);
            pushMsgService.sendPushMsg(wsBaseResp, memberUids);
            groupMemberCache.evictMemberUids(sessionGroup.getId());
        } else {
            //  管理员退出直接解散群聊
            sessionDao.removeById(sessionId);
            contactDao.removeBySessionId(sessionId, Collections.EMPTY_LIST);
            groupMemberDao.removeByGroupId(sessionGroup.getId(), Collections.EMPTY_LIST);
            messageDao.removeBySessionId(sessionId);
        }


    }
}
