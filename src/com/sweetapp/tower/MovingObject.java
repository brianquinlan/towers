package com.sweetapp.tower;

import android.graphics.PointF;

public abstract class MovingObject {

	public abstract PointF getCell();

	abstract void advanceTime(float t);
}
