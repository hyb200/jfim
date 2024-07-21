package com.abin.chatserver.websocket;

import com.abin.chatserver.common.config.ThreadPoolConfig;
import com.abin.chatserver.common.utils.JsonUtils;
import com.abin.chatserver.user.domain.dto.WSChannelExtraDTO;
import com.abin.chatserver.user.domain.vo.resp.WSBaseResp;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class WebSocketServiceImpl implements WebSocketService {

    @Autowired
    @Qualifier(ThreadPoolConfig.WS_EXECUTOR)
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    /**
     * uid -> channel
     */
    private static final ConcurrentHashMap<Long, Channel> ONLINE_UID_MAP = new ConcurrentHashMap<>();

    /**
     * 已连接的 websocket 连接和相关参数
     */
    private static final ConcurrentHashMap<Channel, WSChannelExtraDTO> ONLINE_WS_MAP = new ConcurrentHashMap<>();

    @Override
    public void connect(Channel channel) {
        Long uid = NettyUtils.getAttr(channel, NettyUtils.UID);
        ONLINE_WS_MAP.put(channel, new WSChannelExtraDTO(uid));
        ONLINE_UID_MAP.put(uid, channel);
    }

    @Override
    public void offline(Channel channel) {
        Long uid = NettyUtils.getAttr(channel, NettyUtils.UID);
        ONLINE_UID_MAP.remove(uid);
        ONLINE_WS_MAP.remove(channel);
    }

    @Override
    public void sendToUid(WSBaseResp<?> msg, Long uid) {
        Channel channel = ONLINE_UID_MAP.get(uid);
        if (Objects.isNull(channel)) {
            return;
        }
        threadPoolTaskExecutor.execute(() -> sendMsg(channel, msg));
    }

    private void sendMsg(Channel channel, WSBaseResp<?> msg) {
        channel.writeAndFlush(new TextWebSocketFrame(JsonUtils.toStr(msg)));
    }

    @Override
    public void sendToAllOnline(WSBaseResp<?> msg) {
        sendToAllOnline(msg, null);
    }

    @Override
    public void sendToAllOnline(WSBaseResp<?> msg, Long skipUid) {
        ONLINE_WS_MAP.forEach((channel, extra) -> {
            if (Objects.nonNull(skipUid) && Objects.equals(skipUid, extra.getUid())) {
                return;
            }
            threadPoolTaskExecutor.execute(() -> sendMsg(channel, msg));
        });
    }
}
