package com.example.spaceofeternity.data;

import java.io.*;
import java.net.Socket;

public class ServerSomething extends Thread {
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;

    public ServerSomething(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        start();
    }

    @Override
    public void run() {
        String nickNameAndScore;
        try {
            while (true) {
                nickNameAndScore = in.readLine();
                for (ServerSomething vr : Server.serverList) {
                    vr.send(nickNameAndScore);
                }
            }
        } catch (IOException e) {
        }
    }

    private void send(String msg) {
        try {
            out.write(msg + "\n");
            out.flush();
        } catch (IOException ignored) {}
    }
}
