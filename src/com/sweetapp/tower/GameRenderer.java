package com.sweetapp.tower;

import static android.opengl.GLES10.GL_REPLACE;
import static android.opengl.GLES10.GL_TEXTURE_2D;
import static android.opengl.GLES10.GL_TEXTURE_ENV;
import static android.opengl.GLES10.GL_TEXTURE_ENV_MODE;

import java.util.List;
import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.util.DisplayMetrics;

public class GameRenderer implements GLSurfaceView.Renderer {
	public static Point powerOfTwoDimensions(int x, int y) {
		int width = 1;
		int height = 1;
		while (width < x) {
			width *= 2;
		}
		while (height < y) {
			height *= 2;
		}
		return new Point(width, height);
	}

	final private Pitch pitch;
	final private Context context;
	private long lastTime = 0;
	private List<Sprite> sprites;
	private List<RotateableSprite> rotateableSprites;
	private int width;
	private int height;
	private int cellSize;  // The screen size of a grid cell.
	private int logicalHeight;
	private int logicalWidth;
	private int viewOffsetX;
	private int viewOffsetY;
	private Info info;
	private final int TOWER = 0;
	private final int SOLDIER_1 = 1;
	private final int SOLDIER_2 = 2;	
	private final int ARROW = 0;
	private final int STONE = 1;
	private final int STONE_SHADOW = 2;
	private final SmallSpriteTerrainRenderer terrainRenderer;

	GameRenderer(Context context) {
		this.pitch = new Pitch();
		this.context = context;
		this.terrainRenderer = new SmallSpriteTerrainRenderer(context, pitch, this);
	}

	private void advanceTime() {
		long now = java.lang.System.currentTimeMillis(); 
		if (lastTime > 0) {
			pitch.advanceTime((now - lastTime) / 1000.0f);
		}
		lastTime = now;
	}

    private Rect cellToRect(float x, float y) {
    	x -= pitch.getVisibleXStart();
    	y -= pitch.getVisibleYStart();
    	return new Rect(
    			(int)(cellSize * x) - viewOffsetX,
    			(int)(cellSize * y) - viewOffsetY,
    			(int)(cellSize * (x+1)) - viewOffsetX,
    			(int)(cellSize * (y+1)) - viewOffsetY);
    }

    private Rect cellToCanvasRect(float x, float y) {
    	x -= pitch.getVisibleXStart();
    	y -= pitch.getVisibleYStart();
    	int h = pitch.getVisibleYEnd() - pitch.getVisibleYStart();
    	y = h - y;
    	return new Rect(
    			(int)(cellSize * x),
    			(int)(cellSize * y),
    			(int)(cellSize * (x+1)),
    			(int)(cellSize * (y+1)));
    }

    public Rect cellToCanvasRect(Point p) {
    	return cellToCanvasRect(p.x, p.y);
    }

    public Rect cellToRect(PointF p) {
    	return cellToRect(p.x, p.y);
    }

    public Rect cellToRect(Point p) {
    	return cellToRect(p.x, p.y);
    }

    public Point screenToCell(int x, int y) {
    	return new Point(
    			pitch.getVisibleXStart() + (x + viewOffsetX) / cellSize,
    			pitch.getVisibleYStart() + (y + viewOffsetY) / cellSize);
    }

    private void drawFoes(GL10 gl) {
    	for (Foe badGuy : pitch.getFoes()) {
			Rect r = cellToRect(badGuy.getCell());
			sprites.get(SOLDIER_1).draw(gl, r);
    	}		
	}

	private void drawTowers(GL10 gl) {
    	for (Tower tower : pitch.getTowers()) {
			Rect r = cellToRect(tower.gridLocation);
			sprites.get(TOWER).draw(gl, r);
    	}
	}

	private void drawTerrain(GL10 gl) {
		terrainRenderer.drawTerrain(gl);
	}

	private void drawProjectiles(GL10 gl) {
    	for (Projectile projectile : pitch.getProjectiles()) {
    		PointF p = projectile.getCell();
			Rect r = cellToRect(p);
			if (projectile instanceof GuidedProjectile) {				
				rotateableSprites.get(ARROW).draw(gl, r, ((GuidedProjectile) projectile).getAngle());
			} else {
				// TODO(brian@sweetapp.com): Clean this up. The shadows should be drawn
				// in a separate pass before the projectiles. 
				UnguidedProjectile up = (UnguidedProjectile) projectile;
				rotateableSprites.get(STONE_SHADOW).draw(gl, r, 0);
				p.y += (-4 * (0.9f * up.getDestinationFraction() - 0.4f) * (0.9f * up.getDestinationFraction() - 0.4f) + 1) *up.distanceToTarget/10;
				r = cellToRect(p);
				rotateableSprites.get(STONE).draw(gl, r, 0);
			}
    	}
	}

	private void drawInfo(GL10 gl) {
	}

