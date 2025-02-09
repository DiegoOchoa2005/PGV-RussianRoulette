package com.venexo.players;

import java.io.IOException;

import com.venexo.colors.ConsoleColors;
import com.venexo.guns.Shotgun;
import com.venexo.utils.RoundManager;

public class PlayerActionHandler {
  private RoundManager roundManager;

  public PlayerActionHandler(RoundManager roundManager) {
    this.roundManager = roundManager;
  }

  private String actualPlayerOption() {
    try {
      this.roundManager.messageOptions();
      while (this.roundManager.getPlayers().get(this.roundManager.getCurrentPlayer()).getInput().available() == 0) {
        Thread.sleep(100);
      }
      return this.roundManager.getPlayers().get(this.roundManager.getCurrentPlayer()).getInput().readUTF();
    } catch (IOException e) {
      e.printStackTrace();
      return "";
    } catch (InterruptedException e) {
      e.printStackTrace();
      return "";
    }
  }

  private String handlePlayerInput() {
    String action = "";
    while (true) {
      action = actualPlayerOption();
      if (action.equalsIgnoreCase("player") || action.equalsIgnoreCase("myself")) {
        break;
      } else {
        try {
          this.roundManager.getPlayers().get(this.roundManager.getCurrentPlayer()).getMessage()
              .writeUTF(ConsoleColors.changeBoldColor("\nPlease choose a valid action!\n", ConsoleColors.ANSI_RED));
          this.roundManager.getPlayers().get(this.roundManager.getCurrentPlayer()).getMessage().flush();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return action;
  }

  public void playerAction(Shotgun shootgun) {
    if (this.roundManager.isGameOver()) {
      return;
    }
    String action = this.handlePlayerInput().toLowerCase();

    switch (action) {
      case "player":
        this.roundManager
            .setPlayerShooted(ConsoleColors.changeBoldColor(
                this.roundManager.getPlayers().get(this.roundManager.calcualteNextPlayer()).getName(),
                ConsoleColors.ANSI_YELLOW));
        this.shootAction(shootgun, action);
        break;
      case "myself":
        this.roundManager
            .setPlayerShooted(ConsoleColors.changeBoldColor(
                this.roundManager.getPlayers().get(this.roundManager.getCurrentPlayer()).getName(),
                ConsoleColors.ANSI_YELLOW));
        this.shootAction(shootgun, action);
        break;
    }

    this.roundManager.checkIfPlayerIsDeath();
    if (this.roundManager.isGameOver()) {
      return;
    }
  }

  private void announceShoot(String shooterName, String bullet, String actionDisplayed) {
    String bulletName = bullet.equals("FAKE")
        ? ConsoleColors.changeBoldColor(bullet + " BULLET TO ", ConsoleColors.ANSI_BLUE)
        : ConsoleColors.changeBoldColor(bullet + " BULLET TO ", ConsoleColors.ANSI_RED);
    String shotHimself = actionDisplayed.equalsIgnoreCase("myself")
        ? ConsoleColors.changeBoldColor("HIMSELF", ConsoleColors.ANSI_YELLOW)
        : ConsoleColors.changeBoldColor(this.roundManager.getPlayerShooted(), ConsoleColors.ANSI_YELLOW);

    for (Player player : this.roundManager.getPlayers()) {
      try {
        player.getMessage()
            .writeUTF("\n" + shooterName
                + ConsoleColors.changeBoldColor(" SHOOTS A " + bulletName, ConsoleColors.ANSI_ORANGE)
                + shotHimself);
        player.getMessage().flush();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private void shootAction(Shotgun shootgun, String actionDisplayed) {
    int playerAffected = actionDisplayed.equalsIgnoreCase("myself") ? this.roundManager.getCurrentPlayer()
        : this.roundManager.calcualteNextPlayer();
    String shooterName = ConsoleColors.changeBoldColor(
        this.roundManager.getPlayers().get(this.roundManager.getCurrentPlayer()).getName(), ConsoleColors.ANSI_YELLOW);

    for (String bullet : shootgun.getShootgunBullets()) {
      if (!bullet.equals("EMPTY")) {
        if (bullet.equals("FAKE")) {
          this.announceShoot(shooterName, bullet, actionDisplayed);
          shootgun.getShootgunBullets().set(shootgun.getShootgunBullets().indexOf(bullet), "EMPTY");
          shootgun.calculateCurrentFakeBullets();

          if (actionDisplayed.equalsIgnoreCase("player")) {
            this.roundManager.setCurrentPlayer(this.roundManager.calcualteNextPlayer());
          }
          return;
        }

        if (bullet.equals("REAL")) {
          this.announceShoot(shooterName, bullet, actionDisplayed);
          this.roundManager.getPlayers().get(playerAffected).getShot();
          shootgun.getShootgunBullets().set(shootgun.getShootgunBullets().indexOf(bullet), "EMPTY");
          shootgun.calculateCurrentRealBullets();
          this.roundManager.setCurrentPlayer(this.roundManager.calcualteNextPlayer());
          return;
        }
      }
    }

  }
}
