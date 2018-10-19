package com.mygdx.game.helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.structs.Transform;

public class Helper {
	
	
	
	public static void renderRegion(SpriteBatch sb, TextureRegion region, Transform transform, boolean flipX, boolean flipY) {
		renderRegion(sb, region, transform.getPosition(), transform.getAngle(), transform.getScale(), flipX, flipY);
	}
	
	public static void renderRegion(SpriteBatch sb, TextureRegion region, Vector2 position, float angle, Vector2 size,
			boolean flipX, boolean flipY) {
	
		sb.draw(
				region,
				position.x - region.getRegionWidth()/2f,
				position.y - region.getRegionHeight()/2f,
				region.getRegionWidth()/2f,//originx
				region.getRegionHeight()/2f,//originy
				region.getRegionWidth(),//width
				region.getRegionHeight(),//height
				size.x * (flipX ? -1 : 1),//scalex
				size.y * (flipY ? -1 : 1),//scaley
				angle);
		
	}
	
	public static void renderRegion(SpriteBatch sb, TextureRegion region, Vector2 position, float angle, Vector2 size,
			boolean flipX, boolean flipY, Vector2 origin) {
		sb.draw(
				region,
				position.x - origin.x,
				position.y - origin.y,
				origin.x,//originx
				origin.y,//originy
				region.getRegionWidth(),//width
				region.getRegionHeight(),//height
				size.x * (flipX ? -1 : 1),//scalex
				size.y * (flipY ? -1 : 1),//scaley
				angle);
	}

	public static void renderTex(SpriteBatch sb, Texture tex, Transform transform, boolean flipX, boolean flipY) {
		renderTex(sb, tex, transform.getPosition(), transform.getAngle(), transform.getScale(), flipX, flipY);
	}
	//sem transform
	public static void renderTex(SpriteBatch sb, Texture tex, Vector2 position, float angle, Vector2 size, boolean flipX, boolean flipY) {
		sb.draw(
				tex,
				position.x - tex.getWidth()/2f,
				position.y - tex.getHeight()/2f,
				tex.getWidth()/2f,//originx
				tex.getHeight()/2f,//originy
				tex.getWidth(),//width
				tex.getHeight(),//height
				size.x,//scalex
				size.y,//scaley
				angle,//rotation
				0,//srcx
				0,//srcy
				tex.getWidth(),//srcwidth
				tex.getHeight(),//srcheight
				flipX,
				flipY
				);
	}
	
	public static float tweenToAngle(float inicial, float aFinal, float factor) {
		//TODO: descobrir como fazer tween de angulo
		return 0;
	}
	
	public static void enableBlend() {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	}
	
	public static void disableBlend() {
		Gdx.gl.glDisable(GL20.GL_BLEND);
	}
	
	public static BitmapFont newFont(String path, int size) {
		FreeTypeFontGenerator ftfg = new FreeTypeFontGenerator(Gdx.files.internal(path));
		FreeTypeFontParameter param;
		param = new FreeTypeFontParameter();
		param.size = size;
		param.color = Color.WHITE;
		return ftfg.generateFont(param);
	}
	
	public static BitmapFont newFont(String path, int size, Color color) {
		FreeTypeFontGenerator ftfg = new FreeTypeFontGenerator(Gdx.files.internal(path));
		FreeTypeFontParameter param;
		param = new FreeTypeFontParameter();
		param.size = size;
		param.color = color;
		return ftfg.generateFont(param);
	}
	
	public static BitmapFont newFont(String path, int size, FreeTypeFontParameter parameters) {
		FreeTypeFontGenerator ftfg = new FreeTypeFontGenerator(Gdx.files.internal(path));
		return ftfg.generateFont(parameters);
	}
	
	public static Vector2 randomUnit() {
		return new Vector2((float)Math.random() *2 - 1, (float)Math.random() * 2 - 1).nor();
	}
	
	public static Matrix4 getDefaultProjection() {
		return new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	public static Vector2 newPolarVector(float angle, float magnitude) {
		return new Vector2((float)Math.cos(Math.toRadians(angle)) * magnitude, (float)Math.sin(Math.toRadians(angle)) * magnitude);
	}
}
