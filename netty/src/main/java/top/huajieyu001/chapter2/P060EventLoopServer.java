package top.huajieyu001.chapter2;

import com.google.common.base.Charsets;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author huajieyu
 * @Date 2026/2/14 14:35
 * @Version 1.0
 * @Description TODO
 */
@Slf4j
public class P060EventLoopServer {

    public static void main(String[] args) {
        test2();
    }

    public static void test1() {
        EventLoopGroup helpGroup = new DefaultEventLoopGroup();

        new ServerBootstrap()
                .group(new NioEventLoopGroup(2), new NioEventLoopGroup(2))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast("group1", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf buf = (ByteBuf) msg;
                                log.debug("receive msg: {}", buf.toString(Charsets.UTF_8));
                                ctx.fireChannelRead(msg);
                            }
                        }).addLast(helpGroup, "group2", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf buf = (ByteBuf) msg;
                                log.debug("receive msg: {}", buf.toString(Charsets.UTF_8));
                            }
                        });
                    }

                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        log.debug("channelActive{}", ctx.channel());
                        super.channelActive(ctx);
                    }
                })
                .bind(8080);
    }

    public static void test2() {

        new ServerBootstrap()
                .group(new NioEventLoopGroup(2), new NioEventLoopGroup(2))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast("group1", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf buf = (ByteBuf) msg;
                                log.debug("receive msg: {}", buf.toString(Charsets.UTF_8));
                            }
                        });
                    }

                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        log.debug("channelActive{}", ctx.channel());
                        super.channelActive(ctx);
                    }
                })
                .bind(8080);
    }
}
