package com.abin.chatserver.chat.controller;

import com.abin.chatserver.chat.domain.vo.req.MemberAddReq;
import com.abin.chatserver.chat.domain.vo.req.MemberExitReq;
import com.abin.chatserver.chat.domain.vo.req.NewGroupReq;
import com.abin.chatserver.chat.domain.vo.resp.ChatMemberListResp;
import com.abin.chatserver.chat.domain.vo.resp.GroupDetailResq;
import com.abin.chatserver.chat.domain.vo.resp.GroupMemberResp;
import com.abin.chatserver.chat.domain.vo.req.MemberDelReq;
import com.abin.chatserver.chat.service.GroupMemberService;
import com.abin.chatserver.chat.service.SessionService;
import com.abin.chatserver.common.domain.vo.BaseResponse;
import com.abin.chatserver.user.domain.entity.User;
import com.abin.chatserver.user.domain.vo.req.IdReq;
import com.abin.chatserver.user.domain.vo.resp.IdResp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/capi/session")
@Tag(name = "群聊相关接口")
@RequiredArgsConstructor
public class GroupController {

    private final SessionService sessionService;

    private final GroupMemberService groupMemberService;

    @GetMapping("/public/group")
    @Operation(summary = "群组详情")
    public BaseResponse<GroupDetailResq> groupDetail(@Valid IdReq req) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return BaseResponse.success(sessionService.getGroupDetail(user.getUid(), req.getId()));
    }

    @GetMapping("/group/member")
    @Operation(summary = "群成员列表")
    public BaseResponse<GroupMemberResp> groupMember(@Valid IdReq req) {
        return BaseResponse.success(sessionService.getGroupMember(req.getId()));
    }

    @GetMapping("/group/member/list")
    @Operation(summary = "群聊内的所有群成员列表-@专用，传sessionId")
    public BaseResponse<List<ChatMemberListResp>> getMemberList(@Valid IdReq req) {
        return BaseResponse.success(sessionService.getMemberList(req.getId()));
    }

    @DeleteMapping("/group/member")
    @Operation(summary = "移除成员")
    public BaseResponse<Void> delMember(@Valid @RequestBody MemberDelReq req) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        sessionService.delMember(user.getUid(), req);
        return BaseResponse.success();
    }

    @DeleteMapping("/group/member/exit")
    @Operation(summary = "退出群聊")
    public BaseResponse<Void> exitGroup(@Valid @RequestBody MemberExitReq req) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        groupMemberService.exitGroup(user.getUid(), req);
        return BaseResponse.success();
    }

    @PostMapping("/group")
    @Operation(summary = "创建群聊，响应群聊id")
    public BaseResponse<IdResp> newGroup(@Valid @RequestBody NewGroupReq req) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long sessionId = sessionService.newGroup(user.getUid(), req.getUids());
        return BaseResponse.success(IdResp.builder().id(sessionId).build());
    }

    @PostMapping("/group/member")
    @Operation(summary = "邀请好友")
    public BaseResponse<Void> addMember(@Valid @RequestBody MemberAddReq req) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        sessionService.addMember(user.getUid(), req);
        return BaseResponse.success();
    }
}
