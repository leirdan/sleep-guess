package br.com.ufrn.GMS.Utils;

import java.util.Random;
import java.util.Set;

import br.com.ufrn.Server.Enums.GameDifficulty;

public class LyricFormatter {
  private final Random random = new Random();

  public String parse(String line, GameDifficulty difficulty) {
    return switch (difficulty) {
      case FAN -> "LYRICS " + this.hideCharacters(line);
      case TRVE -> "LYRICS " + this.removeVowels(line);
      default -> "LYRICS " + line;
    };
  }

  private String hideCharacters(String str) {
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
