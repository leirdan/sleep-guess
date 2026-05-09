package br.com.ufrn.GMS.Enums;

import java.util.Objects;

public enum GMSState {
  INTRO,
  BREAKDOWN,
  OUTRO;

  public boolean equals(String other) {
    var lhs = this.toString().toLowerCase();
    var rhs = other.toLowerCase();
    return Objects.equals(lhs, rhs);
  }
}
