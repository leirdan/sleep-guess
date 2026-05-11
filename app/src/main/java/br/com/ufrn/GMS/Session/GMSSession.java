package br.com.ufrn.GMS.Session;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.function.Supplier;

import org.javatuples.Pair;

import br.com.ufrn.GMS.Enums.SessionState;
import br.com.ufrn.GMS.Reverbs.GMSReverb;
import br.com.ufrn.GMS.Screams.BreakScream;
import br.com.ufrn.GMS.Screams.GuessScream;
import br.com.ufrn.GMS.Screams.HelpScream;
import br.com.ufrn.GMS.Screams.IScream;
import br.com.ufrn.GMS.Screams.IntroScream;
import br.com.ufrn.GMS.Utils.GMSStatusCode;
import br.com.ufrn.GMS.Utils.LyricFormatter;
import br.com.ufrn.Server.Enums.GameDifficulty;

public class GMSSession {
  private SessionState state;
  private String username;
  private Integer score;
  private Long currentSong;
  private Integer chances;
  private GameDifficulty difficulty;

  private final LyricFormatter formatter = new LyricFormatter();
  private final Random random = new Random();
  private final Integer pointsPerSuccess = 5;
  private final Set<Long> pastSongs;

  public GMSSession() {
    this.state = SessionState.INTRO;
    this.score = 0;
    this.chances = 3;
    this.pastSongs = new HashSet<>();
  }

  public GMSReverb handleScream(IScream scream) {
    switch (scream) {
      case IntroScream intro -> {
        return this.handleIntroScream(intro);
      }
      case BreakScream _b -> {
        return this.handleBreakScream();
      }
      case GuessScream guess -> {
        return this.handleGuessScream(guess);
      }
      case HelpScream _h -> {
        return this.handleHelpScream();
      }
      default -> {
        return new GMSReverb(GMSStatusCode.UNKNOWN_SCREAM, "ERROR. Unknown scream.");
      }
    }
  }

  public String getNextLine() {
    Pair<String, List<String>> songInfo = SongsManager.getSong(this.currentSong);
    List<String> lines = songInfo.getValue1();

    String randomLine = lines.get(random.nextInt(lines.size()));

    return formatter.parse(randomLine, this.difficulty);
  }

  private GMSReverb handleIntroScream(IntroScream scream) {
    return this.withState(SessionState.INTRO, () -> {
      this.difficulty = scream.getDifficulty();
      this.username = scream.getUsername();
      this.state = SessionState.BREAKDOWN;
      this.currentSong = SongsManager.nextSong(pastSongs);

      return new GMSReverb(GMSStatusCode.INTRO_SUCCESS,
          "OK. Welcome to BREAKDOWN stage, " + this.username + ". Here it comes the first one!");
    });

  }

  private GMSReverb handleHelpScream() {
    return new GMSReverb(GMSStatusCode.HELP, """
          Commands:
            - INTRO: Starts a match by providing username and desired difficulty. Usage: INTRO [username] [difficulty]
            - GUESS: Guess a song during the match. USAGE: GUESS [song]
            - BREAK: Ends the match at any time and closes client's connection. USAGE: BREAK
            - HELP: Displays this section. USAGE: HELP
        """);
  }

  private GMSReverb handleGuessScream(GuessScream scream) {
    return this.withState(SessionState.BREAKDOWN, () -> {
      boolean isGuessRight = Objects.equals(SongsManager.getSong(this.currentSong).getValue0().toLowerCase(),
          scream.guess().toLowerCase());

      if (isGuessRight) {
        this.score += this.chances * this.pointsPerSuccess;
        this.chances = 3;
        this.pastSongs.add(this.currentSong);
        this.currentSong = SongsManager.nextSong(this.pastSongs);
        if (this.currentSong == -1) {
          this.state = SessionState.OUTRO;
          return new GMSReverb(GMSStatusCode.BREAKDOWN_GAME_WIN,
              "OK. Congrats, you won the game. " + this.getResults());
        }

        return new GMSReverb(GMSStatusCode.BREAKDOWN_GUESS_SUCCESS,
            "OK. Now you got a total score of " + this.score + ". Here it comes the song "
                + (this.pastSongs.size() + 1) + "!");
      } else {
        this.chances--;
        if (this.chances != 0) {
          return new GMSReverb(GMSStatusCode.BREAKDOWN_GUESS_ERROR,
              "ERROR. You have " + this.chances + " more chances.");
        } else {
          return new GMSReverb(GMSStatusCode.BREAKDOWN_GAME_OVER,
              "ERROR. You lost all chances. " + this.getResults());
        }

      }
    });
  }

  private GMSReverb handleBreakScream() {
    return this.withState(SessionState.BREAKDOWN, () -> {
      this.state = SessionState.OUTRO;

      return new GMSReverb(GMSStatusCode.OUTRO_SUCCESS, "OK. " + this.getResults());
    });
  }

  private GMSReverb withState(SessionState expected, Supplier<GMSReverb> expectedAction) {
    if (this.state != expected) {
      return new GMSReverb(
          GMSStatusCode.INVALID_STATE_ERROR,
          "ERROR. You are not allowed to perform this command during " + this.state + " state.");
    }

    return expectedAction.get();
  }

  private String getResults() {
    String temp;
    StringBuilder sb = new StringBuilder();
    sb.append("You got a score of ").append(this.score)
        .append(" points and these are the songs you correctly guessed:\n");
    for (var pastSong : this.pastSongs) {
      temp = "- " + SongsManager.getSong(pastSong).getValue0();
      sb.append(temp).append("\n");
    }
    return sb.toString();
  }

}
