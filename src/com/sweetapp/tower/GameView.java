package com.sweetapp.tower;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.GestureDetector.OnGestureListener;

public class GameView extends GLSurfaceView implements OnGestureListener {

	private GameRenderer renderer;
	private GestureDetector gestureDetector;

	public GameView(Context context) {
        super(context);
        // DEBUG_LOG_GL_CALLS is slow but provides more information.
        setDebugFlags(DEBUG_CHECK_GL_ERROR);
        renderer = new GameRenderer(context);
        setRenderer(renderer);
        gestureDetector = new GestureDetector(context, this);
        gestureDetector.setIsLongpressEnabled(false);
	}

    public boolean onTouchEvent(final MotionEvent event) {
        queueEvent(new Runnable(){
            public void run() {
            	gestureDetector.onTouchEvent(event);
            }});
            return true;
        }

	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}

	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		renderer.scrollView((int) distanceX, (int) -distanceY);
		return true;
	}

	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	public boolean onSingleTapUp(MotionEvent e) {
		renderer.tap((int) e.getX(), this.getHeight() - (int) e.getY());
		return true;
	}
}
