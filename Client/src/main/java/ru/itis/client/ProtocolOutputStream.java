package ru.itis.client;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

public class ProtocolOutputStream extends OutputStream {
    private OutputStream outputStream;

    public ProtocolOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void writeMessage(Message message) throws IOException {
        System.out.println(Arrays.toString(message.getData()));
        byte type = message.getType();
        if (message.getContentLength() > Protocol.MAX_ACTION_LENGTH) {
            return;
        }
        byte[] data = message.getData();
        int length = data.length;
        outputStream.write(type);
        outputStream.write(length >> 16);
        outputStream.write(length >> 8);
        outputStream.write(length);
        outputStream.write(data);
        outputStream.flush();
    }

    @Override
    public void write(int b) throws IOException {
        outputStream.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        outputStream.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        outputStream.write(b, off, len);
    }

    @Override
    public void flush() throws IOException {
        outputStream.flush();
    }

    @Override
    public void close() throws IOException {
        outputStream.close();
    }
}
