package com.abin.chatserver.chat.service;

import com.abin.chatserver.chat.domain.entity.SessionSingle;
import com.abin.chatserver.chat.domain.vo.resp.GroupMemberResp;
import com.abin.chatserver.chat.domain.vo.req.MemberDelReq;

import java.util.List;

public interface SessionService {

    SessionSingle newSingleSession(List<Long> uids);

    /**
     * 禁用单聊
     */
    void banSingleSession(List<Long> uids);

    SessionSingle getSingleSession(Long uid, Long friendUid);

    GroupMemberResp getGroupMember(Long sessionId);

    void delMember(Long uid, MemberDelReq req);
}
