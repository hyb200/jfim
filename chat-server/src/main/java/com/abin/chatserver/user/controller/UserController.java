package com.abin.chatserver.user.controller;

import com.abin.chatserver.common.domain.vo.BaseResponse;
import com.abin.chatserver.user.domain.vo.req.LoginReq;
import com.abin.chatserver.user.domain.vo.req.RegisterReq;
import com.abin.chatserver.user.domain.vo.resp.AuthenticateResp;
import com.abin.chatserver.user.service.AccountService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/capi/user")
@Tag(name = "用户相关接口")
public class UserController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/login")
    public BaseResponse<AuthenticateResp> login(@RequestBody @Valid LoginReq loginReq) {
        String token = accountService.authenticate(loginReq);
        AuthenticateResp resp = AuthenticateResp.builder()
                .token(token)
                .build();
        return BaseResponse.success(resp);
    }

    @PostMapping("/register")
    public BaseResponse<Void> register(@RequestBody @Valid RegisterReq registerReq) {
        String account = registerReq.getUsername();
        String password = registerReq.getPassword();
        String confirmPassword = registerReq.getConfirmPassword();
        accountService.register(account, password, confirmPassword);
        return BaseResponse.success();
    }
}
