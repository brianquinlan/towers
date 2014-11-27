package com.sweetapp.tower;

import java.util.ArrayList;
import java.util.List;

public class TowerGrid {
  public final int width;
  public final int height;
  private final Tower[][] towerGrid;

  public TowerGrid(int width, int height) {
    this.width = width;
    this.height = height;
    this.towerGrid = new Tower[width][height];
  }

  public void addTower(Tower t) {
    towerGrid[t.gridLocation.x][t.gridLocation.y] = t;
  }

  public Tower getTower(int x, int y) {
    return this.towerGrid[x][y];
  }

  public List<Tower> getTowers() {
    final ArrayList<Tower> towers = new ArrayList<Tower>(this.width * this.height);
    for (int x = 0; x < width; ++x) {
      for (int y = 0; y < height; ++y) {
        if (towerGrid[x][y] != null) {
          towers.add(towerGrid[x][y]);
        }
      }
    }
    return towers;
  }
}
