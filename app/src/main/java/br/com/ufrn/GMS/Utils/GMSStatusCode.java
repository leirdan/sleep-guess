package br.com.ufrn.GMS.Utils;

public class GMSStatusCode {
  // Success codes
  public static Integer INTRO_SUCCESS = 20;
  public static Integer BREAKDOWN_GUESS_SUCCESS = 21;
  public static Integer OUTRO_SUCCESS = 22;
  public static Integer HELP = 23;
  public static Integer BREAKDOWN_GAME_WIN = 24;

  // Error codes
  public static Integer INTRO_ERROR = 40;
  public static Integer BREAKDOWN_GUESS_ERROR = 41;
  public static Integer BREAKDOWN_GAME_OVER = 42;

  // Failures codes
  public static Integer INVALID_SYNTAX = 50;
  public static Integer INVALID_STATE_ERROR = 51;
  public static Integer UNKNOWN_SCREAM = 52;
}
