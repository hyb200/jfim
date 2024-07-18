package com.abin.chatserver.chat.service;

import com.abin.chatserver.chat.domain.entity.Session;
import com.abin.chatserver.chat.domain.entity.SessionSingle;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Yibin Huang
* @description 针对表【session(会话表)】的数据库操作Service
* @createDate 2024-07-17 20:41:10
*/
public interface SessionService {

    SessionSingle newSingleSession(List<Long> uids);
}
