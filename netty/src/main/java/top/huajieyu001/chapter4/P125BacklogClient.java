package top.huajieyu001.chapter4;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author huajieyu
 * @Date 2026/2/18 8:36
 * @Version 1.0
 * @Description TODO
 */
@Slf4j
public class P125BacklogClient {

    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                startClient();
            }).start();
        }
    }

    public static void startClient() {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            log.debug("start client");
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new LoggingHandler());

            ChannelFuture future = bootstrap.connect("127.0.0.1", 8080);
            future.sync().channel().closeFuture().sync();
        } catch (Exception e) {
            log.debug("time out");
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}
