package com.sweetapp.tower;

import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;

public class Foe {
	private Pitch pitch;
	private Point lastCell;
	private Point nextCell;

	private float velocity;
	private float destinationFraction;
	float hitPoints;
	float maxHitPoints;

	Foe(Pitch pitch, int x, int y, int hitPoints, float velocity) {
		this.pitch = pitch;
		lastCell = new Point(x, y);
		nextCell = pitch.getNextCell(lastCell);
		this.velocity = velocity;
		this.hitPoints = hitPoints;
		maxHitPoints = hitPoints;
	}

	boolean reduceHitPoints(float hp) {
		hitPoints = Math.max(0f, hitPoints - hp);
		return hitPoints <= 0;
	}

	float getHitPoints() {
		return hitPoints;
	}

	void advanceTime(float t) {
		TerrainGrid.Cell lc = pitch.getTerrainCell(lastCell);
		TerrainGrid.Cell nc = pitch.getTerrainCell(nextCell);
		float f = 1 / (lc.getCost() * (1-destinationFraction) + nc.getCost() * destinationFraction);

		destinationFraction += this.velocity * f * t;
		while (destinationFraction > 1) {
			lastCell = nextCell;
			nextCell = pitch.getNextCell(lastCell);
			if (nextCell == null) {
				nextCell = lastCell;
			}
			Log.e("Foo", nextCell.toString());
			destinationFraction -= 1;
		}
	}

	Point getLastCell() {
		return lastCell;
	}

	Point getNextCell() {
		return nextCell;
	}

	PointF getCell() {
		return new PointF((nextCell.x - lastCell.x) * destinationFraction + lastCell.x,
				          (nextCell.y - lastCell.y) * destinationFraction + lastCell.y);
	}

	public float getMaxHitPoints() {
		return maxHitPoints;
	}
}