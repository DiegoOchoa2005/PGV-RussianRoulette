package com.venexo.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.venexo.players.Player;
import com.venexo.server.threads.GameHandler;
import com.venexo.utils.Constants;

public class ServerApp {
  public static void main(String[] args) throws IOException {
    boolean gameStarted = false;
    ServerSocket serverSocket = new ServerSocket(Constants.SERVER_PORT);
    DataInputStream playerInput = null;
    DataOutputStream serverOutput = null;
    ArrayList<Player> players = new ArrayList<>();

    while (players.size() < 2) {
      System.out.println("Waiting for players...");
      Socket clientSocket = serverSocket.accept();
      playerInput = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
      serverOutput = new DataOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));

      String playerName = playerInput.readUTF();

      System.out.println("Player " + playerName + " joined the game!");
      Player player = new Player(playerName, serverOutput, true);
      players.add(player);

      serverOutput.writeUTF("Welcome " + playerName + "!");
      serverOutput.flush();
      System.out.println(players.size());
    }
    System.out.println("🎉 ¡Two players joined the game! Starting game...");

    for (Player player : players) {
      player.getCommand().writeUTF("🚀 The game is going to start. Get ready!\n");
      player.getCommand().flush();
    }

    try {
      Thread.sleep(3000);
      new GameHandler(players, playerInput, serverOutput).start();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}