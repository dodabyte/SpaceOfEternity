package com.example.spaceofeternity.data;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Server {
    public static final int PORT = 8080;
    public static LinkedList<ServerSomething> serverList = new LinkedList<>();

    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(PORT);
            try {
                while (true) {
                    Socket socket = server.accept();
                    try {
                        serverList.add(new ServerSomething(socket));
                    } catch (IOException e) {
                        socket.close();
                    }
                }
            } finally {
                server.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
