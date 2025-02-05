package com.venexo.players;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Player {
  private String name;
  private DataOutputStream message;
  private DataInputStream input;
  private boolean isAlive;

  public Player(String name, DataOutputStream message, boolean isAlive) {
    this.name = name;
    this.message = message;
    this.isAlive = isAlive;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public DataOutputStream getMessage() {
    return message;
  }

  public void setMessage(DataOutputStream command) {
    this.message = command;
  }

  public boolean isAlive() {
    return isAlive;
  }

  public void setAlive(boolean alive) {
    isAlive = alive;
  }

  public DataInputStream getInput() {
    return input;
  }

  public void setInput(DataInputStream input) {
    this.input = input;
  }
}
