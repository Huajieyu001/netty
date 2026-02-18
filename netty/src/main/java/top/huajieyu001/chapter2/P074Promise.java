package top.huajieyu001.chapter2;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Promise;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;

/**
 * @Author huajieyu
 * @Date 2026/2/14 18:34
 * @Version 1.0
 * @Description TODO
 */
@Slf4j
public class P074Promise {

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        EventLoopGroup group = new NioEventLoopGroup(1);

        DefaultPromise<Integer> promise = new DefaultPromise<>(group.next());

        new Thread(() -> {
            log.debug("start");
            try {
                Thread.sleep(2000);
                int i = 1 / 0;
                promise.setSuccess(1);
            } catch (InterruptedException e) {
                promise.setFailure(e);
                throw new RuntimeException(e);
            }
        }).start();

        log.debug("result = {}", promise.get());
    }
}
