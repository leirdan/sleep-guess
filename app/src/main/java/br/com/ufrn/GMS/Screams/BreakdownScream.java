package br.com.ufrn.GMS.Screams;

import br.com.ufrn.GMS.Enums.GMSState;

public record BreakdownScream(String guess) implements IScream {

  @Override
  public GMSState getType() {
    return GMSState.BREAKDOWN;
  }

  @Override
  public String toString() {
    return "GUESS " + this.guess;
  }

}
