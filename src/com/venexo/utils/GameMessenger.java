package com.venexo.utils;

import java.io.IOException;
import java.util.ArrayList;

import com.venexo.colors.ConsoleColors;
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
        String countdownMessage = ConsoleColors.changeBoldColor("\rTHE ROUND ", ConsoleColors.ANSI_GREEN)
            + ConsoleColors.changeBoldColor(String.valueOf(isFirstRound) + " WILL START IN "
                + i + " ", ConsoleColors.ANSI_GREEN);

        for (Player player : this.roundManager.getPlayers()) {
          try {
            player.getMessage().writeUTF(countdownMessage);
            player.getMessage().flush();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }

        ConsoleHandler.waitConsoleTime(1000);
      }

      ConsoleHandler.clearConsole(this.roundManager.getPlayers());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void rulesExplication() throws IOException {
    for (Player player : this.roundManager.getPlayers()) {
      player.getMessage()
          .writeUTF(ConsoleColors.BOLD + ConsoleColors.ANSI_YELLOW + "\nHello "
              + ConsoleColors.changeBoldColor(player.getName(), ConsoleColors.ANSI_YELLOW) +
              ConsoleColors.changeBoldColor("!\n", ConsoleColors.ANSI_YELLOW));
      player.getMessage().flush();
    }

    for (int i = 0; i < Rules.RULES.length; i++) {
      String ruleMessage = Rules.getRule(i) + "\n";

      for (Player player : this.roundManager.getPlayers()) {
        try {
          player.getMessage().writeUTF(ruleMessage);
          player.getMessage().flush();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      ConsoleHandler.waitConsoleTime(1000);

    }

  }

  public void finishRoundMessage(ArrayList<Player> players) {
    for (Player player : players) {
      try {
        player.getMessage().writeUTF(ConsoleColors.changeBoldColor("THE ROUND HAS FINISHED!", ConsoleColors.ANSI_RED));
        player.getMessage()
            .writeUTF(ConsoleColors.changeBoldColor("\nGet ready for the next round!\n", ConsoleColors.ANSI_GREEN));
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
            .writeUTF(ConsoleColors.BOLD + ConsoleColors.ANSI_YELLOW + "\nPLAYER " +
                ConsoleColors.ANSI_GREEN
                + this.roundManager.getPlayers().get(this.roundManager.getCurrentPlayer()).getName()
                + ConsoleColors.ANSI_YELLOW
                + " IS THE STARTER!\n" + ConsoleColors.ANSI_RESET);
        player.getMessage().flush();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

  }

  public void playerNextTurnMessage() {
    for (Player player : this.roundManager.getPlayers()) {
      try {
        player.getMessage().writeUTF(ConsoleColors.BOLD + ConsoleColors.ANSI_YELLOW +
            "\nIS " + ConsoleColors.ANSI_CYAN
            + this.roundManager.getPlayers().get(this.roundManager.getCurrentPlayer()).getName()
            + ConsoleColors.ANSI_YELLOW + "'s TURN!\n");
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
        player.getMessage()
            .writeUTF(ConsoleColors.changeBoldColor("ROUND: " + isFirstRound + "\n", ConsoleColors.ANSI_ORANGE));
        player.getMessage().flush();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
