package top.huajieyu001.chapter2.chatroom;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import top.huajieyu001.chapter2.chatroom.message.LoginRequestMessage;
import top.huajieyu001.chapter2.chatroom.message.LoginResponseMessage;
import top.huajieyu001.chapter2.chatroom.protocol.MessageCodecSharable;
import top.huajieyu001.chapter2.chatroom.protocol.ProtocolFrameDecoder;
import top.huajieyu001.chapter2.chatroom.server.service.UserService;
import top.huajieyu001.chapter2.chatroom.server.service.UserServiceFactory;
import top.huajieyu001.chapter2.chatroom.server.session.SessionFactory;

/**
 * @Author huajieyu
 * @Date 2026/2/17 20:16
 * @Version 1.0
 * @Description TODO
 */
public class ChatServer {

    public static void main(String[] args) {
        startServer();
    }

    public static void startServer() {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        LoggingHandler LOGGINC_HANDLER = new LoggingHandler();
        MessageCodecSharable MESSAGE_CODEC = new MessageCodecSharable();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss, worker);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline()
                            .addLast(new ProtocolFrameDecoder())
                            .addLast(LOGGINC_HANDLER)
                            .addLast(MESSAGE_CODEC)
                            .addLast(new SimpleChannelInboundHandler<LoginRequestMessage>() {
                                @Override
                                protected void channelRead0(ChannelHandlerContext channelHandlerContext, LoginRequestMessage msg) throws Exception {
                                    String username = msg.getUsername();
                                    String password = msg.getPassword();
                                    UserService service = UserServiceFactory.getUserService();
                                    boolean flag = service.login(username, password);
                                    LoginResponseMessage response = null;
                                    if (flag) {
                                        response = new LoginResponseMessage(true, "success");
                                        SessionFactory.getSession().bind(channelHandlerContext.channel(), username);
                                    } else {
                                        response = new LoginResponseMessage(false, "login failed");
                                    }
                                    channelHandlerContext.writeAndFlush(response);
                                }
                            });
                }
            });
            ChannelFuture channelFuture = bootstrap.bind(8080).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
