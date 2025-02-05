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
    ServerSocket serverSocket = new ServerSocket(Constants.SERVER_PORT);

    ArrayList<Player> players = new ArrayList<>();

    while (players.size() < 2) {
      System.out.println("Waiting for players...");
      Socket clientSocket = serverSocket.accept();
      DataInputStream playerInput = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
      DataOutputStream serverOutput = new DataOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));

      String playerName = playerInput.readUTF();

      System.out.println("Player " + playerName + " joined the game!");
      Player player = new Player(clientSocket, playerName, playerInput, serverOutput);
      players.add(player);

      serverOutput.writeUTF("Welcome " + playerName + "!");
      serverOutput.flush();
      System.out.println(players.size());
    }
    System.out.println("🎉 ¡Two players joined the game! Starting game...");

    for (Player player : players) {
      player.getMessage().writeUTF("🚀 The game is going to start. Get ready!\n");
      player.getMessage().flush();
    }

    try {
      Thread.sleep(3000);
      new GameHandler(players).start();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}