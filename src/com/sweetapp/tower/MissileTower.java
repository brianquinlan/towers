package com.sweetapp.tower;

public class MissileTower extends Tower {
  float fireRate;
  float timeToNextShot;
  Pitch pitch;

  public MissileTower(Pitch pitch, int x, int y, float fireRate) {
    super(x, y);
    this.pitch = pitch;
    this.fireRate = fireRate;
  }

  @Override
  void advanceTime(float t) {
    if (t > timeToNextShot) {
      final Foe g = pitch.closestBadGuy(gridLocation);
      if (g != null) {
        pitch.newMissile(gridLocation, g);
        timeToNextShot = fireRate;
      } else {
        timeToNextShot = 0;
      }
    } else {
      timeToNextShot -= t;
    }
  }
}