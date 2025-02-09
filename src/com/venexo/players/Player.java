package com.venexo.players;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class Player {
  private Socket socket;

  private String name;
  private DataOutputStream message;
  private DataInputStream input;
  private boolean isAlive;
  private int lives;

  public Player(Socket socket, String name, DataInputStream input, DataOutputStream message) {
    this.socket = socket;
    this.name = name;
    this.input = input;
    this.message = message;
    this.isAlive = true;
    this.lives = 3;
  }

  public String getName() {
    return this.name;
  }

  public DataOutputStream getMessage() {
    return this.message;
  }

  public DataInputStream getInput() {
    return this.input;
  }

  public boolean isAlive() {
    return this.isAlive;
  }

  public void setAlive(boolean alive) {
    this.isAlive = alive;
  }

  public int getLives() {
    return this.lives;
  }

  public void setLives(int lives) {
    this.lives = lives;
  }

  public Socket getSocket() {
    return this.socket;
  }

  public void getShot() {
    this.lives--;
    if (this.lives <= 0) {
      this.isAlive = false;
    }
  }
}
