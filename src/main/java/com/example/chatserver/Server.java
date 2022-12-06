package com.example.chatserver;
import javafx.scene.layout.VBox;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedWriter;
import java.io.BufferedReader;

public class Server {

    private Socket socket;
    //character streams for messages
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    //takes server socket object passed from controller
    public Server(ServerSocket serverSocket) {
        try {
            //return socket object
            this.socket = serverSocket.accept();
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            System.out.println("Server Error");
            e.printStackTrace();
        }
    }

    public void sendMessageToClient(String messageToClient) {
        try {
            bufferedWriter.write(messageToClient);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error sending message");
            closeEverything(socket, bufferedWriter, bufferedReader);
        }
    }

    public void receiveMessageFromClient(VBox vBox) {
        new Thread(() -> {
            while (socket.isConnected()) {
                try {
                String messageFromClient = bufferedReader.readLine();
                Controller.addLabel((messageFromClient), vBox);
            } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Error receiving message");
                    closeEverything(socket, bufferedWriter, bufferedReader);
                    break;
                }
                }
        }).start();
    }

    //close everything in case of error to preserve resource
    public void closeEverything(Socket socket, BufferedWriter bufferedWriter, BufferedReader bufferedReader) {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
