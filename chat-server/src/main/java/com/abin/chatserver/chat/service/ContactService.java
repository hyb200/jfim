package com.abin.chatserver.chat.service;

import com.abin.chatserver.chat.domain.vo.resp.ChatSessionResp;
import com.abin.chatserver.user.domain.vo.req.CursorPageBaseReq;
import com.abin.chatserver.user.domain.vo.resp.CursorPageBaseResp;

public interface ContactService {

    /**
     * 分页获取会话列表（已排序）
     * @param uid   用户 uid
     * @param req   分页请求
     * @return  会话列表
     */
    CursorPageBaseResp<ChatSessionResp> getContactPage(Long uid, CursorPageBaseReq req);

    ChatSessionResp getContactDetail(Long uid, Long sessionId);

    ChatSessionResp getContactDetailByFriend(Long uid, Long friendUid);
}
