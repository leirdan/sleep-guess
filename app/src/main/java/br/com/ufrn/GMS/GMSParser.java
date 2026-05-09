package br.com.ufrn.GMS;

import org.javatuples.Pair;

import br.com.ufrn.GMS.Screams.BreakdownScream;
import br.com.ufrn.GMS.Screams.IScream;
import br.com.ufrn.GMS.Screams.IntroScream;
import br.com.ufrn.GMS.Screams.OutroScream;

public class GMSParser {
  public Pair<IScream, String> parse(String line) {
    String[] parts = line.split(" ");

    switch (parts[0]) {
      case "INTRO" -> {
        if (parts.length != 3)
          return Pair.with(null, "INTRO scream requires USERNAME and DIFFICULTY.");

        try {
          return Pair.with(new IntroScream(parts[1], parts[2]), null);
        } catch (IllegalArgumentException e) {
          return Pair.with(null, e.getMessage());
        }
      }
      case "GUESS" -> {
        if (parts.length != 2)
          return Pair.with(null, "GUESS scream requires 1 argument.");

        return Pair.with(new BreakdownScream(parts[1]), null);
      }
      case "BREAK" -> {
        if (parts.length != 1)
          return Pair.with(null, "BREAK scream does not require parameters.");

        return Pair.with(new OutroScream(), null);
      }
      default -> {
        return Pair.with(null, "Unknown scream '" + parts[0] + "'");
      }
    }
  }
}
