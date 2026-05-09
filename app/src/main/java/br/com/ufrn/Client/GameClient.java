package br.com.ufrn.Client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class GameClient {
  public static void main(String[] args) {
    String sentence;

    try (var socket = new Socket("lunix", 4242)) {
      var inFromUser = new BufferedReader(new InputStreamReader(System.in));
      var outToServer = new DataOutputStream(socket.getOutputStream());

      var inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));

      while (true) {

        sentence = inFromUser.readLine();
        outToServer.writeBytes(sentence + "\n");
        sentence = inFromServer.readLine();
        System.out.println("S: " + sentence);

      }
    } catch (IOException e) {
      System.out.println("Exception while connecting to server: " + e.getMessage());
    }

  }
}
