package com.sweetapp.tower;

import android.graphics.PointF;

final public class Trig {
  static public float getTrueBearing(PointF from, PointF to) {
    final float dx = to.x - from.x;
    final float dy = to.y - from.y;

    // TODO(bquinlan): Normalize?
    return (float) Math.toDegrees(Math.atan2(dx, dy));
  }

  static public float getRelativeBearing(float fromAngle, float toAngle) {
    return normalizeAngle(toAngle - fromAngle);
  }

  static public float getRelativeBearing(PointF from, float fromAngle, PointF to) {
    return getRelativeBearing(fromAngle, getTrueBearing(from, to));
  }

  static public float normalizeAngle(float angle) {
    angle = angle % 360f;

    if (angle >= 180f) {
      angle -= 360f;
    } else if (angle < -180) {
      angle += 360f;
    }

    return angle;
  }

  static public float distance(PointF p1, PointF p2) {
    return PointF.length(p1.x - p2.x, p1.y - p2.y);
  }

  static public PointF translatePoint(float x, float y, float angle, float distance) {
    return new PointF(
        x + (float) Math.sin(Math.toRadians(angle)) * distance,
        y + (float) Math.cos(Math.toRadians(angle)) * distance);
  }

  static public PointF translatePoint(PointF point, float angle, float distance) {
    return translatePoint(point.x, point.y, angle, distance);
  }
}
