package top.huajieyu001.chapter2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author huajieyu
 * @Date 2026/2/17 16:39
 * @Version 1.0
 * @Description TODO
 */
@Slf4j
public class P100HttpProtocol {

    public static void main(String[] args) {
        startHttpServer();
    }

    public static void startHttpServer() {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss, worker);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                    socketChannel.pipeline().addLast(new HttpServerCodec());
                    /*socketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            log.debug("{}", msg.getClass());

                            if(msg instanceof HttpRequest) {
                                HttpRequest request = (HttpRequest) msg;
                            } else if (msg instanceof HttpContent) {
                                HttpContent content = (HttpContent) msg;
                            } else {
                                log.error("Failed class : {}", msg.getClass());
                            }
                        }
                    });*/
                    socketChannel.pipeline().addLast(new SimpleChannelInboundHandler<HttpRequest>() {
                        @Override
                        protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpRequest httpRequest) throws Exception {
                            // 获取请求的uri
                            log.debug(httpRequest.uri());

                            // 返回响应
                            DefaultFullHttpResponse response = new DefaultFullHttpResponse(httpRequest.protocolVersion(), HttpResponseStatus.OK);
                            String result = "<h1>Hello World</h1>";
                            response.content().writeBytes(result.getBytes());
                            response.headers().setInt(HttpHeaders.Names.CONTENT_LENGTH, response.content().readableBytes());

                            channelHandlerContext.writeAndFlush(response);
                        }
                    });
                }
            });
            ChannelFuture channelFuture = bootstrap.bind(8080).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
