package top.huajieyu001.chapter2;

import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.Future;

/**
 * @Author huajieyu
 * @Date 2026/2/14 18:14
 * @Version 1.0
 * @Description TODO
 */
public class P073NettyFuture {

    public static void main(String[] args) {

        EventLoopGroup group = new NioEventLoopGroup(1);

        EventLoop worker = group.next();

        Future<Integer> nettyFuture = worker.submit(() -> {
            Thread.sleep(1000);
            return 10;
        });

        nettyFuture.addListener(future -> {
            future.sync();
            System.out.println("Successfully completed");
            System.out.println(future.get());
        });
    }
}
