package com.mygdx.game.test;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.helper.Game;
import com.mygdx.game.objects.ObjectInfo;
import com.mygdx.game.objects.TopDownPlayer;

public class MyPlayer extends TopDownPlayer{
	
	Texture texture;
	/* position -> Posição na tela
	 * 
	 * 
	 */
	public MyPlayer(ObjectInfo info, MapProperties properties) {
		super(info, properties);
		setSpeed(5);
		
		texture = new Texture("arm/person.png");
		
		setKeyUp(Keys.W);
		setKeyLeft(Keys.A);
		setKeyDown(Keys.S);
		setKeyRight(Keys.D);

	}
	
	@Override
	public boolean update(float delta) {
		super.update(delta);
		transform.setAngle(Game.mouseWorldPosition(getState().getCamera()).cpy().sub(body.getWorldCenter()).angle() - 90);
		return false;
	}

	public void render(SpriteBatch sb, ShapeRenderer sr, OrthographicCamera camera) {
		renderBodyTexture(sb, texture, body);
	}

}
