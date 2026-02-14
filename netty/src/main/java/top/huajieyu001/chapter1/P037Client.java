package top.huajieyu001.chapter1;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @Author huajieyu
 * @Date 2026/2/13 18:29
 * @Version 1.0
 * @Description TODO
 */
@Slf4j
public class P037Client {

    public static void main(String[] args) throws IOException {

        SocketChannel sc = SocketChannel.open();

        sc.connect(new InetSocketAddress("localhost", 8080));

//        Selector selector = Selector.open();
//        sc.register(selector, SelectionKey.OP_READ);

        int count = 0;
//        while (true) {
//            selector.select();
//            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
//            while (iterator.hasNext()) {
//                SelectionKey key = iterator.next();
//                log.debug("key = " + key);
//                iterator.remove();
//                if (key.isReadable()) {
//                    log.debug("Readable");
//                    SocketChannel channel = (SocketChannel) key.channel();
//                    ByteBuffer buffer = ByteBuffer.allocate(1024);
//                    channel.read(buffer);
//                    P022Server.printAll(buffer);
//                }
//            }
//        }

        while (true) {
            ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);
            count += sc.read(buffer);
            log.debug(count + "");
            buffer.clear();
        }
    }

}
