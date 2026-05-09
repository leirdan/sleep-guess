package br.com.ufrn.GMS.Enums;

import java.util.Objects;

public enum ScreamType {
  INTRO,
  GUESS,
  BREAK;

  public boolean equals(String other) {
    var lhs = this.toString().toLowerCase();
    var rhs = other.toLowerCase();
    return Objects.equals(lhs, rhs);
  }
}
