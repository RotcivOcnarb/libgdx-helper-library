package com.mygdx.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.helper.Game;
import com.mygdx.game.helper.Helper;
import com.mygdx.game.states.State;

public class ParallaxBackground extends GameObject{

	Texture texture;
	OrthographicCamera camera;
			
	public ParallaxBackground(ObjectInfo info, MapProperties properties) {
		super(info, properties);
		
		texture = new Texture(get("imgPath", String.class));
		texture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
	}

	public void render(SpriteBatch sb, ShapeRenderer sr, OrthographicCamera camera) {
		Helper.enableBlend();
		this.camera = camera;
		
		sb.begin();
		sb.setProjectionMatrix(Helper.getDefaultProjection());
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		sb.draw(
				texture,
				0,
				0,
				0,//originx
				0,//originy
				w,//width
				h,//height
				1,//scalex
				1,//scaley
				0,//rotation
				(int)transform.getPosition().x,//srcx
				(int)-transform.getPosition().y,//srcy
				(int)(w / transform.getScale().x),//srcwidth
				(int)(h / transform.getScale().y),//srcheight
				false,
				false
				);
		sb.end();
		sb.setProjectionMatrix(camera.combined);
	}

	public boolean update(float delta) {
		if(camera != null)
		transform.setPosition(new Vector2(camera.position.x, camera.position.y));
		return false;
	}

}
