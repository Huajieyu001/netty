package top.huajieyu001.chapter2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @Author huajieyu
 * @Date 2026/2/17 16:23
 * @Version 1.0
 * @Description TODO
 */
public class P098LengthField {

    public static void main(String[] args) {

        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4), new LoggingHandler(LogLevel.DEBUG));
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        send(buf, "Hello, World");
        send(buf, "Welcome to the world!");
        send(buf, "你好");
        embeddedChannel.writeInbound(buf);
    }

    protected static void send(ByteBuf buf, String content) {
        buf.writeInt(content.getBytes().length);
        buf.writeBytes(content.getBytes());
    }
}
