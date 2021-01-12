package org.tomcat.directBuffer;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author zzj
 * @version 1.0
 * @date 2021/1/4 20:59
 * @description
 */
public class DirectBuffer {

    public static void main(String[] args) throws IOException {
        String infile = "source.txt";
        FileInputStream fin = new FileInputStream(infile);
        FileChannel fChanel = fin.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
        int r = fChanel.read(byteBuffer);
        System.out.println("r=" + r);
        byteBuffer.flip();
        while (byteBuffer.hasRemaining()) {
            byte c = byteBuffer.get();
            System.out.print("==" + (char) c);
        }

    }

}
