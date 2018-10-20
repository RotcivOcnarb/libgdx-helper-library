package com.mygdx.game.test;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.helper.Helper;
import com.mygdx.game.objects.GameObject;
import com.mygdx.game.objects.GameParticle;
import com.mygdx.game.objects.ObjectInfo;
import com.mygdx.game.phys.PhysHelp;
import com.mygdx.game.states.State;
import com.mygdx.game.states.StateOne;

public class Person extends GameObject{

	Body body;
	Texture person;
	
	Vector2 originalPosition;
	float strength;
	float angryTime = 0;
	float totalAngry = 3;
	boolean sitDown;
	
	static BitmapFont font;
	
	static {
		font = Helper.newFont("Allan-Bold.ttf", 32);
	}
	
	Vector2 grabPosition;
	Arm arm;
	
	public Person(ObjectInfo info, MapProperties properties) {
		super(info, properties, true);
		
		this.sitDown = get("sitDown") != null ? get("sitDown", Boolean.class) : false;
		this.originalPosition = get("position", Vector2.class);
		this.strength = get("strength") != null ? get("strength", Float.class) : 0;
		
		person = new Texture("arm/person.png");
		
		MapProperties mp = new MapProperties();
		mp.put("armStart", originalPosition.cpy());
		mp.put("armEnd", originalPosition.cpy());
		arm = new Arm(info.withZ(4), mp);
		getState().putInScene(arm);
		
		transform.setAngle((float) (Math.random() * 360));
		
		body = PhysHelp.createDynamicCircleBody(getState().getWorld(), originalPosition.cpy(), get("width", Float.class)/2f, true);
		body.setLinearDamping(10);
	}

	@Override
	public void render(SpriteBatch sb, ShapeRenderer sr, OrthographicCamera camera) {
		
		renderBodyTexture(sb, person, body);
		
		if(grabPosition != null && !sitDown) {
			arm.render(sb, sr, camera);
		}
		
		sb.end();
		
		sr.begin(ShapeType.Filled);
		sr.setColor(Color.ORANGE.cpy().lerp(Color.RED, Math.max(0, angryTime) / totalAngry));
		float width = Math.max(0, angryTime) / totalAngry * 100f;
		sr.rect(body.getWorldCenter().x - width/2f / State.PHYS_SCALE, body.getWorldCenter().y + 10 / State.PHYS_SCALE, width / State.PHYS_SCALE, 5 / State.PHYS_SCALE);
		
		sr.end();
		
		sb.begin();
		
	}

	@Override
	public boolean update(float delta) {
		angryTime -= delta;
		
		if(grabPosition == null && ((StateOne) getState()).handles.size() > 0 && !sitDown) {
			grabPosition = ((StateOne) getState()).handles.get(0);
			for(Vector2 v : ((StateOne) getState()).handles) {
				if(
						v.cpy().scl(1/State.PHYS_SCALE).sub(body.getWorldCenter()).len() 
						< grabPosition.cpy().scl(1/State.PHYS_SCALE).sub(body.getWorldCenter()).len()) {
					grabPosition = v.cpy();
				}
			}
		}
		
		if(grabPosition != null && !sitDown) {
			transform.setAngle(grabPosition.cpy().scl(1/State.PHYS_SCALE).sub(body.getWorldCenter()).angle() - 90);
			
			arm.setArmEnd(grabPosition.cpy());
			arm.setArmStart(body.getWorldCenter().cpy().add(Helper.newPolarVector(transform.getAngle(), 0.5f)).scl(State.PHYS_SCALE));
		}
		
		body.applyForceToCenter(body.getWorldCenter().sub(originalPosition.cpy().scl(1/State.PHYS_SCALE)).scl(-strength), true);
		
		if(body.getWorldCenter().sub(originalPosition.cpy().scl(1/State.PHYS_SCALE)).len() > 0.3f) {
			if(angryTime < 0) {
				angryTime = totalAngry;
				GameParticle gp = new GameParticle(info.withZ(5), body.getWorldCenter(), "Grr", font, Color.RED, 1);
				gp.setVelocity(Helper.randomUnit().scl(0.1f));
				gp.setDrag(0.2f);
				getState().addParticle(gp);
			}
		}
		
		return false;
	}

}
