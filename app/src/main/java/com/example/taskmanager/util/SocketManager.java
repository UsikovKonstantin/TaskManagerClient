package com.example.taskmanager.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketManager {
    private static Socket socket;
    private static InputStream inputStream;
    private static PrintWriter printWriter;
    private static boolean connected = false;
    private static String result;

    public static void connect(String host, int port) {
        if (!connected) {
            try {
                socket = new Socket(host, port);
                inputStream = socket.getInputStream();
                printWriter = new PrintWriter(socket.getOutputStream(), true);
                connected = true;
            } catch (IOException exception) {

            }
        }
    }

    public static boolean connectParallel(String host, int port) {
        Thread thread = new Thread(() -> {
            connect(host, port);
            receive();
        });
        thread.start();

        try {
            thread.join(1000);
        } catch (InterruptedException exception) {
            thread.interrupt();
        }

        return connected;
    }

    public static void disconnect() {
        if (connected) {
            try {
                socket.close();
                inputStream.close();
                printWriter.close();
                connected = false;
            } catch (IOException ignored) {

            }
        }
    }

    public static boolean disconnectParallel() {
        Thread thread = new Thread(SocketManager::disconnect);
        thread.start();

        try {
            thread.join(1000);
            return true;
        } catch (InterruptedException exception) {
            thread.interrupt();
            return false;
        }
    }

    public static void send(String message) {
        if (connected) {
            printWriter.println(message);
        }
    }

    public static boolean sendParallel(String message) {
        if (connected) {
            Thread thread = new Thread(() -> send(message));
            thread.start();

            try {
                thread.join(1000);
                return true;
            } catch (InterruptedException exception) {
                thread.interrupt();
                return false;
            }
        } else {
            return false;
        }
    }

    public static void receive() {
        if (connected) {
            try {
                byte[] buffer = new byte[1024];
                int bytesRead = inputStream.read(buffer);
                result = new String(buffer, 0, bytesRead);
            } catch (IOException ignored) {

            }
        }
    }

    public static boolean receiveParallel() {
        if (connected) {
            Thread thread = new Thread(SocketManager::receive);
            thread.start();

            try {
                thread.join(1000);
                return true;
            } catch (InterruptedException exception) {
                thread.interrupt();
                return false;
            }
        } else {
            return false;
        }
    }

    public static String getResult() {
        return result;
    }
}

