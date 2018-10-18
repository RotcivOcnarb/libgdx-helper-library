package com.mygdx.game.helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class Size {
	
	public static Vector2 HALFSCREEN;
	public static Vector2 SCREEN;
	public static Vector2 HALFWIDTH;
	public static Vector2 WIDTH;
	public static Vector2 HALFHEIGHT;
	public static Vector2 HEIGHT;
	
	static {
		HALFSCREEN = new Vector2(Gdx.graphics.getWidth()/2f, Gdx.graphics.getHeight()/2f);
		SCREEN = new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		HALFWIDTH = new Vector2(Gdx.graphics.getWidth()/2f, 0);
		WIDTH = new Vector2(Gdx.graphics.getWidth(), 0);
		
		HALFHEIGHT = new Vector2(0, Gdx.graphics.getHeight()/2f);
		HEIGHT = new Vector2(0, Gdx.graphics.getHeight());
	}

}
