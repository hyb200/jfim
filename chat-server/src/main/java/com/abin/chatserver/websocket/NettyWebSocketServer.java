//package com.abin.chatserver.websocket;
//
//import io.netty.bootstrap.ServerBootstrap;
//import io.netty.channel.ChannelInitializer;
//import io.netty.channel.ChannelOption;
//import io.netty.channel.ChannelPipeline;
//import io.netty.channel.EventLoopGroup;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.SocketChannel;
//import io.netty.channel.socket.nio.NioServerSocketChannel;
//import io.netty.handler.codec.http.HttpObjectAggregator;
//import io.netty.handler.codec.http.HttpServerCodec;
//import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
//import io.netty.handler.logging.LogLevel;
//import io.netty.handler.logging.LoggingHandler;
//import io.netty.handler.stream.ChunkedWriteHandler;
//import io.netty.util.NettyRuntime;
//import jakarta.annotation.PostConstruct;
//import jakarta.annotation.PreDestroy;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@Slf4j
//public class NettyWebSocketServer {
//    public static final int WEB_SOCKET_PORT = 10086;
//
//    private final EventLoopGroup bossGroup = new NioEventLoopGroup(1);
//    private final EventLoopGroup workerGroup = new NioEventLoopGroup(NettyRuntime.availableProcessors());
//
//    /**
//     * 优雅停机
//     */
//    @PreDestroy
//    public void destory() {
//        bossGroup.shutdownGracefully().syncUninterruptibly();
//        workerGroup.shutdownGracefully().syncUninterruptibly();
//        log.info("WebSocket Server is closed");
//    }
//
//    /**
//     * 启动 WebSocket
//     */
//    @PostConstruct
//    public void start() throws InterruptedException {
//        ServerBootstrap server = new ServerBootstrap();
//        server.group(bossGroup, workerGroup)
//                .channel(NioServerSocketChannel.class)
//                .option(ChannelOption.SO_BACKLOG, 128)
//                .option(ChannelOption.SO_KEEPALIVE, true)
//                .handler(new LoggingHandler(LogLevel.INFO))
//                .childHandler(new ChannelInitializer<SocketChannel>() {
//                    @Override
//                    protected void initChannel(SocketChannel socketChannel) {
//                        ChannelPipeline pipeline = socketChannel.pipeline();
//                        pipeline.addLast(new HttpServerCodec());
//                        pipeline.addLast(new ChunkedWriteHandler());
//                        pipeline.addLast(new HttpObjectAggregator(8 * 1024));
//                        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
//                        pipeline.addLast(new NettyWebSocketServerHandler());
//                    }
//                });
//        server.bind(WEB_SOCKET_PORT).sync();
//        log.info("WebSocket Server is running on {}", WEB_SOCKET_PORT);
//    }
//}
