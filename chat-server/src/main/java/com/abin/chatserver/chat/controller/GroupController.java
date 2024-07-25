package com.abin.chatserver.chat.controller;

import com.abin.chatserver.chat.domain.vo.req.MemberExitReq;
import com.abin.chatserver.chat.domain.vo.resp.GroupMemberResp;
import com.abin.chatserver.chat.domain.vo.req.MemberDelReq;
import com.abin.chatserver.chat.service.GroupMemberService;
import com.abin.chatserver.chat.service.SessionService;
import com.abin.chatserver.common.domain.vo.BaseResponse;
import com.abin.chatserver.user.domain.entity.User;
import com.abin.chatserver.user.domain.vo.req.IdReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/capi/session")
@Tag(name = "群聊相关接口")
@RequiredArgsConstructor
public class GroupController {

    private final SessionService sessionService;

    private final GroupMemberService groupMemberService;

    @GetMapping("/group/member")
    @Operation(summary = "群成员列表")
    public BaseResponse<GroupMemberResp> groupMember(@Valid IdReq req) {
        return BaseResponse.success(sessionService.getGroupMember(req.getId()));
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
}
