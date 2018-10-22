package com.mygdx.game.test;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.objects.GameObject;
import com.mygdx.game.objects.ObjectInfo;
import com.mygdx.game.states.State;

public class Arm extends GameObject{

	Texture hand;
	Texture arm;
	
	float length = 100;
	float scale = 1;
	
	Vector2 armStart, armEnd;
	
	float angle;
	
	public Arm(ObjectInfo info, MapProperties properties) {
		super(info, properties);
		
		armStart = get("armEnd", Vector2.class);
		armEnd = get("armStart", Vector2.class);
		
		length = armEnd.cpy().sub(armStart).len() / scale;
		angle = armEnd.cpy().sub(armStart).angle();
		
		hand = new Texture("arm/hand.png");
		arm = new Texture("arm/arm.png");
		arm.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
	}
	
	public Arm(ObjectInfo info, Vector2 armStart, Vector2 armEnd) {
		super(info, new MapProperties());
		
		this.armStart = armStart.cpy();
		this.armEnd = armEnd.cpy();
		
		length = armEnd.cpy().sub(armStart).len() / scale;
		angle = armEnd.cpy().sub(armStart).angle();
		
		hand = new Texture("arm/hand.png");
		arm = new Texture("arm/arm.png");
		arm.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
	}

	@Override
	public void render(SpriteBatch sb, ShapeRenderer sr, OrthographicCamera camera) {
	
		
		
		
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
		this.armStart = armStart.cpy().scl(State.PHYS_SCALE);
	}

	public Vector2 getArmEnd() {
		return armEnd;
	}

	public void setArmEnd(Vector2 armEnd) {
		this.armEnd = armEnd.cpy().scl(State.PHYS_SCALE);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void create() {
		// TODO Auto-generated method stub
		
	}

}
