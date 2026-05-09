package br.com.ufrn.Server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import org.javatuples.Pair;

import br.com.ufrn.GMS.GMSParser;
import br.com.ufrn.GMS.GMSSession;
import br.com.ufrn.GMS.Screams.IScream;

public class GameServer {
  public static void main(String[] args) {

    try (ServerSocket serverSocket = new ServerSocket(4242)) {
      while (true) {
        Socket connSocket = serverSocket.accept();
        // Criando 1 thread pra cada jogador é possível ter múltiplas partidas
        // simultaneamente
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
        outToClient.writeBytes("Comando identificado com sucesso: ");
        outToClient.writeBytes(result.getValue0().toString() + " \n");
      }

      // clientSentence = clientSentence.toUpperCase() + "\n";
      // outToClient.writeBytes(clientSentence);
    }

  }
}
