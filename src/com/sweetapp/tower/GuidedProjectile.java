package com.sweetapp.tower;

import android.graphics.PointF;

public class GuidedProjectile extends Projectile {
  private PointF cell;
  private final Foe target;
  private float angle;
  private final float speed;
  private final float maxRateOfTurn;
  private final float damage;

  GuidedProjectile(Pitch pitch, Foe target, float x, float y, float angle, float speed, float damage) {
    this.target = target;
    this.cell = new PointF(x, y);
    this.angle = angle;
    this.speed = speed;
    this.damage = damage;

    maxRateOfTurn = 45;
  }

  @Override
  public PointF getCell() {
    return cell;
  }

  float getAngle() {
    return angle;
  }

  Foe getTarget() {
    return target;
  }

  @Override
  public boolean hasHit() {
    final PointF targetP = target.getCell();
    targetP.offset(-cell.x, -cell.y);
    return targetP.length() < 0.5;
  }

  @Override
  public void advanceTime(float t) {
    final float relativeBearing = Trig.getRelativeBearing(
        getCell(),
        getAngle(),
        target.getCell());

    if (relativeBearing < 0) {
      angle += Math.max(-maxRateOfTurn * t, relativeBearing);
    } else {
      angle += Math.min(maxRateOfTurn * t, relativeBearing);
    }

    this.cell = new PointF(cell.x + (float) Math.sin(Math.toRadians(angle)) * speed * t,
        cell.y + (float) Math.cos(Math.toRadians(angle)) * speed * t);
  }

  @Override
  public float getDamage() {
    return damage;
  }
}
