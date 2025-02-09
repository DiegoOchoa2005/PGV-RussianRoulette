package com.venexo.utils;

public class Rules {
  public final static String[] RULES = {
      "There's gonna be just one shotgun for all of you.",
      "Per each round the shotgun is gonna have random fake or real bullets.",
      "That doesn't mean that the shotgun is full always, it just means that the shotgun could have 2 - 10 bullets if its the case.",
      "The player's turn to use the shotgun is random when is a new round.",
      "If a player shoots a real or fake bullet to the other one, the turn is finished.",
      "If the player shoots himself a real bullet, the turn is finished.",
      "If the player shoots himself a fake bullet, the turn will restart and let the player shoot again.",
      "The round will end when the shotgun is empty or when one player is dead.",
      "Each player has 3 lives.",
      "The game will end when one player is dead.",
      "Good luck :D"
  };

  public static String getRule(int ruleNumber) {
    return RULES[ruleNumber];
  }
}
