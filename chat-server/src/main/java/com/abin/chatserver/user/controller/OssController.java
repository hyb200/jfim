package com.abin.chatserver.user.controller;

import com.abin.chatserver.common.domain.vo.BaseResponse;
import com.abin.chatserver.common.domain.vo.response.OssResp;
import com.abin.chatserver.user.domain.entity.User;
import com.abin.chatserver.user.domain.vo.req.UploadUrlReq;
import com.abin.chatserver.user.service.OssService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "文件上传模块接口")
@RequestMapping("/capi/oss")
@RequiredArgsConstructor
public class OssController {

    private final OssService ossService;

    @GetMapping("/upload/url")
    @Operation(summary = "获取临时上传链接")
    public BaseResponse<OssResp> getUploadUrl(@Valid UploadUrlReq req) {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return BaseResponse.success(ossService.getUploadUrl(user.getUid(), req));
    }
}
