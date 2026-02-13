package top.huajieyu001.chapter1;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author huajieyu
 * @Date 2026/2/13 13:51
 * @Version 1.0
 * @Description TODO
 */
public class P019 {

    public static void main(String[] args) {
        final AtomicInteger dirCount = new AtomicInteger(0);

        final AtomicInteger fileCount = new AtomicInteger(0);

        try {
            Files.walkFileTree(Paths.get("G:\\AAAKey"), new SimpleFileVisitor<Path>() {

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    dirCount.incrementAndGet();
                    return super.preVisitDirectory(dir, attrs);
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    fileCount.incrementAndGet();
                    return super.visitFile(file, attrs);
                }
            });

            System.out.println("Directory count: " + dirCount.get());
            System.out.println("File count: " + fileCount.get());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
