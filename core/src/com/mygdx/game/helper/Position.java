package com.mygdx.game.helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class Position {
	
	public static Vector2 CENTER;
	public static Vector2 CENTERX;
	public static Vector2 CENTERY;
	public static Vector2 TOPLEFT;
	
	static {
		CENTER = new Vector2(Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()/2f);
		CENTERX = new Vector2(Gdx.graphics.getWidth()/2f, 0);
		CENTERY = new Vector2(0, Gdx.graphics.getHeight()/2f);
		
		TOPLEFT = new Vector2(0, Gdx.graphics.getHeight());
	}

}
