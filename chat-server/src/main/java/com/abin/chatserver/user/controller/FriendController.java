package com.abin.chatserver.user.controller;

import com.abin.chatserver.common.domain.vo.BaseResponse;
import com.abin.chatserver.user.domain.entity.User;
import com.abin.chatserver.user.domain.vo.req.*;
import com.abin.chatserver.user.domain.vo.resp.CursorPageBaseResp;
import com.abin.chatserver.user.domain.vo.resp.FriendCheckResp;
import com.abin.chatserver.user.domain.vo.resp.UserInfoResp;
import com.abin.chatserver.user.service.FriendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/capi/user/friend")
@Tag(name = "好友模块相关接口")
public class FriendController {

    @Autowired
    private FriendService friendService;

    @PostMapping("/check")
    @Operation(description = "查询是否是好友")
    public BaseResponse<FriendCheckResp> check(@Valid @RequestBody FriendCheckReq friendCheckReq) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return BaseResponse.success(friendService.check(user.getUid(), friendCheckReq));
    }

    @PostMapping("/request")
    @Operation(description = "提交好友申请")
    public BaseResponse<Void> request(@Valid @RequestBody FriendRequestReq req) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        friendService.doFriendRequest(user.getUid(), req);
        return BaseResponse.success();
    }

    @PutMapping("/agree")
    @Operation(description = "同意申请")
    public BaseResponse<Void> agreeRequest(@Valid @RequestBody AgreeRequestReq req) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        friendService.agreeRequest(user.getUid(), req);
        return BaseResponse.success();
    }

    @DeleteMapping
    @Operation(description = "删除好友")
    public BaseResponse<Void> deleteFriend(@Valid @RequestBody FriendDeleteReq req) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        friendService.deleteFriend(user.getUid(), req.getTargetUid());
        return BaseResponse.success();
    }

    @GetMapping("/list")
    public BaseResponse<CursorPageBaseResp<UserInfoResp>> friendList(@Valid CursorPageBaseReq cursorPageBaseReq) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return BaseResponse.success(friendService.friendList(user.getUid(), cursorPageBaseReq));
    }
}
