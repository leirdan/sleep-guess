package br.com.ufrn;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer {
  public static void main(String[] args) {
    String clientSentence;

    try (ServerSocket serverSocket = new ServerSocket(4242)) {
      while (true) {
        Socket connSocket = serverSocket.accept();
        var inFromClient = new BufferedReader(new InputStreamReader(connSocket.getInputStream()));
        var outToClient = new DataOutputStream(connSocket.getOutputStream());
        clientSentence = inFromClient.readLine();
        clientSentence = clientSentence.toUpperCase() + "\n";
        outToClient.writeBytes(clientSentence);
      }
    } catch (IOException e) {
      System.out.println("Exception during game initialization: " + e.getMessage());
    }
  }
}
