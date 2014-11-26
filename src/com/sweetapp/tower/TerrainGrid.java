package com.sweetapp.tower;

import android.graphics.Point;

public class TerrainGrid {
	public abstract class Cell {
		private Point gridPosition;
		private float cost;

		public Cell(int x, int y, float cost) {
			gridPosition = new Point(x, y);
			this.cost = cost;
		}
		
		public Point getGridPosition() {
			return new Point(gridPosition.x, gridPosition.y);
		}
		
		public float getCost() {
			return cost;
		}

		abstract public boolean canPlaceTower();
	};

	public class NullCell extends Cell {
		public NullCell(int x, int y) {
			super(x, y, Float.POSITIVE_INFINITY);
		}
		
		 public boolean canPlaceTower() {
			 return false;
		 }
	}

	public class MountainCell extends Cell {
		public MountainCell(int x, int y) {
			super(x, y, Float.POSITIVE_INFINITY);
		}

		 public boolean canPlaceTower() {
			 return false;
		 }	
	}

	public class SwampCell extends Cell {
		public SwampCell(int x, int y) {
			super(x, y, 2);
		}

		 public boolean canPlaceTower() {
			 return true;
		 }
	}

	public class RoadCell extends Cell {
		public RoadCell(int x, int y) {
			super(x, y, 0.5f);
		}

		 public boolean canPlaceTower() {
			 return true;
		 }
	}

	public class WaterCell extends Cell {
		public WaterCell(int x, int y) {
			super(x, y, Float.POSITIVE_INFINITY);
		}

		 public boolean canPlaceTower() {
			 return false;
		 }
	}

	public class GrassCell extends Cell {
		public GrassCell(int x, int y) {
			super(x, y, 1f);
		}

		 public boolean canPlaceTower() {
			 return true;
		 }
	}

	public class WallCell extends Cell {
		public WallCell(int x, int y) {
			super(x, y, Float.POSITIVE_INFINITY);
		}

		 public boolean canPlaceTower() {
			 return false;
		 }
	}

	public class ForestCell extends Cell {
		public ForestCell(int x, int y) {
			super(x, y, 3f);
		}

		 public boolean canPlaceTower() {
			 return true;
		 }
	}

	private NullCell newNullCell(int x, int y) {
		NullCell c = new NullCell(x, y);
		terrain[x][y] = c;
		return c;
	}

	private WaterCell newWaterCell(int x, int y) {
		WaterCell c = new WaterCell(x, y);
		terrain[x][y] = c;
		return c;
	}

	private RoadCell newRoadCell(int x, int y) {
		RoadCell c = new RoadCell(x, y);
		terrain[x][y] = c;
		return c;
	}

	private MountainCell newMountainCell(int x, int y) {
		MountainCell c = new MountainCell(x, y);
		terrain[x][y] = c;
		return c;
	}

	private SwampCell newSwampCell(int x, int y) {
		SwampCell c = new SwampCell(x, y);
		terrain[x][y] = c;
		return c;
	}

	private GrassCell newGrassCell(int x, int y) {
		GrassCell c = new GrassCell(x, y);
		terrain[x][y] = c;
		return c;
	}

	private WallCell newWallCell(int x, int y) {
		WallCell c = new WallCell(x, y);
		terrain[x][y] = c;
		return c;
	}

	private ForestCell newForestCell(int x, int y) {
		ForestCell c = new ForestCell(x, y);
		terrain[x][y] = c;
		return c;
	}

	private void setLine(String line, int y) {
		for (int x = 0; x < line.length(); ++x) {
			char c = line.trim().charAt(x);

			switch (c) {
			case '!':
				newNullCell(x, y);
				break;
			case '~':
				newWaterCell(x, y);
				break;
			case 'M':
				newMountainCell(x, y);
				break;
			case '=':
				newRoadCell(x, y);
				break;
			case ',':
				newSwampCell(x, y);
				break;
			case '\'':
				newGrassCell(x, y);
				break;
			case '|':
				newWallCell(x, y);
				break;
			case '^':
				newForestCell(x, y);
				break;
			default:
				throw new IllegalArgumentException("Unexpected char: " + c);
			}
		}
	}

	private Cell[][] terrain;
	private int width;
	private int height;

	public TerrainGrid(int width, int height) {
		terrain = new Cell[width][height];
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	synchronized public Cell getCell(int x, int y) {
		return terrain[x][y];
	}

	static public TerrainGrid parseString(String m) {
		String[] lines = m.split("\n");

		int width = 0;
		int height = 0;
		for (String line : lines) {
			line = line.trim();
			if (line.length() != 0) {
				++height;
				if (width == 0) {
					width = line.length();
				} else if (line.length() != width) {
					throw new IllegalArgumentException("Unequal line lengths");
				}
			}
		}

		TerrainGrid g = new TerrainGrid(width, height);
		int y = height-1;
		for (String line : lines) {
			line = line.trim();
			if (line.length() != 0) {
				g.setLine(line, y);
				--y;				
			}
		}
		return g;
	}
}
