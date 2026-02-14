package top.huajieyu001.chapter2;

import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.NettyRuntime;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author huajieyu
 * @Date 2026/2/14 14:25
 * @Version 1.0
 * @Description TODO
 */
@Slf4j
public class P057 {

    public static void main(String[] args) {
        test1();
    }

    public static void test1 () {
        EventLoopGroup g1 = new NioEventLoopGroup(2);
//        EventLoopGroup g2 = new DefaultEventLoopGroup();

        System.out.println(NettyRuntime.availableProcessors());
        System.out.println(g1.next());
        System.out.println(g1.next());
        System.out.println(g1.next());
        System.out.println(g1.next());

        g1.submit(()-> {
            log.debug("执行任务 - {}", Thread.currentThread().getName());
        });
    }

}
