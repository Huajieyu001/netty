package top.huajieyu001.chapter2.chatroom;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import top.huajieyu001.chapter2.chatroom.message.*;
import top.huajieyu001.chapter2.chatroom.protocol.MessageCodecSharable;
import top.huajieyu001.chapter2.chatroom.protocol.ProtocolFrameDecoder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author huajieyu
 * @Date 2026/2/17 20:40
 * @Version 1.0
 * @Description TODO
 */
@Slf4j
public class ChatClient {

    public static void main(String[] args) {
        startClient();
    }

    public static void startClient() {
        NioEventLoopGroup group = new NioEventLoopGroup();
        LoggingHandler LOGGING_HANDLER = new LoggingHandler();
        MessageCodecSharable MESSAGE_CODEC_SHARABLE = new MessageCodecSharable();
        CountDownLatch latch = new CountDownLatch(1);
        AtomicBoolean loginState = new AtomicBoolean(false);
        AtomicBoolean EXIT = new AtomicBoolean(false);
        try {
            Bootstrap bootstrap = new Bootstrap();
            ChannelFuture future = bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel
                                    .pipeline()
                                    .addLast(new ProtocolFrameDecoder())
                                    .addLast(LOGGING_HANDLER)
                                    .addLast(MESSAGE_CODEC_SHARABLE)
                                    .addLast("client handler", new ChannelInboundHandlerAdapter() {
                                        @Override
                                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                            new Thread(() -> {
                                                Scanner scanner = new Scanner(System.in);
                                                System.out.println("请输入用户名：");
                                                String username = scanner.nextLine();
                                                System.out.println("请输入密码：");
                                                String password = scanner.nextLine();
                                                LoginRequestMessage message = new LoginRequestMessage(username, password);
                                                ctx.writeAndFlush(message);
                                                try {
                                                    latch.await();
                                                    // 登录失败

                                                } catch (InterruptedException e) {
                                                    throw new RuntimeException(e);
                                                }
                                                if(!loginState.get()) {
                                                    ctx.channel().close();
                                                    return;
                                                }
                                                while (true) {
                                                    System.out.println("==================================");
                                                    System.out.println("send [username] [content]");
                                                    System.out.println("gsend [group name] [content]");
                                                    System.out.println("gcreate [group name] [m1,m2,m3...]");
                                                    System.out.println("gmembers [group name]");
                                                    System.out.println("gjoin [group name]");
                                                    System.out.println("gquit [group name]");
                                                    System.out.println("quit");
                                                    System.out.println("==================================");
                                                    String command = null;
                                                    try {
                                                        command = scanner.nextLine();
                                                    } catch (Exception e) {
                                                        break;
                                                    }
                                                    if(EXIT.get()){
                                                        return;
                                                    }
                                                    String[] s = command.split(" ");
                                                    switch (s[0]){
                                                        case "send":
                                                            ctx.writeAndFlush(new ChatRequestMessage(username, s[1], s[2]));
                                                            break;
                                                        case "gsend":
                                                            ctx.writeAndFlush(new GroupChatRequestMessage(username, s[1], s[2]));
                                                            break;
                                                        case "gcreate":
                                                            Set<String> set = new HashSet<>(Arrays.asList(s[2].split(",")));
                                                            set.add(username); // 加入自己
                                                            ctx.writeAndFlush(new GroupCreateRequestMessage(s[1], set));
                                                            break;
                                                        case "gmembers":
                                                            ctx.writeAndFlush(new GroupMembersRequestMessage(s[1]));
                                                            break;
                                                        case "gjoin":
                                                            ctx.writeAndFlush(new GroupJoinRequestMessage(username, s[1]));
                                                            break;
                                                        case "gquit":
                                                            ctx.writeAndFlush(new GroupQuitRequestMessage(username, s[1]));
                                                            break;
                                                        case "quit":
                                                            ctx.channel().close();
                                                            return;
                                                    }
                                                }
                                            }, "input thread").start();
                                            super.channelActive(ctx);
                                        }

                                        @Override
                                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                            log.debug("received {}", msg);
                                            if(msg instanceof LoginResponseMessage) {
                                                LoginResponseMessage message = (LoginResponseMessage) msg;
                                                if(message.isSuccess()){
                                                    loginState.set(true);
                                                }
                                            }
                                            latch.countDown();
//                                            super.channelRead(ctx, msg);
                                        }
                                    });
                        }
                    }).connect("127.0.0.1", 8080);

            Channel channel = future.sync().channel();
            channel.closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}
