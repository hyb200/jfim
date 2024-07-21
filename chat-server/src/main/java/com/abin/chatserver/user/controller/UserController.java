package com.abin.chatserver.user.controller;

import com.abin.chatserver.common.annotation.RedissonLock;
import com.abin.chatserver.common.domain.vo.BaseResponse;
import com.abin.chatserver.user.domain.dto.SummaryInfoDTO;
import com.abin.chatserver.user.domain.entity.User;
import com.abin.chatserver.user.domain.vo.req.LoginReq;
import com.abin.chatserver.user.domain.vo.req.ModifyNameReq;
import com.abin.chatserver.user.domain.vo.req.RegisterReq;
import com.abin.chatserver.user.domain.vo.req.SummaryInfoReq;
import com.abin.chatserver.user.domain.vo.resp.AuthenticateResp;
import com.abin.chatserver.user.domain.vo.resp.UserInfoResp;
import com.abin.chatserver.user.service.AccountService;
import com.abin.chatserver.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/capi/user")
@Tag(name = "用户相关接口")
public class UserController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;

    @GetMapping("/info")
    @Operation(summary = "获取用户信息")
    public BaseResponse<UserInfoResp> getUserInfo() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return BaseResponse.success(userService.getUserInfo(user.getUid()));
    }

    @PostMapping("/summary/info/batch")
    @Operation(summary = "返回需要刷新的信息")
    public BaseResponse<List<SummaryInfoDTO>> getSummaryUserInfo(@Valid @RequestBody SummaryInfoReq req) {
        return BaseResponse.success(userService.getSummaryUserInfo(req));
    }

    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public BaseResponse<AuthenticateResp> login(@RequestBody @Valid LoginReq loginReq) {
        String token = accountService.authenticate(loginReq);
        AuthenticateResp resp = AuthenticateResp.builder()
                .token(token)
                .build();
        return BaseResponse.success(resp);
    }

    @PostMapping("/register")
    @Operation(summary = "用户注册")
    public BaseResponse<Void> register(@RequestBody @Valid RegisterReq registerReq) {
        String account = registerReq.getUsername();
        String password = registerReq.getPassword();
        String confirmPassword = registerReq.getConfirmPassword();
        accountService.register(account, password, confirmPassword);
        return BaseResponse.success();
    }

    @PutMapping("/name")
    @Operation(summary = "更改用户名")
    public BaseResponse<Void> modifyName(@Valid @RequestBody ModifyNameReq req) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        accountService.modifyName(user.getUid(), req);
        return BaseResponse.success();
    }
}
