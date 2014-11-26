package com.sweetapp.tower;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.graphics.Point;

public class NavigationGrid {
	int width;
	int height;

	static class Cell {
		float cost;
		float totalCost;
		Point nextCell;

		public Cell(float cost) {
			this.cost = cost;
		}
	}

	Cell[][] grid;
	List<Point> goalCells = new ArrayList<Point>();

	public NavigationGrid(int width, int height) {
		this.width = width;
		this.height = height;
		grid = new Cell[width][height];
		
		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				grid[x][y] = new Cell(1);
			}
		}
	}

	protected NavigationGrid(NavigationGrid other) {
		width = other.width;
		height = other.height;
		grid = new Cell[width][height];
		
		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				grid[x][y] = new Cell(other.grid[x][y].cost);
			}
		}
		
		goalCells.addAll(other.goalCells);
	}

	public NavigationGrid clone() {
		return new NavigationGrid(this);		
	}

	public void setGoalCell(Point cell) {
		checkPointInGrid(cell);
		goalCells.add(cell);
	}


	class WorkItem {
		Point cell;
		Point sponsorCell;
		
		public WorkItem(int x, int y, Point sponsor) {
			cell = new Point(x, y);
			sponsorCell = sponsor;
		}
	}

	protected void handleCell(LinkedList<WorkItem> work, Point cell, Point sponsor) {
		if (cell.x >= 0 && cell.x < width &&
			cell.y >= 0 && cell.y < height) {
			Cell c = grid[cell.x][cell.y];

			if (sponsor == null) {
				c.nextCell = null;
				c.totalCost = c.cost;
			} else {
				Cell s = grid[sponsor.x][sponsor.y];
				if (c.totalCost > s.totalCost + c.cost) {
					c.nextCell = sponsor;
					c.totalCost = s.totalCost + c.cost;
				} else {
					return;
				}
			}

			work.add(new WorkItem(cell.x - 1, cell.y, cell));
			work.add(new WorkItem(cell.x + 1, cell.y, cell));
			work.add(new WorkItem(cell.x, cell.y - 1, cell));
			work.add(new WorkItem(cell.x, cell.y + 1, cell));
		}
	}

	protected void calc() {
		LinkedList<WorkItem> work = new LinkedList<WorkItem>();

		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				grid[x][y].totalCost = Float.POSITIVE_INFINITY;
			}
		}

		for (Point p : goalCells) {
			handleCell(work, new Point(p.x, p.y), null);
		}


		while (!work.isEmpty()) {
			WorkItem w = work.poll();
			handleCell(work, w.cell, w.sponsorCell);
		}
	}

	public void setCellCost(Point cell, float cost) {
		checkPointInGrid(cell);
		grid[cell.x][cell.y].cost = cost;
		calc();
	}

	private void checkPointInGrid(Point cell) {
		if (cell == null ) {
			throw new IllegalArgumentException("cell is null");
		} else if (cell.x < 0) {
			throw new IllegalArgumentException(String.format("cell.x (%s) < 0", cell.x));
		} else if (cell.x >= width) {
			throw new IllegalArgumentException(String.format("cell.x (%s) > %s", cell.x, width));			
		} else if (cell.y < 0) {
			throw new IllegalArgumentException(String.format("cell.x (%s) < 0", cell.y));
		} else if (cell.y >= height) {
			throw new IllegalArgumentException(String.format("cell.x (%s) > %s", cell.y, height));			
		}
	}

	public Point getNextCell(Point cell) {
		checkPointInGrid(cell);
		return grid[cell.x][cell.y].nextCell;
	}

	public float getTotalCellCost(Point cell) {
		checkPointInGrid(cell);
		return grid[cell.x][cell.y].totalCost;
	}
 }
