package com.abin.chatserver.user.service;

import com.abin.chatserver.user.domain.vo.req.*;
import com.abin.chatserver.user.domain.vo.resp.*;

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

    RequestUnreadResp unread(Long uid);

    PageBaseResp<FriendRequestResp> requestList(Long uid, PageBaseReq req);
}
