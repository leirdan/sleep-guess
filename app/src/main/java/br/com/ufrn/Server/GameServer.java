package br.com.ufrn.Server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import java.util.Optional;

import org.javatuples.Pair;

import br.com.ufrn.GMS.Parser.GMSParser;
import br.com.ufrn.GMS.Reverbs.GMSReverb;
import br.com.ufrn.GMS.Screams.IScream;
import br.com.ufrn.GMS.Session.GMSSession;
import br.com.ufrn.GMS.Session.SongsManager;
import br.com.ufrn.GMS.Utils.GMSStatusCode;

public class GameServer {
  public static void main(String[] args) {

    try (ServerSocket serverSocket = new ServerSocket(4224)) {
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
      System.out.println("Error during game initialization: " + e.getMessage());
    }
  }

  private static void handleNewClient(Socket socket) throws IOException {
    GMSParser parser = new GMSParser();
    GMSSession session = new GMSSession();
    String input;
    var inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    var outToClient = new DataOutputStream(socket.getOutputStream());

    Optional<GMSReverb> initSongs = SongsManager.init();
    if (initSongs.isPresent()) {
      outToClient.writeBytes(initSongs.get().toString() + "\n");
      socket.close();
      return;
    }

    outToClient.writeBytes(
        """
            Sleep Token Guess Game!
            Enter the game with INTRO scream, guess the songs with GUESS screams and, if you are tired, exit with BREAK.
            In case you need some help, scream... HELP.
            """);

    while ((input = inFromClient.readLine()) != null) {
      Pair<IScream, String> result = parser.parse(input);

      if (result.getValue1() != null) {
        outToClient.writeBytes(result.getValue1() + "\n");
      } else {
        GMSReverb reverb = session.handleScream(result.getValue0());
        outToClient.writeBytes(reverb.toString() + "\n");

        if (Objects.equals(reverb.statusCode(), GMSStatusCode.INTRO_SUCCESS)
            || Objects.equals(reverb.statusCode(), GMSStatusCode.BREAKDOWN_GUESS_SUCCESS)
            || Objects.equals(reverb.statusCode(), GMSStatusCode.BREAKDOWN_GUESS_ERROR)) {
          outToClient.writeBytes(session.getNextLine() + "\n");
        } else if (Objects.equals(reverb.statusCode(), GMSStatusCode.BREAKDOWN_GAME_OVER) ||
            Objects.equals(reverb.statusCode(), GMSStatusCode.BREAKDOWN_GAME_WIN) ||
            Objects.equals(reverb.statusCode(), GMSStatusCode.OUTRO_SUCCESS)) {
          socket.close();
          return;
        }
      }
    }
  }
}
