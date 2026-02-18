package top.huajieyu001.chapter1;

import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @Author huajieyu
 * @Date 2026/2/13 10:36
 * @Version 1.0
 * @Description TODO
 */
@Slf4j
public class P011 {

    public static void main(String[] args) {
        test3();
    }

    public static void test1() {
        ByteBuffer buffer = ByteBuffer.allocate(16);

        buffer.put("Hello world".getBytes());

        buffer.flip();
        while (buffer.hasRemaining()) {
            log.debug("字符是 {}", (char) buffer.get());
        }
    }

    public static void test2() {
        ByteBuffer buffer = ByteBuffer.allocate(16);

        Charset charset = Charset.forName("UTF-8");
        buffer.put(charset.encode("Hello world"));

        buffer.flip();
        while (buffer.hasRemaining()) {
            log.debug("字符是 {}", (char) buffer.get());
        }
    }

    public static void test3() {
        ByteBuffer buffer = StandardCharsets.UTF_8.encode("Hello world");

        while (buffer.hasRemaining()) {
            log.debug("<UNK> {}", (char) buffer.get());
        }

        buffer.rewind();
        String string = StandardCharsets.UTF_8.decode(buffer).toString();
        log.debug("String:  {}", string);
    }
}
