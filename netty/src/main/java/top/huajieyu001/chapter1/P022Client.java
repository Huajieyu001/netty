package top.huajieyu001.chapter1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.UUID;

/**
 * @Author huajieyu
 * @Date 2026/2/13 14:14
 * @Version 1.0
 * @Description TODO
 */
public class P022Client {

    public static void main(String[] args) throws IOException {

        try {
            SocketChannel sc = SocketChannel.open();
            sc.connect(new InetSocketAddress("localhost", 8080));

            System.out.println("Connected to server");

            Thread.sleep(20000);

            sc.write(Charset.defaultCharset().encode(UUID.randomUUID().toString()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
