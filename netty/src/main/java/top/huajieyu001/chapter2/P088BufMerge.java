package top.huajieyu001.chapter2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;

/**
 * @Author huajieyu
 * @Date 2026/2/16 23:52
 * @Version 1.0
 * @Description TODO
 */
public class P088BufMerge {

    public static void main(String[] args) {
        ByteBuf b1 = ByteBufAllocator.DEFAULT.buffer();
        ByteBuf b2 = ByteBufAllocator.DEFAULT.buffer();

        b1.writeBytes(new byte[] { 1, 2, 3, 4, 5 });
        b2.writeBytes(new byte[] { 6, 7, 8, 9, 10});

        CompositeByteBuf compositeByteBuf = ByteBufAllocator.DEFAULT.compositeBuffer();

        compositeByteBuf.addComponents(true, b1, b2);

        NettyUtils.log(compositeByteBuf);
    }
}
