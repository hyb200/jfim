package com.abin.chatserver.websocket;

import io.netty.channel.Channel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;


public class NettyUtils {

    public static final AttributeKey<String> TOKEN = AttributeKey.valueOf("token");
    public static final AttributeKey<Long> UID = AttributeKey.valueOf("uid");

    public static <T> void setAttr(Channel channel, AttributeKey<T> key, T value) {
        Attribute<T> attr = channel.attr(key);
        attr.set(value);
    }

    public static <T> T getAttr(Channel channel, AttributeKey<T> key) {
        Attribute<T> attr = channel.attr(key);
        return attr.get();
    }
}
