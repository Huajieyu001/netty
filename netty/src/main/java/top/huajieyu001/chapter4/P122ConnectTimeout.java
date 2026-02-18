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
 * @Date 2026/2/17 22:41
 * @Version 1.0
 * @Description TODO
 */
@Slf4j
public class P122ConnectTimeout {
    public static void main(String[] args) {
        startClient();
    }

    public static void startClient() {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            log.debug("start client");
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000)
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
