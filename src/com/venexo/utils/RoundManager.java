package com.venexo.utils;

import java.io.IOException;
import java.util.ArrayList;

import com.venexo.colors.ConsoleColors;
import com.venexo.guns.Shotgun;
import com.venexo.players.Player;

public class RoundManager {
  private int round;
  private boolean isRoundStarted;
  private int currentPlayer = 0;
  private int playerCount;
  private String deadPlayer;
  private GameMessenger gameMessenger;
  private ArrayList<Player> players;
  private boolean isGameOver;
  private String playerShooted = "";

  public RoundManager(ArrayList<Player> players) {
    this.isRoundStarted = false;
    this.round = 0;
    this.playerCount = players.size();
    this.deadPlayer = "";
    this.playerCount = players.size();
    this.isGameOver = false;
    this.gameMessenger = new GameMessenger(this);
    this.players = players;
  }

  private void prepareRound(Shotgun shootgun) {
    this.isRoundStarted = true;
    this.round++;
    shootgun.fillShootgun();
  }

  private void announceRound(Shotgun shootgun) {
    if (this.round > 1) {
      this.gameMessenger.showTimeToStartARound(3000, this);
    }
    this.gameMessenger.showActualRoundMessage();
    ConsoleHandler.waitConsoleTime(1000);
    this.announcePlayersCurrentLives();
    this.announceCurrentBullets(shootgun);
  }

  private void startTurn() {
    ConsoleHandler.waitConsoleTime(1000);
    this.generateRandomStarterPlayerTurn();
    ConsoleHandler.waitConsoleTime(1500);
    gameMessenger.starterPlayerMessage();
    ConsoleHandler.waitConsoleTime(1500);
  }

  public void roundStart(Shotgun shootgun) {
    prepareRound(shootgun);
    announceRound(shootgun);
    startTurn();
  }

  public void roundFinish() {
    this.isRoundStarted = false;
    this.checkIfPlayerIsDeath();
  }

  private void generateRandomStarterPlayerTurn() {
    int randomPlayer = (int) (Math.random() * this.playerCount) + 1;
    this.currentPlayer = randomPlayer == 1 ? 0 : 1;
  }

  public void setCurrentPlayer(int currentPlayer) {
    this.currentPlayer = currentPlayer;
  }

  public void checkIfPlayerIsDeath() {
    for (int i = 0; i < players.size(); i++) {
      if (!players.get(i).isAlive()) {
        this.isGameOver = true;
        this.deadPlayer = players.get(i).getName();
        return;
      }
    }
  }

  public void announceCurrentBullets(Shotgun shootgun) {
    try {
      for (Player player : players) {
        player.getMessage()
            .writeUTF(ConsoleColors.changeBoldColor("\nTHE SHOTGUN HAS:\n", ConsoleColors.ANSI_ORANGE)
                + ConsoleColors.changeBoldColor("\nFAKE BULLETS: " + String.valueOf(shootgun.getShootgunFakeBullets()),
                    ConsoleColors.ANSI_BLUE)
                + ConsoleColors.changeBoldColor(" \nREAL BULLETS: "
                    + shootgun.getShootgunRealBullets() + "\n", ConsoleColors.ANSI_RED));
        player.getMessage().flush();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void announcePlayersCurrentLives() {
    for (Player player : players) {
      try {
        player.getMessage().writeUTF(ConsoleColors.changeBoldColor("\n" + this.players.get(0).getName() + "'s LIVES: "
            + this.players.get(0).getLives() + "\n", ConsoleColors.ANSI_GREEN));
        player.getMessage().writeUTF(ConsoleColors.changeBoldColor("\n" + this.players.get(1).getName() + "'s LIVES: "
            + this.players.get(1).getLives() + "\n", ConsoleColors.ANSI_GREEN));
        player.getMessage().flush();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public void announceDeathPlayer() {
    for (Player player : players) {
      try {
        player.getMessage()
            .writeUTF(ConsoleColors.BOLD + ConsoleColors.ANSI_YELLOW + "\nPLAYER: " + ConsoleColors.ANSI_ORANGE
                + this.deadPlayer + ConsoleColors.changeBoldColor(" IS DEAD!\n", ConsoleColors.ANSI_RED));
        player.getMessage().flush();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public void announceWinner() {
    Player winner = this.players.get(0);
    for (Player player : players) {
      try {
        player.getMessage()
            .writeUTF(ConsoleColors.BOLD + ConsoleColors.ANSI_RED + "\nTHE GAME IS OVER!" + ConsoleColors.ANSI_GREEN
                + "\nThe winner is: " + ConsoleColors.changeBoldColor(winner.getName(), ConsoleColors.ANSI_ORANGE)
                + "\n");
        player.getMessage().flush();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public void messageOptions() {
    try {
      this.players.get(this.currentPlayer).getMessage().writeUTF(ConsoleColors.changeBoldColor("""
          \nIT'S YOUR TURN! WHAT DO YOU WANT TO DO?
          - PLAYER: To shoot your oponent.
          - MYSELF: To shoot yourself.
          Be careful, maybe you could die! :D\n
          """, ConsoleColors.ANSI_CYAN));
      this.players.get(this.currentPlayer).getMessage().flush();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public int getCurrentPlayer() {
    return currentPlayer;
  }

  public int getPlayerCount() {
    return playerCount;
  }

  public String getDeadPlayer() {
    return deadPlayer;
  }

  public ArrayList<Player> getPlayers() {
    return players;
  }

  public boolean isGameOver() {
    return isGameOver;
  }

  public int calcualteNextPlayer() {
    return this.currentPlayer == 0 ? 1 : 0;
  }

  public boolean isRoundStarted() {
    return isRoundStarted;
  }

  public int getRound() {
    return round;
  }

  public String getPlayerShooted() {
    return playerShooted;
  }

  public void setPlayerShooted(String playerShooted) {
    this.playerShooted = playerShooted;
  }

}
