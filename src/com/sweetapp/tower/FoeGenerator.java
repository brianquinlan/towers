package com.sweetapp.tower;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.graphics.Point;

public class FoeGenerator {
  private static class Foo {
    public float waveTimeOffset;

    public Foo(float waveTimeOffset) {
      this.waveTimeOffset = waveTimeOffset;
    }
  }

  private final Pitch pitch;
  private float waveTimeOffset;
  private LinkedList<Foo> foos;

  public FoeGenerator(Pitch pitch) {
    this.pitch = pitch;
    startNewWave();
  }

  private void startNewWave() {
    foos = new LinkedList<Foo>();
    foos.add(new Foo(1));
    foos.add(new Foo(2));
    foos.add(new Foo(3));
    foos.add(new Foo(4));
    foos.add(new Foo(5));

    foos.add(new Foo(10));
    foos.add(new Foo(12));
    foos.add(new Foo(14));
    foos.add(new Foo(16));
    foos.add(new Foo(17));
    waveTimeOffset = 0f;
  }

  public List<Point> getStartingCellLocations() {
    final ArrayList<Point> locations = new ArrayList<Point>();
    locations.add(new Point(0, 6));
    return locations;
  }

  public void advanceTime(float t) {
    waveTimeOffset += t;
    while (true) {
      final Foo f = foos.peek();
      if (f == null) {
        if (pitch.getFoes().isEmpty()) {
          startNewWave();
        } else {
          break;
        }
      } else if (f.waveTimeOffset < waveTimeOffset) {
        pitch.newBasicEnemy(0, 6);
        foos.poll();
      } else {
        break;
      }
    }
  }
}
