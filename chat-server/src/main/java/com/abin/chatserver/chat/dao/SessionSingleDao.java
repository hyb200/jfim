package com.abin.chatserver.chat.dao;

import com.abin.chatserver.common.domain.enums.CommonStatusEnum;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.abin.chatserver.chat.domain.entity.SessionSingle;
import com.abin.chatserver.chat.mapper.SessionSingleMapper;
import org.springframework.stereotype.Service;

/**
* @author Yibin Huang
* @description 针对表【session_single(单聊表)】的数据库操作
* @createDate 2024-07-17 20:41:16
*/
@Service
public class SessionSingleDao extends ServiceImpl<SessionSingleMapper, SessionSingle> {

    public SessionSingle getBySessionId(Long sessionId) {
        return lambdaQuery().eq(SessionSingle::getSessionId, sessionId).one();
    }

    public SessionSingle getByKey(String key) {
        return lambdaQuery().eq(SessionSingle::getSessionKey, key).one();
    }

    public void renewSession(Long id) {
        lambdaUpdate().eq(SessionSingle::getId, id)
                .set(SessionSingle::getStatus, CommonStatusEnum.NORMAL.getStatus())
                .update();
    }

    public void banSession(String key) {
        lambdaUpdate().eq(SessionSingle::getSessionKey, key)
                .set(SessionSingle::getStatus, CommonStatusEnum.NOT_NORMAL.getStatus())
                .update();
    }
}




