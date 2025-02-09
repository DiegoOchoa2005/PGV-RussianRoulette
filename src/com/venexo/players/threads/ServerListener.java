package com.venexo.players.threads;

import java.io.DataInputStream;
import java.io.IOException;

import com.venexo.colors.ConsoleColors;

public class ServerListener extends Thread {
  private DataInputStream inputStream;

  public ServerListener(DataInputStream inputStream) {
    this.inputStream = inputStream;
  }

  @Override
  public void run() {
    while (true) {
      try {
        String serverMessage = this.inputStream.readUTF();
        System.out.print(serverMessage);
      } catch (IOException e) {
        System.out
            .println(ConsoleColors.changeBoldColor("\nConexión con el servidor perdida.", ConsoleColors.ANSI_RED));
        break;
      }
    }
  }
}
