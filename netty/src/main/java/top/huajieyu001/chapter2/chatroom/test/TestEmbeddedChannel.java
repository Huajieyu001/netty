package top.huajieyu001.chapter2.chatroom.test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LoggingHandler;
import top.huajieyu001.chapter2.chatroom.message.LoginRequestMessage;
import top.huajieyu001.chapter2.chatroom.protocol.MessageCodec;
import top.huajieyu001.chapter2.chatroom.protocol.MessageCodecSharable;

/**
 * @Author huajieyu
 * @Date 2026/2/17 19:27
 * @Version 1.0
 * @Description TODO
 */
public class TestEmbeddedChannel {

    public static void main(String[] args) {
        testDecoderSharableResolveHalfPackageAndPastePackage();
    }

    public static void testEncoder() {
        EmbeddedChannel channel = new EmbeddedChannel(new LoggingHandler(), new MessageCodec());

        LoginRequestMessage loginRequestMessage = new LoginRequestMessage("zhangsan", "123456");
        channel.writeOutbound(loginRequestMessage);
    }

    public static void testDecoder() {
        EmbeddedChannel channel = new EmbeddedChannel(new LoggingHandler(), new MessageCodec());

        LoginRequestMessage loginRequestMessage = new LoginRequestMessage("zhangsan", "123456");
        MessageCodec messageCodec = new MessageCodec();
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        try {
            messageCodec.encode(null, loginRequestMessage, buf);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        channel.writeInbound(buf);
    }

    public static void testDecoderHalfPackage() {
        EmbeddedChannel channel = new EmbeddedChannel(new LoggingHandler(), new MessageCodec());

        LoginRequestMessage loginRequestMessage = new LoginRequestMessage("zhangsan", "123456");
        MessageCodec messageCodec = new MessageCodec();
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        try {
            messageCodec.encode(null, loginRequestMessage, buf);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        ByteBuf slice1 = buf.slice(0, 100);
        slice1.retain();
        ByteBuf slice2 = buf.slice(100, buf.readableBytes() - 100);
        channel.writeInbound(slice1);
//        channel.writeInbound(slice2);
    }

    /**
     * 使用LengthFieldBasedFrameDecoder解决半包问题
     */
    public static void testDecoderResolveHalfPackageAndPastePackage() {
        EmbeddedChannel channel = new EmbeddedChannel(
                new LengthFieldBasedFrameDecoder(1024, 12, 4, 0, 0),
                new LoggingHandler(),
                new MessageCodec());

        LoginRequestMessage loginRequestMessage = new LoginRequestMessage("zhangsan", "123456");
        MessageCodec messageCodec = new MessageCodec();
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        try {
            messageCodec.encode(null, loginRequestMessage, buf);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        ByteBuf slice1 = buf.slice(0, 100);
        slice1.retain();
        ByteBuf slice2 = buf.slice(100, buf.readableBytes() - 100);
        channel.writeInbound(slice1);
        channel.writeInbound(slice2);
    }

    /**
     * 使用LengthFieldBasedFrameDecoder解决半包问题
     */
    public static void testDecoderSharableResolveHalfPackageAndPastePackage() {
        EmbeddedChannel channel = new EmbeddedChannel(
                new LengthFieldBasedFrameDecoder(1024, 12, 4, 0, 0),
                new LoggingHandler(),
                new MessageCodecSharable());

        LoginRequestMessage loginRequestMessage = new LoginRequestMessage("zhangsan", "988");
        MessageCodec messageCodec = new MessageCodec();
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        try {
            messageCodec.encode(null, loginRequestMessage, buf);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        ByteBuf slice1 = buf.slice(0, 100);
        slice1.retain();
        ByteBuf slice2 = buf.slice(100, buf.readableBytes() - 100);
        channel.writeInbound(slice1);
        channel.writeInbound(slice2);
    }
}
