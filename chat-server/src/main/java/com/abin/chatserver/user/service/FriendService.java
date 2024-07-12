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

    /**
     * 检查是否是自己的好友
     * @param uid
     * @param friendCheckReq
     * @return
     */
    FriendCheckResp check(Long uid, FriendCheckReq friendCheckReq);

    void doFriendRequest(Long uid, FriendRequestReq req);

    void agreeRequest(Long uid, AgreeRequestReq agreeRequestReq);

    void deleteFriend(Long uid, Long targetUid);

}
