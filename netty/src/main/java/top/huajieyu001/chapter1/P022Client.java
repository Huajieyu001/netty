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

            Thread.sleep(300);

//            sc.write(Charset.defaultCharset().encode(UUID.randomUUID().toString()));
            sc.write(Charset.defaultCharset().encode("1111122222333334444455555\n"));
            sc.write(Charset.defaultCharset().encode("江畔何人初见月\n"));

            System.out.println("Sending data");
            sc.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
