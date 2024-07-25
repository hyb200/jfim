package com.abin.chatserver.user.controller;

import com.abin.chatserver.common.domain.vo.BaseResponse;
import com.abin.chatserver.user.domain.entity.User;
import com.abin.chatserver.user.domain.vo.req.*;
import com.abin.chatserver.user.domain.vo.resp.*;
import com.abin.chatserver.user.service.FriendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/capi/user/friend")
@Tag(name = "好友模块接口")
public class FriendController {

    @Autowired
    private FriendService friendService;

    @PostMapping("/check")
    @Operation(summary = "查询是否是好友")
    public BaseResponse<FriendCheckResp> check(@Valid @RequestBody FriendCheckReq friendCheckReq) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return BaseResponse.success(friendService.check(user.getUid(), friendCheckReq));
    }

    @PostMapping("/request")
    @Operation(summary = "提交好友申请")
    public BaseResponse<Void> request(@Valid @RequestBody FriendRequestReq req) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        friendService.doFriendRequest(user.getUid(), req);
        return BaseResponse.success();
    }

    @PutMapping("/agree")
    @Operation(summary = "同意申请")
    public BaseResponse<Void> agreeRequest(@Valid @RequestBody AgreeRequestReq req) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        friendService.agreeRequest(user.getUid(), req);
        return BaseResponse.success();
    }

    @DeleteMapping
    @Operation(summary = "删除好友")
    public BaseResponse<Void> deleteFriend(@Valid @RequestBody FriendDeleteReq req) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        friendService.deleteFriend(user.getUid(), req.getTargetUid());
        return BaseResponse.success();
    }

    @GetMapping("/list")
    @Operation(summary = "分页获取好友列表")
    public BaseResponse<CursorPageBaseResp<UserInfoResp>> friendList(@Valid CursorPageBaseReq cursorPageBaseReq) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return BaseResponse.success(friendService.friendList(user.getUid(), cursorPageBaseReq));
    }

    @GetMapping("/request/list")
    @Operation(summary = "分页获取申请列表")
    public BaseResponse<PageBaseResp<FriendRequestResp>> requestList(@Valid PageBaseReq req) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return BaseResponse.success(friendService.requestList(user.getUid(), req));
    }

    @GetMapping("/request/unread")
    @Operation(summary = "申请未读数")
    public BaseResponse<RequestUnreadResp> unread() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return BaseResponse.success(friendService.unread(user.getUid()));
    }
}
