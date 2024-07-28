package com.abin.chatserver.chat.service;

import com.abin.chatserver.chat.domain.entity.SessionSingle;
import com.abin.chatserver.chat.domain.vo.req.MemberAddReq;
import com.abin.chatserver.chat.domain.vo.resp.ChatMemberListResp;
import com.abin.chatserver.chat.domain.vo.resp.GroupDetailResq;
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

    /**
     * 新建群聊
     * @param owner 群主uid
     * @param uids  邀请的成员uid
     * @return  群聊id
     */
    Long newGroup(Long owner, List<Long> uids);

    void addMember(Long inviter, MemberAddReq req);

    GroupDetailResq getGroupDetail(Long uid, Long sessionId);

    List<ChatMemberListResp> getMemberList(Long sessionId);
}
