package br.com.ufrn.GMS.Screams;

import br.com.ufrn.GMS.Enums.GMSState;
import br.com.ufrn.Server.Enums.GameDifficulty;

public class IntroScream implements IScream {
  private String username;
  private GameDifficulty difficulty;

  public IntroScream(String username, String difficulty) {
    try {
      this.difficulty = GameDifficulty.valueOf(difficulty.toUpperCase());
      this.username = username;
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Invalid difficulty was provided...");
    }
  }

  @Override
  public GMSState getType() {
    return GMSState.INTRO;
  }

  @Override
  public String toString() {
    return "INTRO " + this.username + " " + this.difficulty.toString();
  }

  public String getUsername() {
    return username;
  }

  public GameDifficulty getDifficulty() {
    return difficulty;
  }
}
