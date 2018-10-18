package com.mygdx.game.test;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.objects.PlatformPlayer;
import com.mygdx.game.objects.TopDownPlayer;

public class MyPlayer extends PlatformPlayer{

	public MyPlayer(World world, Vector2 position) {
		super(world, position, new Vector2(30, 50));
		setSpeed(5);
		setJumpStrength(15);
	}

	public void render(SpriteBatch sb, ShapeRenderer sr, OrthographicCamera camera) {
		
	}

}
