package top.huajieyu001.chapter1;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * @Author huajieyu
 * @Date 2026/2/13 10:53
 * @Version 1.0
 * @Description TODO
 */
@Slf4j
public class P012 {

    public static void main(String[] args) {
        test3();
    }

    /**
     * 并行写入数据
     */
    public static void test1(){
        ByteBuffer b1 = StandardCharsets.UTF_8.encode("I have");
        ByteBuffer b2 = StandardCharsets.UTF_8.encode(" worked in");
        ByteBuffer b3 = StandardCharsets.UTF_8.encode(" a hospital before");

        try (FileChannel channel = new RandomAccessFile("words.txt", "rw").getChannel()){
            channel.write(new ByteBuffer[]{b1, b2, b3});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * channel转移数据
     */
    public static void test2(){
        try(
                FileChannel from = new FileInputStream("words.txt").getChannel();
                FileChannel to = new FileOutputStream("to.txt").getChannel();
        ){
            from.transferTo(0, from.size(), to);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 尝试复制大于2g的文件
     */
    public static void test3(){
        try(
                FileChannel from = new FileInputStream("G:\\AAAKey\\RussianPassion\\5_6190393645703629788.mp4").getChannel();
                FileChannel to = new FileOutputStream("G:\\AAAKey\\RussianPassion\\Copied.mp4").getChannel();
        ){
            long start = 0;
            while(start < from.size()){
                start += from.transferTo(start, from.size(), to);
                log.debug(start + "\t" + from.size() + "\t" + to.size());
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