	public void onDrawFrame(GL10 gl) {
		long logicStart = System.currentTimeMillis();
		advanceTime();
		double logicTime = (System.currentTimeMillis() - logicStart) / 1000.;

		long drawStart = System.currentTimeMillis();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		drawTerrain(gl);
		drawTowers(gl);
		drawFoes(gl);
		drawProjectiles(gl);
		drawInfo(gl);
		info.draw(gl, new Rect(0, 0, width, height));
		double drawTime = (System.currentTimeMillis() - drawStart) / 1000.;

		info.recordFrame(logicTime, drawTime);
	}

	public void onSurfaceChanged(GL10 gl, int width, int height) {
		gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL11.GL_PROJECTION);
        gl.glLoadIdentity();

        GLU.gluOrtho2D(gl, 0, width, 0, height);

		this.width = width;
		this.height = height;

		info.resize(width, height);

		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		float dpi = Math.min(metrics.xdpi, metrics.ydpi);
		cellSize = 128;  // TODO: Use the dpi to figure out the appropriate cell size.

		logicalWidth = cellSize * (pitch.getVisibleXEnd() - pitch.getVisibleXStart() + 1);
		logicalHeight = cellSize * (pitch.getVisibleYEnd() - pitch.getVisibleYStart() + 1);
		viewOffsetX = 0;
		viewOffsetY = 0;
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		initializeGL(gl);
		sprites = Sprite.loadSprites(context, gl, R.drawable.tower, R.drawable.soldier1, R.drawable.soldier2);
		terrainRenderer.init(gl);
		info = new Info(gl);
        rotateableSprites = RotateableSprite.loadSprites(context, gl, R.drawable.arrow, R.drawable.stone, R.drawable.stone_shadow);
	}

	public void scrollView(int dx, int dy) {
		if (logicalWidth > width) {
			viewOffsetX = Math.min(
					Math.max(0, viewOffsetX + dx),
					logicalWidth - width);
		}
		if (logicalHeight > height) {
			viewOffsetY = Math.min(
					Math.max(0, viewOffsetY + dy),
					logicalHeight - height);
		}
	}

	private void initializeGL(GL10 gl) {
    	gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    	gl.glEnable(GL_TEXTURE_2D);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnable(GL10.GL_BLEND);
    	gl.glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE,
        		GL_REPLACE);
        // GL_DEPTH_TEST will cause texture alpha to be ignored.
    	gl.glDisable(GL10.GL_DEPTH_TEST);
	}

	Random r = new Random();
	public void tap(int x, int y) {
		Point t = screenToCell(x, y);
		if (pitch.canPlaceTower(t.x, t.y)) {
			if (r.nextBoolean()) {
				pitch.newArcherTower(t.x, t.y);
			} else {
				pitch.newMangonelTower(t.x, t.y);				
			}
		}
	}

	static class Info {
		private Bitmap bitmap;
		private Canvas canvas;
		private int frameCount;
		private Paint backgroundPaint;
		private Paint textPaint;
		private int width;
		private int height;
		private double fps;
		private double lastFrameTime;
		private int textureId;
		private double logicTime;
		private double drawTime;

		Info(GL10 gl) {
			backgroundPaint = new Paint();
			backgroundPaint.setARGB(128, 0, 0, 0);
			textPaint = new Paint();
	        textPaint.setARGB(180, 255, 255, 255);
	        int[] textures = new int[1];
	        gl.glGenTextures(1, textures, 0);
	        textureId = textures[0];
		}

		public void recordFrame(double logicTime, double drawTime) {
	        ++frameCount;
			double frameTime = System.currentTimeMillis() / 1000.0;
	        if (lastFrameTime != 0f) {
	        	double frameDelta = frameTime - lastFrameTime;
	        	double twaInterval = Math.min(frameCount, 60);
	        	fps = fps * (twaInterval - 1) / twaInterval + (1 / frameDelta) / twaInterval;
	        	this.logicTime = this.logicTime * (twaInterval-1) / twaInterval + logicTime / twaInterval;
	        	this.drawTime = this.drawTime * (twaInterval-1) / twaInterval + drawTime / twaInterval;
	        }
	        lastFrameTime = frameTime;
		}

		public void resize(int x, int y) {
			Point p = powerOfTwoDimensions(x, 32);
			width = p.x;
			height = p.y;
			bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
	        canvas = new Canvas(bitmap);
		}

		public void draw(GL10 gl, Rect r) {
			bitmap.eraseColor(Color.TRANSPARENT);

			canvas.drawRect(new Rect(0, 0, width, height), backgroundPaint);
			canvas.drawText(
					String.format("Frames: %.0f fps (%.0fms / %.0fms)", fps, logicTime * 1000.0, drawTime * 1000.0), 
					0,
					12,
					textPaint);
		
			Sprite.loadSprite(gl, textureId, bitmap).draw(gl, new Rect(0, 0, width, height));
		}
	}
}
