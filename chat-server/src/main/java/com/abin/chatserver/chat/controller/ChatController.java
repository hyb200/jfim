package com.abin.chatserver.chat.controller;

import com.abin.chatserver.chat.domain.vo.req.ChatMessageReq;
import com.abin.chatserver.chat.domain.vo.req.RecallMsgReq;
import com.abin.chatserver.chat.domain.vo.resp.ChatMessageResp;
import com.abin.chatserver.chat.service.ChatService;
import com.abin.chatserver.common.domain.vo.BaseResponse;
import com.abin.chatserver.user.domain.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/capi/chat")
@Tag(name = "聊天相关接口")
@Slf4j
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/msg")
    @Operation(description = "发送消息")
    public BaseResponse<ChatMessageResp> sendMsg(@Valid @RequestBody ChatMessageReq req) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long msgId = chatService.sendMsg(user.getUid(), req);
        return BaseResponse.success(chatService.getMsgResp(user.getUid(), msgId));
    }

    @PutMapping("/msg/recall")
    @Operation(description = "消息撤回")
    public BaseResponse<Void> msgRecall(@Valid @RequestBody RecallMsgReq req) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        chatService.recallMsg(user.getUid(), req);
        return BaseResponse.success();
    }
}
