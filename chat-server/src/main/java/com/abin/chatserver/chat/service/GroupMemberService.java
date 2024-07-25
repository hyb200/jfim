package com.abin.chatserver.chat.service;

import com.abin.chatserver.chat.domain.vo.req.MemberExitReq;

public interface GroupMemberService {
    void exitGroup(Long uid, MemberExitReq req);
}
