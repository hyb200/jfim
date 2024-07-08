package com.abin.chatserver.websocket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ChannelHandler.Sharable
public class NettyWebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame msg) {
        String message = msg.text();
        System.out.println(message);
        Channel channel = channelHandlerContext.channel();
        channel.writeAndFlush(new TextWebSocketFrame("this is websocket, i got your message"));
    }
}
