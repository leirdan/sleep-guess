package br.com.ufrn.Client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class GameClient {
  public static void main(String[] args) {

    try (var socket = new Socket("lunix", 4224)) {
      var inFromUser = new BufferedReader(new InputStreamReader(System.in));
      var outToServer = new DataOutputStream(socket.getOutputStream());

      var inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));

      new Thread(() -> {
        try {
          String reverb;
          while ((reverb = inFromServer.readLine()) != null) {
            System.out.println("S: " + reverb);
          }
          System.exit(0);
        } catch (Exception e) {
          System.out.println("Connection closed");
        }
      }).start();

      String scream;
      while ((scream = inFromUser.readLine()) != null) {
        outToServer.writeBytes(scream + "\n");
        // sentence = inFromServer.readLine();
        // System.out.println("S: " + sentence);

      }
    } catch (IOException e) {
      System.out.println("Exception while connecting to server: " + e.getMessage());
    }

  }
}
