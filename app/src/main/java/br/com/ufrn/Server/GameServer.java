package br.com.ufrn.Server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

import org.javatuples.Pair;

import br.com.ufrn.GMS.GMSParser;
import br.com.ufrn.GMS.GMSSession;
import br.com.ufrn.GMS.GMSStatusCode;
import br.com.ufrn.GMS.Reverbs.GMSReverb;
import br.com.ufrn.GMS.Screams.IScream;

public class GameServer {
  public static void main(String[] args) {

    try (ServerSocket serverSocket = new ServerSocket(4242)) {
      while (true) {
        Socket connSocket = serverSocket.accept();
        new Thread(() -> {
          try {
            handleNewClient(connSocket);
          } catch (IOException ex) {
            System.getLogger(GameServer.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
          }
        }).start();
      }
    } catch (IOException e) {
      System.out.println("Exception during game initialization: " + e.getMessage());
    }
  }

  private static void handleNewClient(Socket socket) throws IOException {
    GMSParser parser = new GMSParser();
    GMSSession session = new GMSSession();
    String input;
    var inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));

    while ((input = inFromClient.readLine()) != null) {
      var outToClient = new DataOutputStream(socket.getOutputStream());
      Pair<IScream, String> result = parser.parse(input);

      if (result.getValue1() != null) {
        outToClient.writeBytes(result.getValue1() + "\n");
      } else {
        // fazer a integração com a lógica de GMSSession, parece mais tranquilo
        GMSReverb reverb = session.handleScream(result.getValue0());

        outToClient.writeBytes(reverb.toString() + "\n");

        // envia a música
        if (Objects.equals(reverb.statusCode(), GMSStatusCode.INTRO_SUCCESS)
            || Objects.equals(reverb.statusCode(), GMSStatusCode.BREAKDOWN_GUESS_SUCCESS)) {
          outToClient.writeBytes(session.getNextLine() + "\n");
        }
        // anuncia game over
        else if (Objects.equals(reverb.statusCode(), GMSStatusCode.BREAKDOWN_GAME_OVER)) {
          break;
        }
      }
    }
  }
}
