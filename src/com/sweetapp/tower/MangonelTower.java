package com.sweetapp.tower;

import java.util.Random;

import android.graphics.PointF;

public class MangonelTower extends Tower {
	float fireRate;
	float timeToNextShot;
	Pitch pitch;
	Random random = new Random();

	public MangonelTower(Pitch pitch, int x, int y, float fireRate) {
		super(x, y);
		this.pitch = pitch;
		this.fireRate = fireRate;
	}

	void a(float x, float y) {
		for (int i = 0; i < 8; ++i) {
			pitch.newMangonelRock(
					gridLocation,
			        new PointF(x + (float) random.nextGaussian()/3f,
					           y + (float) random.nextGaussian()/3f),
					Math.max(1.5f, 3 + (float) random.nextGaussian() / 3f));			
		}
	}

	void advanceTime(float t) {
		if (t > timeToNextShot) {
			Foe g = pitch.closestBadGuy(gridLocation);
			if (g != null) {
				a(g.getCell().x, g.getCell().y);
				timeToNextShot = fireRate;
			} else {
				timeToNextShot = 0;
			}
		} else {
				timeToNextShot -= t;
		}
	}
}
