package com.venexo.server.threads;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import com.venexo.players.Player;

public class GameHandler extends Thread {
  private ArrayList<Player> players;
  private int playerCount = 0;
  private int currentPlayer = 0;
  private int round;
  private int shootgunFakeBullets;
  private int shootgunRealBullets;
  private ArrayList<String> shootgunBullets = new ArrayList<>();
  private final int MAX_SLOTS = 8;

  public GameHandler(ArrayList<Player> players) {
    this.players = players;
    this.playerCount = players.size();
    this.round = 0;
    this.shootgunFakeBullets = 0;
    this.shootgunRealBullets = 0;

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
              - Each player has 3 lives.
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

  private void newRoundMessage() {
    for (Player player : players) {
      try {
        player.getMessage().writeUTF("WELCOME TO ROUND " + this.round + "!");
        player.getMessage().writeUTF("THERE ARE " + this.playerCount + " PLAYERS ALIVE!");
        player.getMessage()
            .writeUTF("\nTHE SHOOTGUN HAS:\nFAKE BULLETS: " + this.shootgunFakeBullets + " \nREAL BULLETS: "
                + this.shootgunRealBullets + "\n");
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
            .writeUTF("🎉 PLAYER: " + this.players.get(this.currentPlayer).getName() + " IS THE STARTER!");
        player.getMessage().flush();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

  }

  private void prepareRound() {
    this.round++;
    fillShootgun();
  }

  private void announceRound() {
    this.waitConsoleTime(2000);
    this.newRoundMessage();
  }

  private void startTurn() {
    this.waitConsoleTime(3000);
    this.generateRandomStarterPlayerTurn();
    this.waitConsoleTime(1500);
    this.starterPlayerMessage();
    this.waitConsoleTime(1500);
  }

  private void roundStart() {
    prepareRound();
    announceRound();
    startTurn();
    playerAction();
  }

  private void randomRealBullets() {
    this.shootgunRealBullets = 1 + (int) (Math.random() * (MAX_SLOTS - 4) + 1);
  }

  private void randomFakeBullets() {
    this.shootgunFakeBullets = 1 + (int) (Math.random() * (MAX_SLOTS - 4) + 1);
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
    System.out.println(shootgunBullets);
  }

  private void generateRandomStarterPlayerTurn() {
    int randomPlayer = (int) (Math.random() * this.playerCount) + 1;
    this.currentPlayer = randomPlayer == 1 ? 0 : 1;
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

  private void playerAction() {
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

    switch (action.toLowerCase()) {
      case "player":
        System.out.println("PLAYER: Choose a player to shoot.");
        break;
      case "myself":
        System.out.println("PLAYER: Chooses to shoot himself.");
        break;
    }
  }

  @Override
  public void run() {
    try {
      rulesExplication();
      while (true) {
        roundStart();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
