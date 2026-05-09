package br.com.ufrn.GMS.Screams;

import br.com.ufrn.GMS.Enums.GMSState;

public record OutroScream() implements IScream {

  @Override
  public GMSState getType() {
    return GMSState.OUTRO;
  }

  @Override
  public String toString() {
    return "BREAK";
  }
}
