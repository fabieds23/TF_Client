package main;

import com.sun.tools.javac.Main;
import javafx.scene.control.Label;

import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;

public class Client {
    private Socket socket;
    private BufferedReader readerClient;
    private BufferedWriter writerClient;

    public Client(Socket socket) {
        try {
            this.socket = socket;
            this.readerClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writerClient = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            System.out.println("Error creando el cliente");
        }
    }
    public void sendMessagetoServer(String clientMessage) {
        try {
            writerClient.write(clientMessage);
            writerClient.newLine();
            writerClient.flush();
        } catch (IOException e) {
            System.out.println("Error enviando el mensaje al servidor");
        }
    }

    public void receiveMessageFromServer(Label temp) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (socket.isConnected()) {
                    try {
                        String messagefromServer = readerClient.readLine();
                        System.out.println(messagefromServer);
                    } catch (IOException e) {
                        System.out.println("Error recibiendo mensaje del servidor");
                        break;
                    }
                }
            }
        }).start();
    }
}
