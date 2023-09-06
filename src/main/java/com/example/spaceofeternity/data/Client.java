package com.example.spaceofeternity.data;

import java.io.*;
import java.net.Socket;

public class Client {
    private static Socket clientSocket;
    private static BufferedReader in;
    private static BufferedWriter out;

    private String[] newNickNameAndScore;
    private String newNickName;
    private int newScore;

    public Client(String nickName, String score) {
        try {
            try {
                clientSocket = new Socket("localhost", 8080);

                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                String text = nickName + " " + score + "\n";
                out.write(text);
                out.flush();
                String serverWord = in.readLine();
                newNickNameAndScore = serverWord.split(" ");
                newNickName = newNickNameAndScore[0];
                newScore = Integer.parseInt(newNickNameAndScore[1]);
            } finally {
                clientSocket.close();
                in.close();
                out.close();
            }
        } catch (Exception e) {
            System.out.println("Подключение к серверу не было произведено. Работа продолжится в автономном режиме.");
        }
    }

    public String getNewNickName() { return newNickName; }

    public int getNewScore() { return newScore; }
}
