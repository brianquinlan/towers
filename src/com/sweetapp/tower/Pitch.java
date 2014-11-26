package com.sweetapp.tower;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.graphics.Point;
import android.graphics.PointF;

public class Pitch {
    Set<Projectile> projectiles = new HashSet<Projectile>();
    NavigationGrid navGrid;
    TowerGrid towerGrid;
	TerrainGrid terrainGrid;
	FoeGrid foeGrid;
	FoeGenerator foeGenerator;

	Pitch() {
    	terrainGrid = TerrainGrid.parseString(
    			"!!!!!!!!!!!!!!!!!!!\n" +
				"!~~~~~~~~~~~~~~~~~!\n" +
				"!'''''''''''''''''!\n" +
				"!''''===='''''''''!\n" +
				"!''''=^^=''''''~~~!\n" +
				"!MM''=^^=''''''~||!\n" +
				"==M''=^^========|'!\n" +
				"!=====^^'''''''~||!\n" +
				"!'M''''''''''''~~~!\n" +
				"!'M'''''''''''''''!\n" +
				"!'M''MM'''''''''''!\n" +
				"!MMMMMMMMMMMMMMMMM!\n" +
				"!!!!!!!!!!!!!!!!!!!");
 
    	navGrid = new NavigationGrid(terrainGrid.getWidth(), terrainGrid.getHeight());
		navGrid.setGoalCell(new Point(15, 6));
		for (int x = 0; x < terrainGrid.getWidth(); ++x) {
			for (int y = 0; y < terrainGrid.getHeight(); ++y) {
				navGrid.setCellCost(
						terrainGrid.getCell(x, y).getGridPosition(),
						terrainGrid.getCell(x, y).getCost());
			}
		}

		towerGrid = new TowerGrid(terrainGrid.getWidth(), terrainGrid.getHeight());
		foeGrid = new FoeGrid(terrainGrid.getWidth(), terrainGrid.getHeight());
		foeGenerator = new FoeGenerator(this);
    }

    public List<Tower> getTowers() {
    	return towerGrid.getTowers();
    }

	synchronized public List<Foe> getFoes() {
		return foeGrid.getFoes();
	}

	synchronized public List<Projectile> getProjectiles() {
		return new ArrayList<Projectile>(projectiles);
	}

    public List<TerrainGrid.Cell> getVisibleTerrain() {
    	List <TerrainGrid.Cell> tcs = new ArrayList<TerrainGrid.Cell>();
  
    	for (int x = 1; x < terrainGrid.getWidth() - 1; ++x) {
    		for (int y = 1; y < terrainGrid.getHeight() - 1; ++y) {
    			tcs.add(terrainGrid.getCell(x, y));
    		}
    	}
    	
    	return tcs;
    }

	public int getVisibleXStart() {
		return 1;
	}

	public int getVisibleYStart() {
		return 1;
	}

	public int getVisibleXEnd() {
		return navGrid.width - 2;
	}

	public int getVisibleYEnd() {
		return navGrid.height - 2;
	}

	public Point getNextCell(Point lastCell) {
		return navGrid.getNextCell(lastCell);
	}

    // New object creation methods.
    public Tower newArcherTower(int x, int y) {
    	Tower t = new MissileTower(this, x, y, 5);
    	towerGrid.addTower(t);
    	navGrid.setCellCost(new Point(x, y), Float.POSITIVE_INFINITY);
    	return t;
    }

    public Tower newMangonelTower(int x, int y) {
    	Tower t = new MangonelTower(this, x, y, 15);
    	towerGrid.addTower(t);
    	navGrid.setCellCost(new Point(x, y), Float.POSITIVE_INFINITY);
    	return t;
    }

	public GuidedProjectile newMissile(PointF cell, Foe g) {
		float angle = Trig.getTrueBearing(cell, g.getCell());
		// TODO(brian@sweetapp.com): Why does angle have to be but here?
		GuidedProjectile m = new GuidedProjectile(this, g, cell.x, cell.y, angle, 6f, 50f);
    	projectiles.add(m);
    	return m;
	}

	public GuidedProjectile newMissile(Point gridLocation, Foe g) {
		return newMissile(new PointF(gridLocation.x, gridLocation.y), g);
	}

