package com.abin.chatserver.chat.controller;

import com.abin.chatserver.chat.domain.vo.resp.ChatSessionResp;
import com.abin.chatserver.chat.service.ContactService;
import com.abin.chatserver.common.domain.vo.BaseResponse;
import com.abin.chatserver.user.domain.entity.User;
import com.abin.chatserver.user.domain.vo.req.CursorPageBaseReq;
import com.abin.chatserver.user.domain.vo.req.IdReq;
import com.abin.chatserver.user.domain.vo.resp.CursorPageBaseResp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/capi/chat")
@Tag(name = "会话模块接口")
@Slf4j
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @GetMapping("/contact/page")
    @Operation(summary = "分页获取会话列表")
    public BaseResponse<CursorPageBaseResp<ChatSessionResp>> getSessionPage(@Valid CursorPageBaseReq req) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return BaseResponse.success(contactService.getContactPage(user.getUid(), req));
    }

    @GetMapping("/contact/detail")
    @Operation(summary = "会话详情（会话列表点击）")
    public BaseResponse<ChatSessionResp> getContactDetail(@Valid IdReq req) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return BaseResponse.success(contactService.getContactDetail(user.getUid(), req.getId()));
    }

    @GetMapping("/contact/detail/friend")
    @Operation(summary = "会话详情（好友列表点击发消息，参数为好友uid）")
    public BaseResponse<ChatSessionResp> getContactDetailByFriend(@Valid IdReq req) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return BaseResponse.success(contactService.getContactDetailByFriend(user.getUid(), req.getId()));
    }
}
