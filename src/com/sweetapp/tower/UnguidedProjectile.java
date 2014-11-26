package com.sweetapp.tower;

import android.graphics.PointF;

public class UnguidedProjectile extends Projectile {
	private PointF startCell;
	private PointF targetCell;
	private float speed;
	private float damage;
	private float splashRadius;
	private float destinationFraction;
	public final float distanceToTarget;

	public UnguidedProjectile(PointF cell, PointF target, float speed, float damage, float splashRadius) {
		startCell = cell;
		targetCell = target;
		this.speed = speed;
		this.damage = damage;
		this.splashRadius = splashRadius;
		this.destinationFraction = 0f;
		distanceToTarget = Trig.distance(cell, target);
	}

	public float getDestinationFraction() {
		return destinationFraction;
	}

	@Override
	public PointF getCell() {
		return new PointF(
				(targetCell.x - startCell.x) * destinationFraction + startCell.x,
		        (targetCell.y - startCell.y) * destinationFraction + startCell.y);
	}

	@Override
	public boolean hasHit() {
		return destinationFraction >= 1f;
	}

	@Override
	public float getDamage() {
		return damage;
	}

	public float getSplashRadius() {
		return splashRadius;
	}

	@Override
	public void advanceTime(float t) {
		destinationFraction = Math.min(1f, destinationFraction + (speed * t) / distanceToTarget);
	}
}
