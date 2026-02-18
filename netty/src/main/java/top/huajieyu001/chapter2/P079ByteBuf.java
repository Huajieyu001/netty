package top.huajieyu001.chapter2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import lombok.extern.slf4j.Slf4j;

import static io.netty.buffer.ByteBufUtil.appendPrettyHexDump;
import static io.netty.util.internal.StringUtil.NEWLINE;

/**
 * @Author huajieyu
 * @Date 2026/2/16 22:14
 * @Version 1.0
 * @Description TODO
 */
@Slf4j
public class P079ByteBuf {

    public static void main(String[] args) {

        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        System.out.println(buf.getClass());

        NettyUtils.log(buf);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 300; i++) {
            sb.append("a");
        }

        buf.writeBytes(sb.toString().getBytes());
        // byteBuf自动扩容
        NettyUtils.log(buf);
    }
}

/**
 * 提供一些简单调试工具
 */
class NettyUtils {
    /**
     * 打印ByteBuf详细信息
     * @param buffer
     */
    public static void log(ByteBuf buffer) {
        int length = buffer.readableBytes();
        int rows = length / 16 + (length % 15 == 0 ? 0 : 1) + 4;
        StringBuilder buf = new StringBuilder(rows * 80 * 2);
        buf.append("read index").append(buffer.readerIndex())
                .append(" write index").append(buffer.writerIndex())
                .append(" capacity").append(buffer.capacity()).append(NEWLINE);
        appendPrettyHexDump(buf, buffer);
        System.out.println(buf.toString());
    }
}
