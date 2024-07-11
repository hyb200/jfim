package com.abin.chatserver.user.service;

import com.abin.chatserver.user.domain.vo.req.AgreeRequestReq;
import com.abin.chatserver.user.domain.vo.req.CursorPageBaseReq;
import com.abin.chatserver.user.domain.vo.req.FriendCheckReq;
import com.abin.chatserver.user.domain.vo.req.FriendRequestReq;
import com.abin.chatserver.user.domain.vo.resp.CursorPageBaseResp;
import com.abin.chatserver.user.domain.vo.resp.FriendCheckResp;
import com.abin.chatserver.user.domain.vo.resp.UserInfoResp;

public interface FriendService {

    CursorPageBaseResp<UserInfoResp> friendList(Long uid, CursorPageBaseReq cursorPageBaseReq);

    FriendCheckResp check(Long uid, FriendCheckReq friendCheckReq);

    void doFriendRequest(Long uid, FriendRequestReq req);

    void agreeRequest(Long uid, AgreeRequestReq agreeRequestReq);
}
