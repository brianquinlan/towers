package com.sweetapp.tower;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Rect;

public class SmallSpriteTerrainRenderer {
	private final int FOREST = 0;
	private final int ROAD = 1;
	private final int WATER = 2;	
	private final int MOUNTAIN = 3;	
	private final int GRASS = 4;	

	final GameRenderer coordinateMapper;
	final Context context;
	final Pitch pitch;

	private List<Sprite> backgroundSprites = null;

	public SmallSpriteTerrainRenderer(Context context, Pitch pitch, GameRenderer coordinateMapper) {
		this.coordinateMapper = coordinateMapper;
		this.context = context;
		this.pitch = pitch;
	}

	public void init(GL10 gl) {
		backgroundSprites = Sprite.loadSprites(context, gl, R.drawable.trees, R.drawable.horizontal_road, R.drawable.water,
				R.drawable.mountain, R.drawable.grass);		
	}

	public void drawTerrain(GL10 gl) {
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
}
