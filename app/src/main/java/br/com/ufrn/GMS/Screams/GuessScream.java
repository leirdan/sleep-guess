package br.com.ufrn.GMS.Screams;

import br.com.ufrn.GMS.Enums.ScreamType;

public record GuessScream(String guess) implements IScream {

  public GuessScream(String guess) {
    this.guess = guess.toLowerCase();
  }

  @Override
  public ScreamType getType() {
    return ScreamType.GUESS;
  }

  @Override
  public String toString() {
    return "GUESS " + this.guess;
  }

}
