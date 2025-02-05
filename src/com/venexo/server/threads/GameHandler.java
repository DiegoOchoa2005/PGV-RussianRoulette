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
  private DataInputStream playerInput;
  private DataOutputStream playerOutput;

  public GameHandler(ArrayList<Player> players, DataInputStream playerInput, DataOutputStream playerOutput) {
    this.players = players;
    this.playerCount = players.size();
    this.round = 0;
    this.shootgunFakeBullets = 0;
    this.shootgunRealBullets = 0;
    this.playerInput = playerInput;
    this.playerOutput = playerOutput;
  }

  private void rulesExplication() throws IOException {
    for (Player player : players) {
      player.getCommand().writeUTF("Hello " + player.getName() + "!\n");
      player.getCommand().writeUTF(
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
      player.getCommand().flush();
    }
  }

  private void waitConsoleTime(int miliseconds) {
    try {
      Thread.sleep(miliseconds);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private void roundStart() {
    this.round++;
    fillShootgun();
    this.waitConsoleTime(2000);
    System.out.println("WELCOME TO ROUND " + this.round + "!");
    System.out.println("THERE ARE " + this.playerCount + " PLAYERS ALIVE!");
    System.out.printf("\nTHE SHOOTGUN HAS:\nFAKE BULLETS: %d \nREAL BULLETS: %d\n",
        this.shootgunFakeBullets,
        this.shootgunRealBullets);
    this.waitConsoleTime(3000);
    this.generateRandomStarterPlayerTurn();

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
    System.out.println("🎉 PLAYER: " + this.players.get(this.currentPlayer).getName() + " IS THE STARTER!");
  }

  private void actualPlayerMessageOptions() {
    try {
      this.players.get(this.currentPlayer).getCommand().writeUTF("""
          IT'S YOUR TURN! WHAT DO YOU WANT TO DO?
          - PLAYER: To choose a player to shoot.
          - MYSELF: To shoot yourself.
          Be careful, maybe you could die! :D
          """);
      this.players.get(this.currentPlayer).getCommand().flush();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void playerAction(String action) {
    switch (action.toLowerCase()) {
      case "player":

        break;
      case "myself":
      default:
        try {
          this.players.get(this.currentPlayer).getCommand().writeUTF("Please choose a valid action!");
          this.players.get(this.currentPlayer).getCommand().flush();
        } catch (Exception e) {
          // TODO: handle exception
        }
        break;
    }
  }

  @Override
  public void run() {
    try {
      rulesExplication();
      roundStart();
      for (int i = 0; i < 4; i++) {
        generateRandomStarterPlayerTurn();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
