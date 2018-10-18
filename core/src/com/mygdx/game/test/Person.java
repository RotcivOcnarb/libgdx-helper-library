package com.mygdx.game.test;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.helper.Helper;
import com.mygdx.game.objects.GameObject;
import com.mygdx.game.objects.GameParticle;
import com.mygdx.game.phys.PhysHelp;
import com.mygdx.game.states.State;

public class Person extends GameObject{

	Body body;
	
	Vector2 originalPosition;
	float strength;
	
	float angryTime = 0;
	
	State state;
	
	static BitmapFont font;
	
	static {
		font = Helper.newFont("Allan-Bold.ttf", 24);
	}
	
	Vector2 grabPosition;
	Arm arm;
	
	public Person(World world, Vector2 position, Float strength, Vector2 size, State state, Vector2 grabPosition) {
		super(Vector2.Zero);
				
		this.state = state;
		this.originalPosition = position.cpy();
		this.strength = strength;
		this.grabPosition = grabPosition.cpy();
		
		arm = new Arm(position.cpy(), grabPosition);
		state.putToUpdate(arm);
		
		body = PhysHelp.createDynamicCircleBody(world, position, size.x/2f, true);
		body.setLinearDamping(10);
	}

	@Override
	public void render(SpriteBatch sb, ShapeRenderer sr, OrthographicCamera camera) {
		// TODO Auto-generated method stub
		arm.render(sb, sr, camera);
		
	}

	@Override
	public boolean update(float delta) {
		angryTime -= delta;
		arm.setArmStart(body.getWorldCenter().cpy().scl(State.PHYS_SCALE));
		body.applyForceToCenter(body.getWorldCenter().sub(originalPosition.cpy().scl(1/State.PHYS_SCALE)).scl(-strength), true);
		
		if(body.getWorldCenter().sub(originalPosition.cpy().scl(1/State.PHYS_SCALE)).len() > 0.3f) {
			if(angryTime < 0) {
				angryTime = 1;
				GameParticle gp = new GameParticle(body.getWorldCenter(), "Grr", font, Color.RED, 1);
				gp.setVelocity(Helper.randomUnit().scl(0.3f));
				gp.setDrag(0.2f);
				state.addParticle(gp);
			}
		}
		
		return false;
	}

}
