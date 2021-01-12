package org.tomcat.fileChannel;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author zzj
 * @version 1.0
 * @date 2021/1/2 23:37
 * @description
 */
public class ChannelDemo {

    private static final String CONTENT = "Zero copy implemented by FileChannel";
    private static final String SOURCE_FILE = "source.txt";
    private static final String TARGET_FILE = "target.txt";
    private static final String CHARSET = "UTF-8";

    public static void main(String[] args) throws Exception {
        setup();
        transferTo();
        //transferFrom();
    }


    public static void setup() {
        Path source = Paths.get(SOURCE_FILE);
        byte[] bytes = CONTENT.getBytes(Charset.forName(CHARSET));
        try (FileChannel fromChannel = FileChannel.open(source, StandardOpenOption.READ,
                StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
            fromChannel.write(ByteBuffer.wrap(bytes));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void transferTo() throws Exception {
        try (FileChannel fromChannel = new RandomAccessFile(
                SOURCE_FILE, "rw").getChannel();
             FileChannel toChannel = new RandomAccessFile(
                     TARGET_FILE, "rw").getChannel()) {
            long position = 0L;
            long offset = fromChannel.size();
            fromChannel.transferTo(position, offset, toChannel);
        }
    }

    public static void transferFrom() throws Exception {
        try (FileChannel fromChannel = new RandomAccessFile(
                SOURCE_FILE, "rw").getChannel();
             FileChannel toChannel = new RandomAccessFile(
                     TARGET_FILE, "rw").getChannel()) {
            long position = 0L;
            long offset = fromChannel.size();
            toChannel.transferFrom(fromChannel, position, offset);
        }
    }

}
