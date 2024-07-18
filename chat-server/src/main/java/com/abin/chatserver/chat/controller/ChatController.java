package com.abin.chatserver.chat.controller;

import com.abin.chatserver.chat.domain.vo.req.ChatMessageReq;
import com.abin.chatserver.chat.domain.vo.resp.ChatMessageResp;
import com.abin.chatserver.chat.service.ChatService;
import com.abin.chatserver.common.domain.vo.BaseResponse;
import com.abin.chatserver.user.domain.entity.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/capi/chat")
@Tag(name = "聊天相关接口")
@Slf4j
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/send")
    public BaseResponse<ChatMessageResp> sendMsg(@Valid @RequestBody ChatMessageReq req) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long msgId = chatService.sendMsg(user.getUid(), req);
        return BaseResponse.success(chatService.getMsgResp(user.getUid(), msgId));
    }
}
