package com.sweetapp.tower;

import android.graphics.PointF;

public abstract class Projectile {
	public abstract void advanceTime(float time);
	public abstract PointF getCell();
	public abstract boolean hasHit();
	public abstract float getDamage();
}
