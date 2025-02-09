package com.venexo.utils;

import java.util.ArrayList;

import com.venexo.players.Player;

public class ConsoleHandler {
  public static void waitConsoleTime(int miliseconds) {
    try {
      Thread.sleep(miliseconds);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public static void clearConsole(ArrayList<Player> players) {
    for (Player player : players) {
      try {
        player.getMessage().writeUTF("\033[H\033[2J");
        player.getMessage().writeUTF("\033[H\033[2J");

      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
