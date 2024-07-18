package com.abin.chatserver.chat.service;

import com.abin.chatserver.chat.domain.entity.Message;
import com.abin.chatserver.chat.domain.vo.req.ChatMessageReq;
import com.abin.chatserver.chat.domain.vo.resp.ChatMessageResp;

public interface ChatService {

    Long sendMsg(Long uid, ChatMessageReq req);

    /**
     * 获取消息给前端展示
     * @param receiver  接收者 uid，可为 null
     * @param msgId 消息 id
     * @return
     */
    ChatMessageResp getMsgResp(Long receiver, Long msgId);

    /**
     * 获取消息给前端展示
     * @param receiver  接收者 uid，可为 null
     * @param msg 消息
     * @return
     */
    ChatMessageResp getMsgResp(Long receiver, Message msg);
}
