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
import com.venexo.utils.Constants;

public class ServerApp {
  public static void main(String[] args) throws IOException {
    boolean gameStarted = false;
    ServerSocket serverSocket = new ServerSocket(Constants.SERVER_PORT);

    ArrayList<Player> players = new ArrayList<>();

    Socket clientSocket = serverSocket.accept();
    DataInputStream playerInput = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
    DataOutputStream playerOutput = new DataOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));

    String playerName = playerInput.readUTF();
    System.out.println("Player " + playerName + " connected");

    Player player = new Player(playerName, playerOutput, true);
    players.add(player);

    System.out.println(players.size());

    if (players.size() >= 2 && players.size() < 3 && !gameStarted) {
      gameStarted = true;
    } else {
      System.out.println("Waiting for 2 players");
    }
  }
}