package com.abin.chatserver.websocket;

import cn.hutool.extra.spring.SpringUtil;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ChannelHandler.Sharable
public class NettyWebSocketServerHandler extends ChannelInboundHandlerAdapter {

    private WebSocketService webSocketService;

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent event) {
            if (event.state() == IdleState.READER_IDLE) {
                //  心跳丢失，关闭连接
                userOffline(ctx);
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        webSocketService = SpringUtil.getBean(WebSocketService.class);
        webSocketService.connect(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        userOffline(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("WebSocket Service Error: {}", cause.getMessage());
        ctx.channel().close();
    }

    private void userOffline(ChannelHandlerContext ctx) {
        webSocketService.offline(ctx.channel());
        ctx.channel().close();
    }
}
