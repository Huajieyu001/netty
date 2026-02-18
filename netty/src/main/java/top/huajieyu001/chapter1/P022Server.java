package top.huajieyu001.chapter1;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;

/**
 * @Author huajieyu
 * @Date 2026/2/13 14:06
 * @Version 1.0
 * @Description TODO
 */
@Slf4j
public class P022Server {

    public static void main(String[] args) throws IOException {
        testNonBlockingSelector();
    }

    public static void split(ByteBuffer source) throws IOException {
        source.flip();

        for (int i = 0; i < source.limit(); i++) {
            if(source.get(i) == '\n') {
                int length = i + 1 - source.position();
                ByteBuffer target = ByteBuffer.allocate(length);
                for (int j = 0; j < length; j++) {
                    target.put(source.get());
                }
                printAll(target);
            }
        }
        source.compact();
    }

    public static void printAll(ByteBuffer source) {
        source.flip();
        System.out.println("start print---------------------------------");
        for (int i = 0; i < source.limit(); i++) {
            System.out.print((char) source.get());
        }
        System.out.println("end print---------------------------------");
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

    public static void testNonBlockingSelector() throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        Selector selector = Selector.open();

        SelectionKey selectionKey = ssc.register(selector, 0, null);
        selectionKey.interestOps(SelectionKey.OP_ACCEPT);
        ssc.bind(new InetSocketAddress(8080));
        log.debug("selectionKey {}", selectionKey);

        while (true) {
            selector.select();
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();

            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                log.debug("key {}", key);
                if (key.isAcceptable()) {
                    ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                    SocketChannel sc = channel.accept();
                    sc.configureBlocking(false);
                    ByteBuffer buffer = ByteBuffer.allocate(8);
                    SelectionKey scKey = sc.register(selector, 0, buffer);
                    scKey.interestOps(SelectionKey.OP_READ);
                    log.debug("建立连接 {}", channel);
                } else if (key.isReadable()) {
                    try {
                        SocketChannel channel = (SocketChannel) key.channel();
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        if(channel.read(buffer) == -1){
                            key.cancel();
                            continue;
                        }
                        split(buffer);
                        // 扩容buffer
                        if(buffer.position() == buffer.limit()){
                            ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity() << 1);
                            buffer.flip();
                            newBuffer.put(buffer);
                            key.attach(newBuffer);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        key.cancel();
                    }
                }

                iterator.remove();
            }
        }
    }
}
