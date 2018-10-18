package com.mygdx.game.objects;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.phys.PhysHelp;
import com.mygdx.game.states.State;

public abstract class TopDownPlayer extends GameObject{
	
	protected Body body;
	protected float speed = 10;
	protected Vector2 input;
	
	
	public TopDownPlayer(World world, Vector2 position, float radius) {
		super(position);
		BodyDef def = new BodyDef();
		def.type = BodyType.DynamicBody;
		def.fixedRotation = true;
		def.position.set(position.cpy().scl(1/State.PHYS_SCALE));
		body = PhysHelp.creatCircleBody(world, radius, def);
		body.setUserData(this);
		input = new Vector2();
	}

	public abstract void render(SpriteBatch sb, ShapeRenderer sr, OrthographicCamera camera);


	@Override
	public boolean update(float delta) {
		body.setLinearVelocity(input.cpy().nor().scl(speed));
		return false;
	}
	
	public boolean keyDown(int keycode) {
		if(keycode == Keys.LEFT) {
			input.x = -1;
		}
		if(keycode == Keys.RIGHT) {
			input.x = 1;
		}
		if(keycode == Keys.UP) {
			input.y = 1;
		}
		if(keycode == Keys.DOWN) {
			input.y = -1;
		}

		return false;
	}

	public boolean keyUp(int keycode) {
		if(keycode == Keys.LEFT) {
			if(input.x == -1)
			input.x = 0;
		}
		if(keycode == Keys.RIGHT) {
			if(input.x == 1)
			input.x = 0;
		}
		if(keycode == Keys.UP) {
			if(input.y == 1)
			input.y = 0;
		}
		if(keycode == Keys.DOWN) {
			if(input.y == -1)
			input.y = 0;
		}
		return false;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	public float getSpeed() {
		return speed;
	}
	
	public Vector2 getBodyPosition() {
		return body.getWorldCenter();
	}

	public void setBodyPosition(Vector2 position) {
		body.setTransform(position.x, position.y, 0);
	}
}
