package com.mygdx.game.helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Game {
	public static float globalTimer = 0;
	
	static Vector2 mousePosition = new Vector2();
	
	public static Vector2 mousePosition() {
		mousePosition.set(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
		return mousePosition;
	}
	
	public static Vector2 mouseWorldPosition(OrthographicCamera camera) {
		mousePosition.set(Gdx.input.getX(), Gdx.input.getY());
		
		Vector3 mpos = camera.unproject(new Vector3(mousePosition, 0));
		
		mousePosition.set(mpos.x, mpos.y);
		
		return mousePosition;
	}
}
