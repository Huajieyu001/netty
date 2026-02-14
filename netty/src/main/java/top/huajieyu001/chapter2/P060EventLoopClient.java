package top.huajieyu001.chapter2;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @Author huajieyu
 * @Date 2026/2/14 14:41
 * @Version 1.0
 * @Description TODO
 */
@Slf4j
public class P060EventLoopClient {

    public static void main(String[] args) {
        test3();
    }

    public static void test1() {
        try {
            Channel channel = new Bootstrap()
                    .group(new NioEventLoopGroup())
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                            nioSocketChannel.pipeline().addLast(new StringEncoder());
                        }
                    })
                    .connect("localhost", 8080)
                    .sync()
                    .channel();

            log.debug("channel is connected [{}] !", channel);
            channel.writeAndFlush("Hello World");

            System.in.read();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void test2() {
        try {
            ChannelFuture future = new Bootstrap()
                    .group(new NioEventLoopGroup())
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                            nioSocketChannel.pipeline().addLast(new StringEncoder());
                        }
                    })
                    .connect("localhost", 8080);

            // connect是异步的，不使用sync的话，可能就会在connect未完成之前获取到不完整的channel，这时候对channel的操作就是无用的
//            future.sync();
            future.channel().writeAndFlush("Hello BBB");
            System.in.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
    }

    public static void test3() {
        try {
            ChannelFuture future = new Bootstrap()
                    .group(new NioEventLoopGroup())
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                            nioSocketChannel.pipeline().addLast(new StringEncoder());
                        }
                    })
                    .connect("localhost", 8080);

            future.addListener(new ChannelFutureListener() {
                // 建立完成之后才会执行此方法，所以没有使用sync的话就可以使用addListener，而且执行listener方法的也不是主线程，而是nioEventLoop
                // 所以也不会阻塞影响主线程
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    channelFuture.channel().writeAndFlush("Hello CCC");
                    log.debug("channel is connected [{}] !", channelFuture.channel());
                }
            });
            System.in.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
