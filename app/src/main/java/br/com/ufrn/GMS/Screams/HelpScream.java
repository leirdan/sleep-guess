package br.com.ufrn.GMS.Screams;

import br.com.ufrn.GMS.Enums.ScreamType;

public record HelpScream() implements IScream {

  @Override
  public ScreamType getType() {
    return ScreamType.HELP;
  }

}
