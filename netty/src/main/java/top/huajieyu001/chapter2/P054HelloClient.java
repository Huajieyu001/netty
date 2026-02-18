package top.huajieyu001.chapter2;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;


/**
 * @Author huajieyu
 * @Date 2026/2/14 13:35
 * @Version 1.0
 * @Description TODO
 */
public class P054HelloClient {

    public static void main(String[] args) {
        try {
            new Bootstrap()
                    .group(new NioEventLoopGroup())
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new StringEncoder());
                        }
                    })
                    .connect("localhost", 8080)
                    .sync()
                    .channel()
                    .writeAndFlush("Hello world");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
