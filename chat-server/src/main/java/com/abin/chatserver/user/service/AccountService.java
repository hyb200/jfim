package com.abin.chatserver.user.service;

import com.abin.chatserver.user.domain.vo.req.LoginReq;

public interface AccountService {

    String authenticate(LoginReq loginReq);

    void register(String account, String password, String confirmPassword);
}
