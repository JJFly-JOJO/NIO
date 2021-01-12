package org.tomcat;

import java.io.IOException;
import java.nio.channels.spi.SelectorProvider;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException {
        SelectorProvider.provider().openSelector();
    }
}
