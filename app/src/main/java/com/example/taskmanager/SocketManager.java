package com.example.taskmanager;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketManager {
    private static Socket socket;
    private static InputStream inputStream;
    private static PrintWriter printWriter;
    private static boolean connected = false;
    public static final Object lock = new Object();
    public static String result;

    public static void connect(String host, int port) {
        if (!connected) {
            try {
                socket = new Socket(host, port);
                inputStream = socket.getInputStream();
                printWriter = new PrintWriter(socket.getOutputStream(), true);
                connected = true;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void disconnect() {
        if (connected) {
            try {
                socket.close();
                inputStream.close();
                printWriter.close();
                connected = false;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void send(String message) {
        if (connected) {
            printWriter.println(message);
        }
    }

    public static String receive() {
        synchronized (lock) {
            String message = null;
            if (connected) {
                try {
                    byte[] buffer = new byte[1024];
                    int bytesRead = inputStream.read(buffer);
                    message = new String(buffer, 0, bytesRead);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            lock.notify();
            return message;
        }
    }
}

