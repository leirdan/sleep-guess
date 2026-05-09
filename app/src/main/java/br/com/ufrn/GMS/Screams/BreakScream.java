package br.com.ufrn.GMS.Screams;

import br.com.ufrn.GMS.Enums.ScreamType;

public record BreakScream() implements IScream {

  @Override
  public ScreamType getType() {
    return ScreamType.BREAK;
  }

  @Override
  public String toString() {
    return "BREAK";
  }
}
