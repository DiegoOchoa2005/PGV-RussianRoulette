package com.venexo.players;

import java.io.DataOutputStream;

public class Player {
  private String name;
  private DataOutputStream command;
  private boolean isAlive;

  public Player(String name, DataOutputStream command, boolean isAlive) {
    this.name = name;
    this.command = command;
    this.isAlive = isAlive;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public DataOutputStream getCommand() {
    return command;
  }

  public void setCommand(DataOutputStream command) {
    this.command = command;
  }

  public boolean isAlive() {
    return isAlive;
  }

  public void setAlive(boolean alive) {
    isAlive = alive;
  }
}
