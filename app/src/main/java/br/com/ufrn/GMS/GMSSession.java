package br.com.ufrn.GMS;

import br.com.ufrn.GMS.Enums.GMSState;
import br.com.ufrn.GMS.Reverbs.GMSReverb;
import br.com.ufrn.GMS.Screams.IScream;
import br.com.ufrn.GMS.Screams.IntroScream;
import br.com.ufrn.Server.Enums.GameDifficulty;

public class GMSSession {
  private GMSState state;
  private String username;
  private Integer score;
  private Integer chances;
  private GameDifficulty difficulty;
  private final Integer pointsPerSuccess = 5;
  // This is multiplied with chances,
  // so a 1st try is 15 points, 2nd is 10 and so it goes ..

  public GMSSession() {
    this.state = GMSState.INTRO;
    this.score = 0;
    this.chances = 3;
  }

  public GMSReverb handleScream(IScream scream) {
    switch (scream.getType()) {
      case GMSState.INTRO -> {
        if (this.state != GMSState.INTRO) {
          return new GMSReverb(40, "Oops, some error occurred.. \n");
        }

        var introScream = (IntroScream) scream;
        this.difficulty = introScream.getDifficulty();
        this.username = introScream.getUsername();
        this.state = GMSState.BREAKDOWN; // avançar né
        return new GMSReverb(20, "Okay, welcome to our challenge, " + this.username + ".. \n");
      }
      case GMSState.BREAKDOWN -> {

      }
      // TODO: completar os outros casos
      default -> {
        return new GMSReverb(200, "");
      }
    }
    return new GMSReverb(200, "");
  }

}
