package org.tomcat.byteBuffer;

import sun.misc.Cleaner;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.AccessController;
import java.security.PrivilegedAction;

import static java.nio.channels.FileChannel.MapMode.READ_ONLY;
import static java.nio.channels.FileChannel.MapMode.READ_WRITE;

/**
 * @author zzj
 * @version 1.0
 * @date 2020/12/29 21:43
 * @description
 */
public class MapByteBufferDemo {

    private final static String CONTENT = "Zero copy implemented by MappedByteBuffer";
    private final static String FILE_NAME = "mmap.txt";
    private final static String CHARSET = "UTF-8";

    public static void main(String[] args) throws IOException {
        //File file = new File("mmap.txt");
        //FileOutputStream fos=new FileOutputStream(file);
        ///fos.write("sss".getBytes());

        write();
        read();
    }

    public static void write() {
        Path path = Paths.get(FILE_NAME);
        byte[] bytes = CONTENT.getBytes(Charset.forName(CHARSET));
        try (FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.READ,
                StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
            MappedByteBuffer mappedByteBuffer = fileChannel.map(READ_WRITE, 0, bytes.length);
            if (mappedByteBuffer != null) {
                mappedByteBuffer.put(bytes);
                mappedByteBuffer.force();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void read() {
        Path path = Paths.get(FILE_NAME);
        int length = CONTENT.getBytes(Charset.forName(CHARSET)).length;
        try (FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.READ)) {
            MappedByteBuffer mappedByteBuffer = fileChannel.map(READ_ONLY, 0, length);
            if (mappedByteBuffer != null) {
                byte[] bytes = new byte[length];
                mappedByteBuffer.get(bytes);
                String content = new String(bytes, StandardCharsets.UTF_8);
                System.out.println(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void clean(final Object buffer) throws Exception {
        AccessController.doPrivileged((PrivilegedAction<Integer>) () -> {
            try {
                Method getCleanerMethod = buffer.getClass().getMethod("cleaner", new Class[0]);
                getCleanerMethod.setAccessible(true);
                Cleaner cleaner = (Cleaner) getCleanerMethod.invoke(buffer, new Object[0]);
                cleaner.clean();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        });
    }

}
