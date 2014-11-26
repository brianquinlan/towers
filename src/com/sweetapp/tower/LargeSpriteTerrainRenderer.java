package com.sweetapp.tower;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

public class LargeSpriteTerrainRenderer {
	/*
	private final int FOREST = 0;
	private final int ROAD = 1;
	private final int WATER = 2;	
	private final int MOUNTAIN = 3;	
	private final int GRASS = 4;	

	final GameRenderer coordinateMapper;
	final Context context;
	
	private Sprite backgroundSprite = null;

	public LargeSpriteTerrainRenderer(Context context, Pitch pitch, GameRenderer coordinateMapper) {
		this.coordinateMapper = coordinateMapper;
		this.context = context;
	}

	public void init(GL10 gl) {
		Point p = powerOfTwoDimensions(logicalWidth, logicalHeight);
		Bitmap bitmap = Bitmap.createBitmap(p.x, p.y, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        for (TerrainGrid.Cell cell : pitch.getVisibleTerrain()) {
			Rect r = cellToCanvasRect(cell.getGridPosition());
			Bitmap bitmap2;
			if (cell instanceof TerrainGrid.ForestCell) {
				bitmap2 = Sprite.loadBitmap(context, R.drawable.trees);
			} else if (cell instanceof TerrainGrid.RoadCell) {
				bitmap2 = Sprite.loadBitmap(context, R.drawable.horizontal_road);
			} else if (cell instanceof TerrainGrid.WaterCell) {
				bitmap2 = Sprite.loadBitmap(context, R.drawable.water);
			} else if (cell instanceof TerrainGrid.MountainCell) {
				bitmap2 = Sprite.loadBitmap(context, R.drawable.mountain);
			} else {
				bitmap2 = Sprite.loadBitmap(context, R.drawable.grass);
			}
			canvas.drawBitmap(bitmap2, new Rect(0, 0, bitmap2.getWidth(), bitmap2.getHeight()), r, null);
		}

		int[] textures = new int[1];
        gl.glGenTextures(1, textures, 0);
        // XXX Crashes with cell size 256
        backgroundSprite = Sprite.loadSprite(gl, textures[0], bitmap, logicalWidth, logicalHeight);
	}

	public void drawTerrain(GL10 gl, Pitch pitch) {
        for (TerrainGrid.Cell cell : pitch.getVisibleTerrain()) {
			Rect r = coordinateMapper.cellToRect(cell.getGridPosition());
			//r.offset(-viewOffsetX, -viewOffsetY);
			if (cell instanceof TerrainGrid.ForestCell) {
				backgroundSprites.get(FOREST).draw(gl, r);
			} else if (cell instanceof TerrainGrid.RoadCell) {
				backgroundSprites.get(ROAD).draw(gl, r);
			} else if (cell instanceof TerrainGrid.WaterCell) {
				backgroundSprites.get(WATER).draw(gl, r);
			} else if (cell instanceof TerrainGrid.MountainCell) {
				backgroundSprites.get(MOUNTAIN).draw(gl, r);
			} else {
				backgroundSprites.get(GRASS).draw(gl, r);
			}
		}

//		backgroundSprite.draw(gl, new Point(-viewOffsetX, -viewOffsetY));
	}
	*/
}
