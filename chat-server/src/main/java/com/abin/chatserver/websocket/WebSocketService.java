package com.abin.chatserver.websocket;

import com.abin.chatserver.user.domain.vo.resp.WSBaseResp;
import io.netty.channel.Channel;

public interface WebSocketService {

    void connect(Channel channel);

    void offline(Channel channel);

    void sendToUid(WSBaseResp<?> msg, Long uid);

    void sendToAllOnline(WSBaseResp<?> msg);

    void sendToAllOnline(WSBaseResp<?> msg, Long skipUid);
}
