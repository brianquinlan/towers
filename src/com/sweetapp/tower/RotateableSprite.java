package com.sweetapp.tower;

import static android.opengl.GLES10.GL_TEXTURE_2D;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;

class RotateableSprite {
    private final float texture[] = {
		0f, 1f,
		0f, 0f,
		1f, 0f,
		1f, 1f
    };

    private final byte indices[] = {
        0, 1, 3,
        1, 2, 3
    };

    private float vertices[] = {
		-16f, -16f, 0f,
		-16f, 16f, 0f,
		16f, 16f, 0f,
		16f, -16f, 0f
	};

	private FloatBuffer vertexBuffer;
	private ByteBuffer indexBuffer;
	private FloatBuffer texBuffer;
	private final int textureId;

	public RotateableSprite(int textureId, int width, int height) {
		this.textureId = textureId;

		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());
        vertexBuffer = vbb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        indexBuffer = ByteBuffer.allocateDirect(indices.length);
        indexBuffer.put(indices);
        indexBuffer.position(0);

        ByteBuffer tbb = ByteBuffer.allocateDirect(vertices.length / 3 * 2 * 4);
        tbb.order(ByteOrder.nativeOrder());
        texBuffer = tbb.asFloatBuffer();
        texBuffer.put(texture);
        texBuffer.position(0);
    }

	public void draw(GL10 gl, Rect r, float angle) {
    	gl.glMatrixMode(GL10.GL_MODELVIEW);
    	gl.glPushMatrix();
    	gl.glLoadIdentity();
    	gl.glTranslatef(r.left+32, r.top+32, 0);
    	gl.glRotatef(-angle, 0, 0, 1);
    	gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glBindTexture(GL_TEXTURE_2D, textureId);
        gl.glFrontFace(GL10.GL_CW);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuffer);
        gl.glDrawElements(GL10.GL_TRIANGLES, 6, GL10.GL_UNSIGNED_BYTE, indexBuffer);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glPopMatrix();
	}

	static public List<RotateableSprite> loadSprites(Context context, GL10 gl, int... resourceIds) {
		ArrayList<RotateableSprite> sprites = new ArrayList<RotateableSprite>(resourceIds.length);

        int[] textures = new int[resourceIds.length];
        gl.glGenTextures(resourceIds.length, textures, 0);
        for (int i = 0; i < resourceIds.length; ++i) {
            Bitmap m = Sprite.loadBitmap(context, resourceIds[i]);
            Sprite.bindTexture(gl, textures[i], m, m.getWidth(), m.getHeight());
        	sprites.add(new RotateableSprite(textures[i], m.getWidth(), m.getHeight()));
        }
        return sprites;
	}
}
