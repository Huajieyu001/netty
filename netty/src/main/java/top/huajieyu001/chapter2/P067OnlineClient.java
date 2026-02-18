package top.huajieyu001.chapter2;

import com.google.common.base.Charsets;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.NettyRuntime;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

/**
 * @Author huajieyu
 * @Date 2026/2/14 15:57
 * @Version 1.0
 * @Description TODO
 */
@Slf4j
public class P067OnlineClient {
    public static void main(String[] args) {
        try {
            start();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void start() throws InterruptedException {

        NioEventLoopGroup eventExecutors = new NioEventLoopGroup(NettyRuntime.availableProcessors() >> 1);

        ChannelFuture future = new Bootstrap()
                .group(eventExecutors)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        nioSocketChannel.pipeline().addLast(new StringEncoder(Charsets.UTF_8));
                    }
                })
                .connect("127.0.0.1", 8080);

        future.sync();
        Channel channel = future.channel();
        new Thread(() -> {
            Scanner sc = new Scanner(System.in);
            String line = null;
            while (!(line = sc.nextLine()).intern().equals("exit")) {
                channel.writeAndFlush(line);
                log.debug("您输入成功了-{}", line);
            }
            ChannelFuture closeFuture = channel.closeFuture();
            closeFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    log.debug("closed");
                    eventExecutors.shutdownGracefully();
                }
            });
        }).start();
    }
}
