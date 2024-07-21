package com.abin.chatserver.chat.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.abin.chatserver.chat.domain.entity.Session;
import com.abin.chatserver.chat.mapper.SessionMapper;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
* @author Yibin Huang
* @description 针对表【session(会话表)】的数据库操作
* @createDate 2024-07-17 20:41:10
*/
@Service
public class SessionDao extends ServiceImpl<SessionMapper, Session>  {

    public Session getBySessionId(Long sessionId) {
        return lambdaQuery().eq(Session::getId, sessionId).one();
    }

    public void refreshActiveTime(Long sessionId, Long msgId, Date createTime) {
        lambdaUpdate().eq(Session::getId, sessionId)
                .set(Session::getLastMsgId, msgId)
                .set(Session::getActiveTime, createTime)
                .update();
    }
}




