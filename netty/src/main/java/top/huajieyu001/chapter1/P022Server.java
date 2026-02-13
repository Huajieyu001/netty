package top.huajieyu001.chapter1;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author huajieyu
 * @Date 2026/2/13 14:06
 * @Version 1.0
 * @Description TODO
 */
@Slf4j
public class P022Server {

    public static void main(String[] args) throws IOException {
        testNonBlocking();
    }

    public static void testBlocking() throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();

        ssc.bind(new InetSocketAddress(8080));

        List<SocketChannel> channels = new ArrayList<SocketChannel>();

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        while (true) {
            log.debug("准备建立连接");
            SocketChannel sc = ssc.accept();
            log.debug("连接成功 -- {}", sc);
            channels.add(sc);

            for (SocketChannel channel : channels) {
                log.debug("准备读取数据 -- {}", channel);
                channel.read(buffer);
                buffer.flip();
                while (buffer.hasRemaining()) {
                    System.out.print((char) buffer.get());
                }
                System.out.println();
                buffer.clear();
                log.debug("数据读取完成 -- {}", channel);
            }
        }
    }

    public static void testNonBlocking() throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();

        ssc.bind(new InetSocketAddress(8080));
        ssc.configureBlocking(false); // 配置ssc为非阻塞模式，默认为阻塞

        List<SocketChannel> channels = new ArrayList<SocketChannel>();

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        while (true) {
            SocketChannel sc = ssc.accept();
            if (sc == null) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } else {
                log.debug("连接成功 -- {}", sc);
                sc.configureBlocking(false); // 把SocketChannel设置为非阻塞模式
                channels.add(sc);
            }
            for (SocketChannel channel : channels) {
                int readByteCount = channel.read(buffer);
                if (readByteCount <= 0) {
                    continue;
                }
                log.debug("准备读取数据 -- {}", channel);
                buffer.flip();
                while (buffer.hasRemaining()) {
                    System.out.print((char) buffer.get());
                }
                System.out.println();
                buffer.clear();
                log.debug("数据读取完成 -- {}", channel);
            }
        }
    }
}
