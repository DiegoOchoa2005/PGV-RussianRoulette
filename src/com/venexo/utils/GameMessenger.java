package com.venexo.utils;

import java.io.IOException;
import java.util.ArrayList;

import com.venexo.players.Player;

public class GameMessenger {
  private RoundManager roundManager;

  public GameMessenger(RoundManager roundManager) {
    this.roundManager = roundManager;
  }

  public void showTimeToStartARound(int miliseconds, RoundManager roundManager) {
    try {
      int seconds = miliseconds / 1000;
      int isFirstRound = roundManager.getRound() == 0 ? 1 : roundManager.getRound();
      for (int i = seconds; i > 0; i--) {
        String countdownMessage = "\rTHE ROUND " + isFirstRound + " WILL START IN " + i + "";

        for (Player player : this.roundManager.getPlayers()) {
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

      ConsoleHandler.clearConsole(this.roundManager.getPlayers());
    } catch (Exception e) {
      e.printStackTrace();

    }
  }

  public void rulesExplication() throws IOException {
    for (Player player : this.roundManager.getPlayers()) {
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

  public void finishRoundMessage(ArrayList<Player> players) {
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

  public void starterPlayerMessage() {
    for (Player player : this.roundManager.getPlayers()) {
      try {
        player.getMessage()
            .writeUTF("PLAYER: " + this.roundManager.getPlayers().get(this.roundManager.getCurrentPlayer()).getName()
                + " IS THE STARTER!");
        player.getMessage().flush();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

  }

  public void playerNextTurnMessage() {
    for (Player player : this.roundManager.getPlayers()) {
      try {
        player.getMessage().writeUTF(
            "IS " + this.roundManager.getPlayers().get(this.roundManager.getCurrentPlayer()).getName() + "'s TURN!");
        player.getMessage().flush();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public void showActualRoundMessage() {
    int isFirstRound = roundManager.getRound() == 0 ? 1 : this.roundManager.getRound();
    for (Player player : this.roundManager.getPlayers()) {
      try {
        player.getMessage().writeUTF("ROUND: " + isFirstRound);
        player.getMessage().flush();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
