package top.huajieyu001.chapter2.chatroom.test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.logging.LoggingHandler;
import top.huajieyu001.chapter2.chatroom.message.LoginRequestMessage;
import top.huajieyu001.chapter2.chatroom.protocol.MessageCodec;

/**
 * @Author huajieyu
 * @Date 2026/2/17 19:27
 * @Version 1.0
 * @Description TODO
 */
public class TestEmbeddedChannel {

    public static void main(String[] args) {
        testDecoder();
    }

    public static void testEncoder() {
        EmbeddedChannel channel = new EmbeddedChannel(new LoggingHandler(), new MessageCodec());

        LoginRequestMessage loginRequestMessage = new LoginRequestMessage("zhangsan", "123456", "张三");
        channel.writeOutbound(loginRequestMessage);
    }

    public static void testDecoder() {
        EmbeddedChannel channel = new EmbeddedChannel(new LoggingHandler(), new MessageCodec());

        LoginRequestMessage loginRequestMessage = new LoginRequestMessage("zhangsan", "123456", "张三");
        MessageCodec messageCodec = new MessageCodec();
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        try {
            messageCodec.encode(null, loginRequestMessage, buf);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        channel.writeInbound(buf);
    }
}
