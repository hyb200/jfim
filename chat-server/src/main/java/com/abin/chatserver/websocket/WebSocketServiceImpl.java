package com.abin.chatserver.websocket;

import io.netty.channel.Channel;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class WebSocketServiceImpl implements WebSocketService {

    /**
     * uid -> channel
     */
    private static final ConcurrentHashMap<Long, Channel> ONLINE_UID_MAP = new ConcurrentHashMap<>();

    @Override
    public void connect(Channel channel) {
        Long uid = NettyUtils.getAttr(channel, NettyUtils.UID);
        ONLINE_UID_MAP.put(uid, channel);
    }

    @Override
    public void offline(Channel channel) {
        Long uid = NettyUtils.getAttr(channel, NettyUtils.UID);
        ONLINE_UID_MAP.remove(uid);
    }
}
