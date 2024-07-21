package com.abin.chatserver.chat.service;

import com.abin.chatserver.chat.domain.entity.SessionSingle;

import java.util.List;

public interface SessionService {

    SessionSingle newSingleSession(List<Long> uids);

    /**
     * 禁用单聊
     */
    void banSingleSession(List<Long> uids);
}
