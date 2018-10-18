package com.mygdx.game.helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class Game {
	public static float globalTimer = 0;
	
	static Vector2 mousePosition = new Vector2();
	
	public static Vector2 mousePosition() {
		mousePosition.set(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
		return mousePosition;
	}
}
