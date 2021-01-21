package com.sl.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import java.io.*;

/**
 * @author 李旭日
 * 判断json格式的传参
 */
public class WebRequestReadUtils {

    private static final int BUFFER_SIZE = 1024 * 8;

    public static String read(HttpServletRequest request) throws IOException {
        BufferedReader bufferedReader = request.getReader();
        StringWriter writer = new StringWriter();
        write(bufferedReader,writer);
        return writer.getBuffer().toString();
    }

    public static long write(Reader reader, Writer writer) throws IOException {
        return write(reader, writer, BUFFER_SIZE);
    }

    public static long write(Reader reader, Writer writer, int bufferSize) throws IOException
    {
        int read;
        long total = 0;
        char[] buf = new char[bufferSize];
        while( ( read = reader.read(buf) ) != -1 ) {
            writer.write(buf, 0, read);
            total += read;
        }
        return total;
    }
}