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
  // This is multiplied with chances,
  // so a 1st try is 15 points, 2nd is 10 and so it goes ..
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

      return new GMSReverb(GMSStatusCode.INTRO_SUCCESS, "OK. Welcome to our challenge, " + this.username + ".");
    });

  }

  private GMSReverb handleGuessScream(GuessScream scream) {
    return this.withState(SessionState.BREAKDOWN, () -> {
      boolean isGuessRight = Objects.equals(SongsManager.getSong(this.currentSong).getValue0().toLowerCase(),
          scream.guess().toLowerCase());

      if (isGuessRight) {
        this.score += this.chances * this.pointsPerSuccess;
        this.chances = 3; // Reseta
        this.pastSongs.add(this.currentSong);
        this.currentSong = SongsManager.nextSong(this.pastSongs);
        if (this.currentSong == -1) {
          // Acabou o jogo
          this.state = SessionState.OUTRO;
          return new GMSReverb(GMSStatusCode.BREAKDOWN_GAME_WIN,
              "OK. You won the game! Congrats, you got " + this.score + " points!");
        }

        return new GMSReverb(GMSStatusCode.BREAKDOWN_GUESS_SUCCESS,
            "OK. Now you have " + this.score + " points. Get ready for the next one!");
      } else {
        this.chances--;
        if (this.chances != 0) {
          return new GMSReverb(GMSStatusCode.BREAKDOWN_GUESS_ERROR,
              "ERROR. You have more " + this.chances + " chances.");
        } else {
          return new GMSReverb(GMSStatusCode.BREAKDOWN_GAME_OVER,
              "ERROR. You lost all chances. You got a score of " + this.score + " points.");
        }

      }
    });
  }

  private GMSReverb handleBreakScream() {
    return this.withState(SessionState.BREAKDOWN, () -> {
      this.state = SessionState.OUTRO;
      return new GMSReverb(GMSStatusCode.OUTRO_SUCCESS, "OK. You had a score of " + this.score + " points.");
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

}
