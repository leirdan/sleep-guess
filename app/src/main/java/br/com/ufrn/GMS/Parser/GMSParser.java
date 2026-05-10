package br.com.ufrn.GMS.Parser;

import java.util.Arrays;

import org.javatuples.Pair;

import br.com.ufrn.GMS.Screams.BreakScream;
import br.com.ufrn.GMS.Screams.GuessScream;
import br.com.ufrn.GMS.Screams.HelpScream;
import br.com.ufrn.GMS.Screams.IScream;
import br.com.ufrn.GMS.Screams.IntroScream;
import br.com.ufrn.GMS.Utils.GMSStatusCode;

public class GMSParser {
  public Pair<IScream, String> parse(String line) {
    String[] parts = line.split(" ");

    switch (parts[0]) {
      case "INTRO" -> {
        if (parts.length != 3)
          return Pair.with(null, "[" + GMSStatusCode.INVALID_SYNTAX + "] ERROR. Usage: INTRO [username] [difficulty]");

        try {
          return Pair.with(new IntroScream(parts[1], parts[2]), null);
        } catch (IllegalArgumentException e) {
          return Pair.with(null, e.getMessage());
        }
      }
      case "GUESS" -> {
        if (parts.length < 2)
          return Pair.with(null, "[" + GMSStatusCode.INVALID_SYNTAX + "] ERROR. Usage: GUESS [song]");

        String song = String.join(" ", Arrays.copyOfRange(parts, 1, parts.length));

        return Pair.with(new GuessScream(song), null);
      }
      case "BREAK" -> {
        if (parts.length != 1)
          return Pair.with(null, "[" + GMSStatusCode.INVALID_SYNTAX + "] ERROR. Usage: BREAK");

        return Pair.with(new BreakScream(), null);
      }
      case "HELP" -> {
        if (parts.length != 1)
          return Pair.with(null, "[" + GMSStatusCode.INVALID_SYNTAX + "] ERROR. Usage: HELP");

        return Pair.with(new HelpScream(), null);
      }
      default -> {
        return Pair.with(null, "[" + GMSStatusCode.UNKNOWN_SCREAM + "] ERROR. Unknown scream '" + parts[0] + "'");
      }
    }
  }
}
