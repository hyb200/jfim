package com.abin.chatserver.user.service.impl;

import cn.hutool.core.util.IdUtil;
import com.abin.chatserver.common.annotation.RedissonLock;
import com.abin.chatserver.common.domain.enums.ErrorEnum;
import com.abin.chatserver.common.exception.BusinessException;
import com.abin.chatserver.common.utils.JwtUtils;
import com.abin.chatserver.user.dao.UserDao;
import com.abin.chatserver.user.domain.entity.User;
import com.abin.chatserver.user.domain.vo.req.LoginReq;
import com.abin.chatserver.user.domain.vo.req.ModifyNameReq;
import com.abin.chatserver.user.service.AccountService;
import com.abin.chatserver.user.service.cache.UserCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserCache userCache;

    @Override
    public void register(String username, String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new BusinessException(ErrorEnum.BUSINESS_ERROR.getCode(), "两次密码输入不一致");
        }
        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .nickname("用户" + IdUtil.fastSimpleUUID().substring(0, 8))
                .build();
        userDao.save(user);
    }

    @Override
    @RedissonLock(key = "#uid")
    public void modifyName(Long uid, ModifyNameReq req) {
        String name = req.getName();
        userDao.modifyName(uid, name);
        userCache.userInfoChange(uid);
    }

    @Override
    public String authenticate(LoginReq loginReq) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginReq.getUsername(), loginReq.getPassword()));
        User user = (User) authenticate.getPrincipal();
        return JwtUtils.generateToken(user.getUid());
    }

}
