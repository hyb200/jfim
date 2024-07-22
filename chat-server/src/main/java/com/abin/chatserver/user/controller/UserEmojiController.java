package com.abin.chatserver.user.controller;

import com.abin.chatserver.common.domain.vo.BaseResponse;
import com.abin.chatserver.user.domain.entity.User;
import com.abin.chatserver.user.domain.vo.req.AddEmojiReq;
import com.abin.chatserver.user.domain.vo.req.IdReq;
import com.abin.chatserver.user.domain.vo.resp.UserEmojiResp;
import com.abin.chatserver.user.service.UserEmojiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/capi/user/emoji")
@Tag(name = "表情包模块接口")
@RequiredArgsConstructor
public class UserEmojiController {

    private final UserEmojiService userEmojiService;

    @GetMapping("/list")
    @Operation(summary = "获取所有表情包")
    public BaseResponse<List<UserEmojiResp>> getEmojiList() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return BaseResponse.success(userEmojiService.getEmojiList(user.getUid()));
    }

    @PostMapping
    @Operation(summary = "添加表情包")
    public BaseResponse<Long> addEmoji(@Valid @RequestBody AddEmojiReq req) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return BaseResponse.success(userEmojiService.addEmoji(user.getUid(), req));
    }

    @DeleteMapping
    @Operation(summary = "删除表情包")
    public BaseResponse<Void> deleteEmoji(@Valid @RequestBody IdReq req) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userEmojiService.delEmoji(user.getUid(), req);
        return BaseResponse.success();
    }
}