	public UnguidedProjectile newMangonelRock(Point gridLocation, PointF targetLocation, float velocity) {
		UnguidedProjectile p = new UnguidedProjectile(
				new PointF(gridLocation.x, gridLocation.y), targetLocation, velocity, 10f, .2f);
    	projectiles.add(p);
    	return p;
	}

	synchronized public Foe newBasicEnemy(int x, int y) {
    	Foe g = new Foe(this, x, y, 5050, 0.5f);
    	foeGrid.addFoe(g);
    	return g;
    }

    synchronized public Foe newFastEnemy(int x, int y) {
    	Foe g = new Foe(this, x, y, 5150, 1);
       	foeGrid.addFoe(g);
    	return g;
    }

    // Query methods.
    public List<Foe> getFoesByProximity(final PointF p) {
    	List<Foe> foes = foeGrid.getFoes();
    	Collections.sort(foes, new Comparator<Foe>() {
			public int compare(Foe foe1, Foe foe2) {
				return Float.compare(Trig.distance(foe1.getCell(), p),
						             Trig.distance(foe1.getCell(), p));
			}
    	});
    	return foes;
    }

    synchronized public Foe closestBadGuy(PointF p) {
    	Foe closestBadGuy = null;
    	float minDistance = Float.POSITIVE_INFINITY;

    	for (Foe g : foeGrid.getFoes()) {
    		float distance = Trig.distance(p, g.getCell());
    		if (distance < minDistance) {
    			minDistance = distance;
    			closestBadGuy = g;
    		}
    	}
    	return closestBadGuy;
    }

    public Foe closestBadGuy(Point p) {
    	return closestBadGuy(new PointF(p.x, p.y));
    }

    synchronized public boolean canPlaceTower(int x, int y) {
    	if (towerGrid.getTower(x, y) != null ||
     		!foeGrid.getFoes(x,y).isEmpty() ||
     		!terrainGrid.getCell(x, y).canPlaceTower()) {
    		return false;
    	}
 
    	NavigationGrid n = navGrid.clone();
    	n.setCellCost(new Point(x, y), Float.POSITIVE_INFINITY);
 
    	for (Point p : foeGenerator.getStartingCellLocations()) {
			if (Float.isInfinite(n.getTotalCellCost(p))) {
				return false;
			}	
    	}

    	for (Foe guy : foeGrid.getFoes()) {
			if (Float.isInfinite(n.getTotalCellCost(guy.getNextCell()))) {
				return false;
			}
		}
		return true;
    }
 
    synchronized void advanceTime(float time) {
		for (Foe guy : foeGrid.getFoes()) {
			Point old1 = guy.getLastCell();
			Point old2 = guy.getNextCell();
			guy.advanceTime(time);
			foeGrid.updatePosition(guy, old1, old2, guy.getLastCell(), guy.getNextCell());
		}
		
		for (Tower t : towerGrid.getTowers()) {
			t.advanceTime(time);
		}

		foeGenerator.advanceTime(time);

		HashSet<Foe> hitFoes = new HashSet<Foe>();
		for (Projectile projectile : new ArrayList<Projectile>(projectiles)) {
			projectile.advanceTime(time);
			if (projectile.hasHit()) {
				if (projectile instanceof UnguidedProjectile) {
					UnguidedProjectile p = (UnguidedProjectile) projectile;
					List<Foe> foes = getFoesByProximity(projectile.getCell());
					for (Foe f : foes) {
						if (Trig.distance(f.getCell(), p.getCell()) < p.getSplashRadius()) {
							hitFoes.add(f);
							f.reduceHitPoints(p.getDamage());							
						}
					}
				} else if (projectile instanceof GuidedProjectile ){
					GuidedProjectile m = (GuidedProjectile) projectile;
					m.getTarget().reduceHitPoints(m.getDamage());
					hitFoes.add(m.getTarget());
				}
				projectiles.remove(projectile);
			}
		}
		
		for (Foe f : hitFoes) {
			if (f.getHitPoints() <= 0) {
				foeGrid.removeFoe(f);
			}			
		}
	}

	public TerrainGrid.Cell getTerrainCell(Point p) {
		return terrainGrid.getCell(p.x, p.y);
	}
}
