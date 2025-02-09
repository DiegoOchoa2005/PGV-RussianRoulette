package com.venexo.server.threads;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import com.venexo.players.Player;

public class GameHandler extends Thread {
  private ArrayList<Player> players;
  private int playerCount = 0;
  private int currentPlayer = 0;
  private String deadPlayer = "";
  private String playerShooted = "";
  private int round;
  private int shootgunFakeBullets;
  private int shootgunRealBullets;
  private ArrayList<String> shootgunBullets = new ArrayList<>();
  private final int MAX_SLOTS = 8;
  private boolean isRoundStarted;
  private boolean isGameOver;

  public GameHandler(ArrayList<Player> players) {
    this.players = players;
    this.playerCount = players.size();
    this.round = 0;
    this.shootgunFakeBullets = 0;
    this.shootgunRealBullets = 0;
    this.isRoundStarted = false;
    this.isGameOver = false;
  }

  private void rulesExplication() throws IOException {
    for (Player player : players) {
      player.getMessage().writeUTF("Hello " + player.getName() + "!\n");
      player.getMessage().writeUTF(
          """
              Rules are simple:
              - There's gonna be just one shotgun for all of you.
              - Per each round the shotgun is gonna have random fake or real bullets.
              - That doesn't mean that the shotgun is full always, it just means that the shotgun could have 2 - 8 bullets if its the case.
              - The player's turn to use the shotgun is random when is a new round.
              - If the player shoots to the other player or himself, the turn will be passed to the next player.
              - The round will end when the shotgun is empty.
              - Each player has 9 lives.
              - The game will end when one player is dead.
              - Good luck :D
              """);
      player.getMessage().flush();
    }
  }

