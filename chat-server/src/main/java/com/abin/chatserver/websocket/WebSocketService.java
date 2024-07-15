package com.abin.chatserver.websocket;

import io.netty.channel.Channel;

public interface WebSocketService {

    void connect(Channel channel);

    void offline(Channel channel);
}
