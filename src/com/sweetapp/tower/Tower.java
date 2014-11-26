package com.sweetapp.tower;

import android.graphics.Point;

public class Tower {
	public final Point gridLocation;
	
	public Tower(int x, int y) {
		this.gridLocation = new Point(x, y);
	}

	void advanceTime(float t) {
	}
}
