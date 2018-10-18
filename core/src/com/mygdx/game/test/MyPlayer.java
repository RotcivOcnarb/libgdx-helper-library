package com.mygdx.game.test;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.objects.PlatformPlayer;
import com.mygdx.game.objects.TopDownPlayer;

public class MyPlayer extends TopDownPlayer{

	public MyPlayer(World world, Vector2 position, Vector2 size) {
		super(world, position, size.x/2f);
		setSpeed(5);
	}

	public void render(SpriteBatch sb, ShapeRenderer sr, OrthographicCamera camera) {
		
	}

}
