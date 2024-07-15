package com.abin.chatserver.websocket;

import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.core.util.StrUtil;
import com.abin.chatserver.common.utils.JwtUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.Optional;

@Slf4j
public class HttpHeaderHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            UrlBuilder urlBuilder = UrlBuilder.ofHttp(request.uri());
            String token = Optional.ofNullable(urlBuilder.getQuery())
                    .map(k -> k.get("token"))
                    .map(CharSequence::toString)
                    .orElse("");

            if (StrUtil.isBlank(token)) {
                ctx.channel().close();
                return;
            }
            //  todo token 过期处理逻辑
            Long uid = JwtUtils.extractUidOrNull(token);

            if (Objects.nonNull(uid)) {
                NettyUtils.setAttr(ctx.channel(), NettyUtils.UID, uid);
                request.setUri(urlBuilder.getPathStr());
            }

            ctx.pipeline().remove(this);
        }
        ctx.fireChannelActive();
        super.channelRead(ctx, msg);
    }
}
