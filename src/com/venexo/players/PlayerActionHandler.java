package com.venexo.players;

import java.io.IOException;

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
              .writeUTF("Please choose a valid action!");
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
            .setPlayerShooted(this.roundManager.getPlayers().get(this.roundManager.calcualteNextPlayer()).getName());
        this.shootAction(shootgun, action);
        break;
      case "myself":
        this.roundManager
            .setPlayerShooted(this.roundManager.getPlayers().get(this.roundManager.getCurrentPlayer()).getName());
        this.shootAction(shootgun, action);
        break;
    }

    this.roundManager.checkIfPlayerIsDeath();
    if (this.roundManager.isGameOver()) {
      return;
    }
  }

  private void announceShoot(String playerName, String bullet) {
    for (Player player : this.roundManager.getPlayers()) {
      try {
        player.getMessage()
            .writeUTF(playerName + " SHOOTS A " + bullet + " BULLET TO " + this.roundManager.getPlayerShooted());
        player.getMessage().flush();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private void shootAction(Shotgun shootgun, String actionDisplayed) {
    int playerAffected = actionDisplayed.equalsIgnoreCase("myself") ? this.roundManager.getCurrentPlayer()
        : this.roundManager.calcualteNextPlayer();
    String shooterName = this.roundManager.getPlayers().get(this.roundManager.getCurrentPlayer()).getName();
    System.out.println(playerAffected);
    for (String bullet : shootgun.getShootgunBullets()) {
      if (!bullet.equals("EMPTY")) {
        if (bullet.equals("FAKE")) {
          this.announceShoot(shooterName, bullet);
          shootgun.getShootgunBullets().set(shootgun.getShootgunBullets().indexOf(bullet), "EMPTY");
          shootgun.calculateCurrentFakeBullets();

          if (actionDisplayed.equalsIgnoreCase("player")) {
            this.roundManager.setCurrentPlayer(this.roundManager.calcualteNextPlayer());
          }
          return;
        }

        if (bullet.equals("REAL")) {
          this.announceShoot(shooterName, bullet);
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
