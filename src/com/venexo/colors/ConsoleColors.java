package com.venexo.colors;

public class ConsoleColors {
  public static final String ANSI_RESET = "\u001B[0m";
  public static final String ANSI_RED = "\u001B[31m";
  public static final String ANSI_GREEN = "\u001B[32m";
  public static final String ANSI_YELLOW = "\u001B[33m";
  public static final String ANSI_BLUE = "\u001B[34m";
  public static final String ANSI_CYAN = "\u001B[36m";
  public static final String BOLD = "\u001B[1m";
  public static final String ANSI_ORANGE = "\u001B[38;5;208m";

  public static String changeColor(String message, String color) {
    return color + message + ANSI_RESET;
  }

  public static String changeBoldColor(String message, String color) {
    return BOLD + color + message + ANSI_RESET;
  }
}
