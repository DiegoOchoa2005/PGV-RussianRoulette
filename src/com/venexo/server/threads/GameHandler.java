package com.venexo.server.threads;

import java.util.ArrayList;

import com.venexo.players.Player;

public class GameHandler extends Thread {
  private ArrayList<Player> players;
  private int playerCount = 0;
  private int currentPlayer = 0;
  private int round;
  private int shootgunFakeBullets;
  private int shootgunRealBullets;
  private int shootgunBulletSlots = 8;

  public GameHandler(ArrayList<Player> players) {
    this.players = players;
    this.playerCount = players.size();
    this.round = 0;
    this.shootgunFakeBullets = 0;
    this.shootgunRealBullets = 0;
  }

  private void roundStart() {
    this.round++;
    generateRandomFakeBullets();
    generateRandomRealBullets();
    System.out.println("WELCOME TO ROUND " + this.round + "!");
    System.out.println("THERE ARE " + this.playerCount + " PLAYERS ALIVE!");
    System.out.printf("\nTHE SHOOTGUN HAS:\nFAKE BULLETS: %d \nREAL BULLETS: %d\nGOOD LUCK :D\n",
        this.shootgunFakeBullets,
        this.shootgunRealBullets);
  }

  private void generateRandomFakeBullets() {
    this.shootgunFakeBullets = (int) (Math.random() * 4);
  }

  private void generateRandomRealBullets() {
    this.shootgunRealBullets = (int) (Math.random() * 4);
  }
}
