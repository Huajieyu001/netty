package top.huajieyu001.chapter2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

/**
 * @Author huajieyu
 * @Date 2026/2/16 23:14
 * @Version 1.0
 * @Description TODO
 */
public class P086Slice {

    public static void main(String[] args) {
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();

        buf.writeBytes(new byte[] {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'});
        NettyUtils.log(buf);

        ByteBuf slice = buf.slice(0, 5);
        NettyUtils.log(slice);
        slice.clear();
        slice.writeBytes(new byte[] {'a', 'a', 'a', 'a', 'a'}); // slice后的buf和原buf是共享内存的，修改slice的对象会影响到原对象
//        slice.writeBytes(new byte[] {'a', 'a', 'a', 'a', 'a', 'a'}); // slice后的buf写入capacity限制最大不超过截取的长度
        NettyUtils.log(slice);
        NettyUtils.log(buf);
    }
}
