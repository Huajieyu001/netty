package top.huajieyu001.chapter1;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @Author huajieyu
 * @Date 2026/2/12 20:43
 * @Version 1.0
 * @Description TODO
 */
@Slf4j
public class P006 {

    public static void main(String[] args) {
        test4();
    }

    public static void test1() {
        try (FileChannel channel = new FileInputStream("G:\\Program\\JAVA_WorkSpace\\project1\\netty\\netty\\src\\main\\resources\\data.txt").getChannel()) {
            ByteBuffer buffer = ByteBuffer.allocate(10);
            while (channel.read(buffer) != -1) {
                buffer.flip(); // 切换到buffer的读模式
                while (buffer.hasRemaining()) {
                    log.debug("读取的字节{}", (char)buffer.get());
                }
                buffer.clear(); // 切换到buffer的写模式
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void test2() {
        try (FileChannel channel = new FileInputStream("G:\\Program\\JAVA_WorkSpace\\project1\\netty\\netty\\src\\main\\resources\\data.txt").getChannel()) {
            ByteBuffer buffer = ByteBuffer.allocate(10);
            channel.read(buffer);
            buffer.flip(); // 切换到buffer的读模式
            log.debug("读取的字节{}", (char)buffer.get(0));
            log.debug("读取的字节{}", (char)buffer.get(1));
            log.debug("读取的字节{}", (char)buffer.get(0));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void test3() {
        try (FileChannel channel = new FileInputStream("G:\\Program\\JAVA_WorkSpace\\project1\\netty\\netty\\src\\main\\resources\\data.txt").getChannel()) {
            ByteBuffer buffer = ByteBuffer.allocate(10);
            boolean flag = false;
            while (channel.read(buffer) != -1) {
                buffer.flip(); // 切换到buffer的读模式
                while (buffer.hasRemaining()) {
                    log.debug("1读取的字节{}", (char)buffer.get());
                    log.debug("1index {}", buffer.position());
                }
                if (flag) {
                    break;
                } else {
                    buffer.rewind();
                    while (buffer.hasRemaining()) {
                        log.debug("2读取的字节{}", (char)buffer.get());
                        log.debug("2index {}", buffer.position());
                    }
                    flag = true;
                }
                buffer.clear();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void test4() {
        try(FileChannel channel = new FileInputStream("data.txt").getChannel()) {
            ByteBuffer buffer = ByteBuffer.allocate(10);
            buffer.put(new byte[] {'a', 'b', 'c', 'd'});

            buffer.flip();
            log.debug("" + buffer.get());
            log.debug("" + buffer.get());
            buffer.mark();
            log.debug("" + buffer.get());
            log.debug("" + buffer.get());
            buffer.reset();
            log.debug("" + buffer.get());
            log.debug("" + buffer.get());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
