package com.venexo.server.threads;

import java.io.IOException;
import java.util.ArrayList;
import com.venexo.guns.Shotgun;
import com.venexo.players.Player;
import com.venexo.players.PlayerActionHandler;
import com.venexo.utils.ConsoleHandler;
import com.venexo.utils.GameMessenger;
import com.venexo.utils.RoundManager;

public class GameHandler extends Thread {
  private Shotgun shootgun = new Shotgun();
  private RoundManager roundManager;
  private PlayerActionHandler playerActionHandler;
  private GameMessenger gameMessenger;

  public GameHandler(ArrayList<Player> players) {
    this.roundManager = new RoundManager(players);
    this.playerActionHandler = new PlayerActionHandler(this.roundManager);
    this.gameMessenger = new GameMessenger(this.roundManager);
  }

  @Override
  public void run() {
    try {
      this.gameMessenger.rulesExplication();
      this.gameMessenger.showTimeToStartARound(15000, this.roundManager);
      while (!this.roundManager.isGameOver()) {

        this.roundManager.roundStart(this.shootgun);
        this.playerActionHandler.playerAction(this.shootgun);
        ConsoleHandler.waitConsoleTime(2000);
        ConsoleHandler.clearConsole(this.roundManager.getPlayers());
        if (this.roundManager.isGameOver()) {
          break;
        }
        this.gameMessenger.showActualRoundMessage();
        this.roundManager.announcePlayersCurrentLives();
        this.roundManager.announceCurrentBullets(this.shootgun);
        while (this.roundManager.isRoundStarted() && !this.roundManager.isGameOver()) {
          ConsoleHandler.waitConsoleTime(2000);
          this.gameMessenger.playerNextTurnMessage();
          ConsoleHandler.waitConsoleTime(2000);
          this.playerActionHandler.playerAction(this.shootgun);
          ConsoleHandler.waitConsoleTime(2000);
          ConsoleHandler.clearConsole(this.roundManager.getPlayers());
          if (this.roundManager.isGameOver()) {
            break;
          }
          if (!this.shootgun.isShotgunEmpty()) {
            this.gameMessenger.showActualRoundMessage();
            this.roundManager.announcePlayersCurrentLives();
            this.roundManager.announceCurrentBullets(this.shootgun);
          } else {
            ConsoleHandler.waitConsoleTime(2000);
            ConsoleHandler.clearConsole(this.roundManager.getPlayers());
            this.gameMessenger.finishRoundMessage(this.roundManager.getPlayers());
            ConsoleHandler.waitConsoleTime(2000);
            this.roundManager.roundFinish();
          }
        }
      }
      if (this.roundManager.isGameOver()) {
        ConsoleHandler.waitConsoleTime(2000);
        this.roundManager.announceDeathPlayer();
        ConsoleHandler.waitConsoleTime(1000);
        this.roundManager.announceWinner();
        ConsoleHandler.waitConsoleTime(1000);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
