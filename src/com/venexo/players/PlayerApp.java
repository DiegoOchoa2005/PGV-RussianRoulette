package com.venexo.players;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

import com.venexo.players.threads.ServerListener;
import com.venexo.utils.Constants;

public class PlayerApp {
  public static void main(String[] args) throws Exception {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Introduce your beatiful name: ");
    String name = scanner.nextLine();
    Socket socket = new Socket("localhost", Constants.SERVER_PORT);

    DataOutputStream outputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

    outputStream.writeUTF(name);
    outputStream.flush();

    DataInputStream inputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

    ServerListener serverListenerThread = new ServerListener(inputStream);
    serverListenerThread.start();

    while (true) {
      String action = scanner.nextLine();

      outputStream.writeUTF(action);
      outputStream.flush();

      if (socket.isClosed()) {
        scanner.close();
        socket.close();
      }
    }

  }

}
