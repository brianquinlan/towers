package com.sweetapp.tower;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import android.graphics.Point;

public class FoeGrid {
  public final int width;
  public final int height;
  Set<Foe>[][] foeGrid;
  List<Foe> foes;

  public FoeGrid(int width, int height) {
    this.width = width;
    this.height = height;
    foes = new LinkedList<Foe>();
    foeGrid = new Set[width][height];
    for (int x = 0; x < width; ++x) {
      for (int y = 0; y < height; ++y) {
        foeGrid[x][y] = new HashSet<Foe>();
      }
    }
  }

  public List<Foe> getFoes() {
    return new LinkedList<Foe>(foes);
  }

  public void addFoe(Foe f) {
    foeGrid[f.getLastCell().x][f.getLastCell().y].add(f);
    foeGrid[f.getNextCell().x][f.getNextCell().y].add(f);
    foes.add(f);
  }

  public void removeFoe(Foe foe) {
    foeGrid[foe.getLastCell().x][foe.getLastCell().y].remove(foe);
    foeGrid[foe.getNextCell().x][foe.getNextCell().y].remove(foe);
    foes.remove(foe);
  }

  public Set<Foe> getFoes(int x, int y) {
    return new HashSet<Foe>(foeGrid[x][y]);
  }

  public void updatePosition(
      Foe guy,
      Point oldLastCell,
      Point oldNextCell,
      Point lastCell,
      Point nextCell) {
    foeGrid[oldLastCell.x][oldLastCell.y].remove(guy);
    foeGrid[oldLastCell.x][oldLastCell.y].remove(guy);
    foeGrid[lastCell.x][lastCell.y].add(guy);
    foeGrid[nextCell.x][nextCell.y].add(guy);
  }
}