  private void waitConsoleTime(int miliseconds) {
    try {
      Thread.sleep(miliseconds);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private void announceCurrentBullets() {
    try {
      for (Player player : players) {
        player.getMessage()
            .writeUTF("THE SHOOTGUN HAS:\nFAKE BULLETS: " + this.shootgunFakeBullets + " \nREAL BULLETS: "
                + this.shootgunRealBullets);
        player.getMessage().flush();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void finishRoundMessage() {
    for (Player player : players) {
      try {
        player.getMessage().writeUTF("THE ROUND HAS FINISHED!");
        player.getMessage().writeUTF("Get ready for the next round!");
        player.getMessage().flush();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private void starterPlayerMessage() {
    for (Player player : players) {
      try {
        player.getMessage()
            .writeUTF("PLAYER: " + this.players.get(this.currentPlayer).getName() + " IS THE STARTER!");
        player.getMessage().flush();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

  }

  private void prepareRound() {
    this.isRoundStarted = true;
    this.round++;
    fillShootgun();
  }

  private void announceRound() {
    if (this.round > 1) {
      this.showTimeToStartARound(3000);
    }
    this.showActualRoundMessage();
    this.waitConsoleTime(1000);
    this.announcePlayersCurrentLives();
    this.announceCurrentBullets();
  }

  private void startTurn() {
    this.waitConsoleTime(1000);
    this.generateRandomStarterPlayerTurn();
    this.waitConsoleTime(1500);
    this.starterPlayerMessage();
    this.waitConsoleTime(1500);
  }

  private void roundStart() {
    prepareRound();
    announceRound();
    startTurn();
  }

  private void roundFinish() {
    this.isRoundStarted = false;
    this.checkIfPlayerIsDeath();
  }

  private void randomRealBullets() {
    this.shootgunRealBullets = 1 + (int) (Math.random() * (MAX_SLOTS - 4) + 1);
  }

  private void randomFakeBullets() {
    this.shootgunFakeBullets = 1 + (int) (Math.random() * (MAX_SLOTS - 4) + 1);
  }

  private void calculateCurrentRealBullets() {
    if (this.shootgunRealBullets < 0) {
      this.shootgunRealBullets = 0;
      return;
    }
    this.shootgunRealBullets--;
  }

  private void calculateCurrentFakeBullets() {
    if (this.shootgunFakeBullets < 0) {
      this.shootgunFakeBullets = 0;
      return;
    }
    this.shootgunFakeBullets--;
  }

  private void generateBullets() {
    randomRealBullets();
    randomFakeBullets();
  }

  private void fillShootgun() {
    generateBullets();

    shootgunBullets.clear();

    for (int i = 0; i < this.shootgunFakeBullets; i++) {
      this.shootgunBullets.add("FAKE");
    }

    for (int i = 0; i < this.shootgunRealBullets; i++) {
      this.shootgunBullets.add("REAL");
    }

    if (this.shootgunBullets.size() < this.MAX_SLOTS) {
      for (int i = 0; i <= this.MAX_SLOTS - this.shootgunBullets.size(); i++) {
        this.shootgunBullets.add("EMPTY");
      }
    }
    Collections.shuffle(this.shootgunBullets);
  }

  private void generateRandomStarterPlayerTurn() {
    int randomPlayer = (int) (Math.random() * this.playerCount) + 1;
    this.currentPlayer = randomPlayer == 1 ? 0 : 1;
  }

  private int calcualteNextPlayer() {
    return this.currentPlayer == 0 ? 1 : 0;
  }

  private void playerNextTurnMessage() {
    for (Player player : players) {
      try {
        player.getMessage().writeUTF("IS " + this.players.get(this.currentPlayer).getName() + "'s TURN!");
        player.getMessage().flush();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private void messageOptions() {
    try {
      this.players.get(this.currentPlayer).getMessage().writeUTF("""
          IT'S YOUR TURN! WHAT DO YOU WANT TO DO?
          - PLAYER: To choose a player to shoot.
          - MYSELF: To shoot yourself.
          Be careful, maybe you could die! :D
          """);
      this.players.get(this.currentPlayer).getMessage().flush();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private String actualPlayerOption() {
    try {
      messageOptions();
      while (this.players.get(this.currentPlayer).getInput().available() == 0) {
        Thread.sleep(100);
      }
      return this.players.get(this.currentPlayer).getInput().readUTF();
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
          this.players.get(this.currentPlayer).getMessage().writeUTF("Please choose a valid action!");
          this.players.get(this.currentPlayer).getMessage().flush();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return action;
  }

  private void playerAction() {
    if (isGameOver) {
      return;
    }
    String action = handlePlayerInput().toLowerCase();

    switch (action) {
      case "player":
        this.playerShooted = this.players.get(this.calcualteNextPlayer()).getName();
        this.shootAction(action);
        break;
      case "myself":
        this.playerShooted = this.players.get(this.currentPlayer).getName();
        this.shootAction(action);
        break;
    }

    this.checkIfPlayerIsDeath();
    if (isGameOver) {
      return;
    }
  }

  private void announceShoot(String playerName, String bullet) {
    for (Player player : players) {
      try {
        player.getMessage()
            .writeUTF(playerName + " SHOOTS A " + bullet + " BULLET TO " + this.playerShooted);
        player.getMessage().flush();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private void shootAction(String actionDisplayed) {
    int playerAffected = actionDisplayed.equalsIgnoreCase("myself") ? this.currentPlayer : this.calcualteNextPlayer();
    String shooterName = this.players.get(this.currentPlayer).getName();
    System.out.println(playerAffected);
    for (String bullet : this.shootgunBullets) {
      if (!bullet.equals("EMPTY")) {
        if (bullet.equals("FAKE")) {
          this.announceShoot(shooterName, bullet);
          this.shootgunBullets.set(this.shootgunBullets.indexOf(bullet), "EMPTY");
          this.calculateCurrentFakeBullets();

          if (actionDisplayed.equalsIgnoreCase("player")) {
            this.currentPlayer = this.calcualteNextPlayer();
          }
          return;
        }

        if (bullet.equals("REAL")) {
          this.announceShoot(shooterName, bullet);
          this.players.get(playerAffected).getShot();
          this.shootgunBullets.set(this.shootgunBullets.indexOf(bullet), "EMPTY");
          this.calculateCurrentRealBullets();
          this.currentPlayer = this.calcualteNextPlayer();
          return;
        }
      }
    }

  }

  private void showActualRoundMessage() {
    int isFirstRound = this.round == 0 ? 1 : this.round;
    for (Player player : players) {
      try {
        player.getMessage().writeUTF("ROUND: " + isFirstRound);
        player.getMessage().flush();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private boolean isShotgunEmpty() {
    ArrayList<String> emptySlots = new ArrayList<>(shootgunBullets);
    boolean isEmpty = false;

    for (String bullet : emptySlots) {
      if (bullet.equals("EMPTY")) {
        isEmpty = true;
      } else {
        isEmpty = false;
        break;
      }
    }
    return isEmpty;
  }

  private void announceDeathPlayer(String playerName) {
    for (Player player : players) {
      try {
        player.getMessage().writeUTF("PLAYER: " + playerName + " IS DEAD!");
        player.getMessage().flush();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private void checkIfPlayerIsDeath() {
    for (int i = 0; i < players.size(); i++) {
      if (!players.get(i).isAlive()) {
        this.isGameOver = true;
        this.deadPlayer = players.get(i).getName();
        return;
      }
    }
  }

  private void announcePlayersCurrentLives() {
    for (Player player : players) {
      try {
        player.getMessage().writeUTF(this.players.get(0).getName() + "'s LIVES: "
            + this.players.get(0).getLives());
        player.getMessage().writeUTF(this.players.get(1).getName() + "'s LIVES: "
            + this.players.get(1).getLives());
        player.getMessage().flush();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private void showTimeToStartARound(int miliseconds) {
    try {
      int seconds = miliseconds / 1000;
      int isFirstRound = this.round == 0 ? 1 : this.round;
      for (int i = seconds; i > 0; i--) {
        String countdownMessage = "\rTHE ROUND " + isFirstRound + " WILL START IN " + i + "";

        for (Player player : this.players) {
          try {
            player.getMessage().writeUTF(countdownMessage);
            player.getMessage().flush();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }

        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }

      this.clearConsole();
    } catch (Exception e) {
      e.printStackTrace();

    }
  }

  private void announceWinner() {
    Player winner = this.players.get(0);
    for (Player player : players) {
      try {
        player.getMessage().writeUTF("THE GAME IS OVER! The winner is: " + winner.getName());
        player.getMessage().flush();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private void clearConsole() {
    for (Player player : players) {
      try {
        player.getMessage().writeUTF("\033[H\033[2J");
        player.getMessage().writeUTF("\033[H\033[2J");

      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void run() {
    try {
      rulesExplication();
      this.showTimeToStartARound(15000);
      while (!this.isGameOver) {

        roundStart();
        playerAction();
        this.waitConsoleTime(2000);
        this.clearConsole();
        if (this.isGameOver) {
          break;
        }
        this.showActualRoundMessage();
        this.announcePlayersCurrentLives();
        this.announceCurrentBullets();
        while (this.isRoundStarted && !this.isGameOver) {
          this.waitConsoleTime(2000);
          playerNextTurnMessage();
          this.waitConsoleTime(2000);
          playerAction();
          this.waitConsoleTime(2000);
          this.clearConsole();
          if (this.isGameOver) {
            break;
          }
          if (!this.isShotgunEmpty()) {
            this.showActualRoundMessage();
            this.announcePlayersCurrentLives();
            this.announceCurrentBullets();
          } else {
            this.waitConsoleTime(2000);
            this.clearConsole();
            this.finishRoundMessage();
            this.waitConsoleTime(2000);
            this.roundFinish();
          }
        }
      }
      if (this.isGameOver) {
        this.waitConsoleTime(2000);
        this.announceDeathPlayer(deadPlayer);
        this.waitConsoleTime(1000);
        this.announceWinner();
        this.waitConsoleTime(1000);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
