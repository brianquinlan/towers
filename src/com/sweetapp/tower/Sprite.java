package com.sweetapp.tower;

import static android.opengl.GLES10.GL_CLAMP_TO_EDGE;
import static android.opengl.GLES10.GL_LINEAR;
import static android.opengl.GLES10.GL_NEAREST;
import static android.opengl.GLES10.GL_TEXTURE_2D;
import static android.opengl.GLES10.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES10.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES10.GL_TEXTURE_WRAP_S;
import static android.opengl.GLES10.GL_TEXTURE_WRAP_T;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.opengl.GLUtils;

class Sprite {
  private final int width;
  private final int height;
  private final int textureId;

  private Sprite(int textureId, int width, int height) {
    this.textureId = textureId;
    this.width = width;
    this.height = height;
  }

  public void draw(GL10 gl, Rect r) {
    gl.glBindTexture(GL_TEXTURE_2D, textureId);
    ((GL11Ext) gl).glDrawTexiOES(r.left, r.top, 0, r.width(), r.height());
  }

  public void draw(GL10 gl, Point p) {
    gl.glBindTexture(GL_TEXTURE_2D, textureId);
    ((GL11Ext) gl).glDrawTexiOES(p.x, p.y, 0, width, height);
  }

  static public List<Sprite> loadSprites(Context context, GL10 gl, int... resourceIds) {
    final ArrayList<Sprite> sprites = new ArrayList<Sprite>(resourceIds.length);

    final int[] textures = new int[resourceIds.length];
    gl.glGenTextures(resourceIds.length, textures, 0);
    for (int i = 0; i < resourceIds.length; ++i) {
      sprites.add(loadSprite(context, gl, textures[i], resourceIds[i]));
    }
    return sprites;
  }

  static public Sprite loadSprite(GL10 gl, int textureId, Bitmap bitmap, int width, int height) {
    bindTexture(gl, textureId, bitmap, width, height);
    return new Sprite(textureId, width, height);

  }

  public static void bindTexture(GL10 gl, int textureId, Bitmap bitmap,
      int width, int height) {
    gl.glBindTexture(GL_TEXTURE_2D, textureId);
    gl.glTexParameterf(GL_TEXTURE_2D,
        GL_TEXTURE_MIN_FILTER,
        GL_NEAREST);
    gl.glTexParameterf(
        GL_TEXTURE_2D,
        GL_TEXTURE_MAG_FILTER,
        GL_LINEAR);

    gl.glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S,
        GL_CLAMP_TO_EDGE);
    gl.glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T,
        GL_CLAMP_TO_EDGE);


    GLUtils.texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);
    final int cropRect[] = { 0, height, width, -height };
    ((GL11) gl).glTexParameteriv(GL_TEXTURE_2D,
        GL11Ext.GL_TEXTURE_CROP_RECT_OES, cropRect, 0);
  }

  static public Sprite loadSprite(GL10 gl, int textureId, Bitmap bitmap) {
    return loadSprite(gl, textureId, bitmap, bitmap.getWidth(), bitmap.getHeight());
  }

  static public Sprite loadSprite(Context context, GL10 gl, int textureId, int resourceId) {
    final Bitmap bitmap = loadBitmap(context, resourceId);
    try {
      return loadSprite(gl, textureId, bitmap);
    } finally {
      bitmap.recycle();
    }
  }

  public static Bitmap loadBitmap(Context context, int resourceId) {
    final InputStream is = context.getResources().openRawResource(resourceId);

    Bitmap bitmap;
    try {
      bitmap = BitmapFactory.decodeStream(is);
    } finally {
      try {
        is.close();
      } catch(final IOException e) {
        // Ignore.
      }
    }
    return bitmap;
  }
}