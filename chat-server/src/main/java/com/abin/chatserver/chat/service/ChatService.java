package com.abin.chatserver.chat.service;

import com.abin.chatserver.chat.domain.entity.Message;
import com.abin.chatserver.chat.domain.vo.req.ChatMessagePageReq;
import com.abin.chatserver.chat.domain.vo.req.ChatMessageReq;
import com.abin.chatserver.chat.domain.vo.req.RecallMsgReq;
import com.abin.chatserver.chat.domain.vo.resp.ChatMessageResp;
import com.abin.chatserver.user.domain.vo.resp.CursorPageBaseResp;

public interface ChatService {

    Long sendMsg(Long uid, ChatMessageReq req);

    /**
     * 获取消息给前端展示
     * @param msgId 消息 id
     * @return
     */
    ChatMessageResp getMsgResp(Long msgId);

    /**
     * 获取消息给前端展示
     * @param msg 消息
     * @return
     */
    ChatMessageResp getMsgResp(Message msg);

    void recallMsg(Long uid, RecallMsgReq req);

    CursorPageBaseResp<ChatMessageResp> getMsgPage(Long receiverUid, ChatMessagePageReq req);
}
