package com.venexo.guns;

import java.util.ArrayList;
import java.util.Collections;

public class Shotgun {
  private int shootgunFakeBullets;
  private int shootgunRealBullets;
  private ArrayList<String> shootgunBullets = new ArrayList<>();
  private final int MAX_SLOTS = 8;

  public Shotgun() {
    this.shootgunFakeBullets = 0;
    this.shootgunRealBullets = 0;
  }

  public int getShootgunFakeBullets() {
    return shootgunFakeBullets;
  }

  public void setShootgunFakeBullets(int shootgunFakeBullets) {
    this.shootgunFakeBullets = shootgunFakeBullets;
  }

  public int getShootgunRealBullets() {
    return shootgunRealBullets;
  }

  public void setShootgunRealBullets(int shootgunRealBullets) {
    this.shootgunRealBullets = shootgunRealBullets;
  }

  private void randomRealBullets() {
    this.shootgunRealBullets = 1 + (int) (Math.random() * (MAX_SLOTS - 4) + 1);
  }

  private void randomFakeBullets() {
    this.shootgunFakeBullets = 1 + (int) (Math.random() * (MAX_SLOTS - 4) + 1);
  }

  public void calculateCurrentRealBullets() {
    if (this.shootgunRealBullets < 0) {
      this.shootgunRealBullets = 0;
      return;
    }
    this.shootgunRealBullets--;
  }

  public void calculateCurrentFakeBullets() {
    if (this.shootgunFakeBullets < 0) {
      this.shootgunFakeBullets = 0;
      return;
    }
    this.shootgunFakeBullets--;
  }

  public void generateBullets() {
    randomRealBullets();
    randomFakeBullets();
  }

  public void fillShootgun() {
    generateBullets();

    shootgunBullets.clear();

    for (int i = 0; i < this.shootgunFakeBullets; i++) {
      this.shootgunBullets.add("FAKE");
    }

    for (int i = 0; i < this.shootgunRealBullets; i++) {
      this.shootgunBullets.add("REAL");
    }

    if (this.shootgunBullets.size() < this.MAX_SLOTS) {
      for (int i = 0; i <= this.MAX_SLOTS - this.shootgunBullets.size(); i++) {
        this.shootgunBullets.add("EMPTY");
      }
    }
    Collections.shuffle(this.shootgunBullets);
  }

  public boolean isShotgunEmpty() {
    ArrayList<String> emptySlots = new ArrayList<>(shootgunBullets);
    boolean isEmpty = false;

    for (String bullet : emptySlots) {
      if (bullet.equals("EMPTY")) {
        isEmpty = true;
      } else {
        isEmpty = false;
        break;
      }
    }
    return isEmpty;
  }

  public ArrayList<String> getShootgunBullets() {
    return shootgunBullets;
  }
}
