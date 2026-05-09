package br.com.ufrn.GMS;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

import org.javatuples.Pair;

import br.com.ufrn.GMS.Enums.GMSState;
import br.com.ufrn.GMS.Reverbs.GMSReverb;
import br.com.ufrn.GMS.Screams.BreakdownScream;
import br.com.ufrn.GMS.Screams.IScream;
import br.com.ufrn.GMS.Screams.IntroScream;
import br.com.ufrn.Server.Enums.GameDifficulty;
import br.com.ufrn.Server.SongsManager;

public class GMSSession {
  private GMSState state;
  private String username;
  private Integer score;
  private Long currentSong;
  private Integer currentSongLine;
  private Integer chances;
  private GameDifficulty difficulty;

  // This is multiplied with chances,
  // so a 1st try is 15 points, 2nd is 10 and so it goes ..
  private final Integer pointsPerSuccess = 5;
  private final List<Long> pastSongs;

  public GMSSession() {
    this.state = GMSState.INTRO;
    this.score = 0;
    this.chances = 3;
    this.pastSongs = new ArrayList<>();
  }

  public GMSReverb handleScream(IScream scream) {
    switch (scream.getType()) {
      case GMSState.INTRO -> {
        return this.handleIntroScream((IntroScream) scream);
      }
      case GMSState.OUTRO -> {
        return this.handleOutroScream();
      }
      case GMSState.BREAKDOWN -> {
        return this.handleBreakdownScream((BreakdownScream) scream);
      }
      default -> {
        return new GMSReverb(GMSStatusCode.UNKNOWN_SCREAM, "ERROR. Unknown scream.");
      }
    }
  }

  public String getNextLine() {
    Pair<String, List<String>> song = SongsManager.getData().get(this.currentSong);
    String line = song.getValue1().get(this.currentSongLine);

    return switch (difficulty) {
      case FAN -> "LYRICS " + this.hideCharacters(line);
      case TRVE -> "LYRICS " + this.removeVowels(line);
      default -> "LYRICS " + line;
    };
  }

  private GMSReverb handleIntroScream(IntroScream scream) {
    if (this.state != GMSState.INTRO) {
      return new GMSReverb(GMSStatusCode.INVALID_STATE_ERROR,
          "ERROR. You are not allowed to perform this command during Intro state.");
    }

    this.difficulty = scream.getDifficulty();
    this.username = scream.getUsername();
    this.state = GMSState.BREAKDOWN; // avançar né

    // Selecionar aleatoriamente a música a ser adivinhada.
    this.getNextSong();

    return new GMSReverb(GMSStatusCode.INTRO_SUCCESS, "OK. Welcome to our challenge, " + this.username + "..");
  }

  private GMSReverb handleBreakdownScream(BreakdownScream scream) {
    if (this.state == GMSState.INTRO) {
      return new GMSReverb(GMSStatusCode.INVALID_STATE_ERROR,
          "ERROR. You are not allowed to perform this command during Intro state.");
    } else if (this.state != GMSState.BREAKDOWN) {
      return new GMSReverb(GMSStatusCode.INVALID_STATE_ERROR,
          "ERROR. You are not allowed to perform this command during Breakdown state.");
    }

    var breakdownScream = (BreakdownScream) scream;
    var guess = breakdownScream.guess();

    boolean isGuessRight = Objects.equals(SongsManager.getData().get(currentSong).getValue0().toLowerCase(),
        guess.toLowerCase());

    if (isGuessRight) {
      this.pastSongs.add(this.currentSong);
      this.score += this.chances * this.pointsPerSuccess;
      this.chances = 3; // Reseta
      this.getNextSong();

      return new GMSReverb(GMSStatusCode.BREAKDOWN_GUESS_SUCCESS,
          "OK. Now you have " + this.score + " points. Get ready for the next one!");
    } else {
      this.chances--;

      if (this.chances != 0) {
        this.advanceSongLine();
        return new GMSReverb(GMSStatusCode.BREAKDOWN_GUESS_ERROR,
            "ERROR. You have more " + this.chances + " chances.");
      } else {
        return new GMSReverb(GMSStatusCode.BREAKDOWN_GAME_OVER,
            "ERROR. You lost all chances.\n You had a score of " + this.score + " points.");
      }

    }
  }

  private GMSReverb handleOutroScream() {
    if (this.state == GMSState.INTRO) {
      return new GMSReverb(GMSStatusCode.INVALID_STATE_ERROR,
          "ERROR. You are not allowed to perform this command during Intro State.");
    }

    this.state = GMSState.OUTRO;
    return new GMSReverb(GMSStatusCode.OUTRO_SUCCESS, "OK. You had a score of " + this.score + " points.");
  }

  private void getNextSong() {
    Random random = new Random();
    Long songIdx = random.nextLong(SongsManager.getData().size());

    while (this.pastSongs.contains(songIdx)) {
      songIdx = random.nextLong(SongsManager.getData().size());
    }

    this.currentSong = songIdx + 1;
    this.currentSongLine = random.nextInt(SongsManager.getData().get(this.currentSong).getValue1().size());
  }

  private void advanceSongLine() {
    Integer songLine = this.currentSongLine;
    while (Objects.equals(songLine, this.currentSongLine)) {
      songLine = new Random().nextInt(SongsManager.getData().get(this.currentSong).getValue1().size());
    }
    this.currentSongLine = songLine + 1;
  }

  private String hideCharacters(String str) {
    Random random = new Random();
    StringBuilder sb = new StringBuilder();
    for (char ch : str.toCharArray()) {
      if (Character.isWhitespace(ch) || !Character.isLetterOrDigit(ch)) {
        sb.append(ch);
        continue;
      }
      boolean shouldHide = random.nextDouble() < 0.5;
      if (shouldHide) {
        sb.append("-");
      } else {
        sb.append(ch);
      }
    }

    return sb.toString();
  }

  private String removeVowels(String str) {
    StringBuilder sb = new StringBuilder();
    Set<Character> vowels = Set.of('a', 'e', 'i', 'o', 'u', 'A', 'E', 'I', 'O', 'U');

    for (char ch : str.toCharArray()) {
      if (!vowels.contains(ch)) {
        sb.append(ch);
      }
    }

    return sb.toString();
  }
}
