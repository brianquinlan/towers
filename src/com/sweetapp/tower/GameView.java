package com.sweetapp.tower;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;

public class GameView extends GLSurfaceView implements OnGestureListener {

  private final GameRenderer renderer;
  private final GestureDetector gestureDetector;

  public GameView(Context context) {
    super(context);
    // DEBUG_LOG_GL_CALLS is slow but provides more information.
    setDebugFlags(DEBUG_CHECK_GL_ERROR);
    renderer = new GameRenderer(context);
    setRenderer(renderer);
    gestureDetector = new GestureDetector(context, this);
    gestureDetector.setIsLongpressEnabled(false);
  }

  @Override
  public boolean onTouchEvent(final MotionEvent event) {
    queueEvent(new Runnable(){
      @Override
      public void run() {
        gestureDetector.onTouchEvent(event);
      }});
    return true;
  }

  @Override
  public boolean onDown(MotionEvent e) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
      float velocityY) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void onLongPress(MotionEvent e) {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
      float distanceY) {
    renderer.scrollView((int) distanceX, (int) -distanceY);
    return true;
  }

  @Override
  public void onShowPress(MotionEvent e) {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean onSingleTapUp(MotionEvent e) {
    renderer.tap((int) e.getX(), this.getHeight() - (int) e.getY());
    return true;
  }
}
