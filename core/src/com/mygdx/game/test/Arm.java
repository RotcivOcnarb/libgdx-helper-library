package com.mygdx.game.test;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.helper.Game;
import com.mygdx.game.objects.GameObject;
import com.mygdx.game.states.State;

public class Arm extends GameObject{

	Texture hand;
	Texture arm;
	
	float length = 100;
	float scale = 1;
	
	Vector2 armStart, armEnd;
	
	float angle;
	
	public Arm(Vector2 start, Vector2 end) {
		super(Vector2.Zero);
		
		armStart = start;
		armEnd = end;
		
		length = armEnd.cpy().sub(armStart).len() / scale;
		
		angle = armEnd.cpy().sub(armStart).angle();
		
		hand = new Texture("arm/hand.png");
		arm = new Texture("arm/arm.png");
		arm.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
	}

	@Override
	public void render(SpriteBatch sb, ShapeRenderer sr, OrthographicCamera camera) {
	
		
		
		sb.begin();
		
			sb.draw(
					arm,
					armStart.x / State.PHYS_SCALE,
					armStart.y / State.PHYS_SCALE - arm.getHeight()/2f / State.PHYS_SCALE,
					0,
					arm.getHeight()/2f / State.PHYS_SCALE,
					length / State.PHYS_SCALE,
					arm.getHeight() / State.PHYS_SCALE,
					scale,
					scale,
					angle,
					0,
					0,
					(int)length,
					arm.getHeight(),
					false,
					false
					);
			
			Vector2 origin = new Vector2(hand.getWidth()/3 / State.PHYS_SCALE, hand.getHeight()/2 / State.PHYS_SCALE);
			
			sb.draw(
					hand,
					armEnd.x / State.PHYS_SCALE - origin.x,
					armEnd.y / State.PHYS_SCALE - origin.y,
					origin.x,
					origin.y,
					hand.getWidth() / State.PHYS_SCALE,
					hand.getHeight() / State.PHYS_SCALE,
					scale,
					scale,
					angle,
					0,
					0,
					hand.getWidth(),
					hand.getHeight(),
					false,
					false
					);
		
		sb.end();
		
		
	}

	public boolean update(float delta) {
		length = armEnd.cpy().sub(armStart).len() / scale;
		angle = armEnd.cpy().sub(armStart.cpy()).angle();
		return false;
	}

	public Vector2 getArmStart() {
		return armStart;
	}

	public void setArmStart(Vector2 armStart) {
		this.armStart = armStart;
	}

	public Vector2 getArmEnd() {
		return armEnd;
	}

	public void setArmEnd(Vector2 armEnd) {
		this.armEnd = armEnd;
	}

}
