package com.mygdx.game.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.UnaryOperator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.DelaunayTriangulator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.helper.Helper;
import com.mygdx.game.objects.AStarPathFinder;
import com.mygdx.game.objects.GameObject;
import com.mygdx.game.objects.GameParticle;
import com.mygdx.game.objects.ImageObject;
import com.mygdx.game.objects.ObjectInfo;
import com.mygdx.game.states.State;
import com.mygdx.game.states.StateOne;

public class Person extends GameObject{

	Body body;
	Texture person;
	
	float sitAngle;
	Vector2 originalPosition;
	float strength;
	float angryTime = 0;
	float totalAngry = 3;
	boolean sitDown;
	float paciency = 30;
	float compulsivity = 500;
	public static BitmapFont font;
	
	DelaunayTriangulator triangulator;
	
	Color tint;
	
	float calling = 0;
	
	static Texture hey_tex;
	static Texture calling_left;
	static Texture calling_right;
	
	ImageObject calling_object;
	
	GameParticle heyPart;
	
	boolean isWalking = true;
	
	static {
		hey_tex = new Texture("arm/hey.png");
		font = Helper.newFont("Allan-Bold.ttf", 32);
		calling_left = new Texture("arm/calling_left.png");
		calling_right = new Texture("arm/calling_right.png");
	}
	
	Vector2 grabPosition;
	Arm arm;
	
	PersonPath path;
	
	int location;
	
	
	public Person(ObjectInfo info, MapProperties properties) {
		super(info, properties, true);
		
		this.sitDown = get("sitDown") != null ? get("sitDown", Boolean.class) : false;
		this.originalPosition = get("position", Vector2.class);
		this.strength = get("strength") != null ? get("strength", Float.class) : 0;
		person = new Texture("arm/person.png");
		location = get("location", Integer.class);
		float rotation = get("rotation") != null ? get("rotation", Float.class) : (float)Math.random() * 360;
		

		arm = new Arm(info.withZ(4), originalPosition.cpy(), originalPosition.cpy());
		getState().putInScene(arm);
		
		arm.setToRender(!sitDown);
		
		transform.setAngle(rotation);
		
		body = Helper.PhysHelp.createDynamicCircleBody(getState().getWorld(), get("position", Vector2.class), get("width", Float.class)/2f, true);
		body.setLinearDamping(10);
		body.setUserData(this);
		
		tint = Color.WHITE.cpy();
		
		calling = (float) (Math.random() * compulsivity + paciency);
		
		calling_object = new ImageObject(info.withZ(4), calling_left);
		getState().putInScene(calling_object);
	
	}
	
	public void create() {
		sitDown = location != -1;
		
		ArrayList<Vector2> standupPoints = ((StateOne) getState()).remainingStandupPoints;
		if(location == -1 && standupPoints.size() > 0) {
					
			int startPoint = 0;
					
			for(int i = 0; i < standupPoints.size(); i ++) {
				Vector2 sp = standupPoints.get(i);
				if(sp.cpy().sub(body.getWorldCenter()).len2() < standupPoints.get(startPoint).cpy().sub(body.getWorldCenter()).len2()) {
					startPoint = i;
				}
			}

			int endPoint = (int)(Math.random() * standupPoints.size());
					
			while(standupPoints.get(endPoint).cpy().sub(body.getWorldCenter()).len() > 20) {
				System.out.println("Muito longe ("+standupPoints.get(endPoint).cpy().sub(body.getWorldCenter()).len()+"), tentando de novo");
				endPoint = (int)(Math.random() * standupPoints.size());
			}
			standupPoints.remove(endPoint);
						
			AStarPathFinder pathFinder = new AStarPathFinder(((StateOne) getState()).allStandupPoints);
			ArrayList<Vector2> pathastar = pathFinder.findPath(startPoint, endPoint);
			int tries = 0;
			while(pathastar == null) {
				pathastar = pathFinder.findPath(startPoint, endPoint);
				tries ++;
				if(tries >= 100) break;
			}
					
			path = new PersonPath(info, pathastar, -1);
					
			path.getPath().replaceAll(new UnaryOperator<Vector2>() {
				public Vector2 apply(Vector2 t) {
					return t.scl(State.PHYS_SCALE);
				}
			});
							
		}
				
	}

	public boolean isCalling() {
		return calling < paciency && angryTime < 0;
	}
	float paradoTimer = 0;
	@Override
	public void render(SpriteBatch sb, ShapeRenderer sr, OrthographicCamera camera) {
		
		if(isWalking) {
			if(body.getLinearVelocity().cpy().len() < 0.01f) {
				paradoTimer += Gdx.graphics.getDeltaTime();
				if(paradoTimer > 0.3f) {
					body.applyForceToCenter(Helper.newPolarVector(transform.getAngle() + 45, 100), true);
				}
			}
		}
		
		if(isWalking) {
			
			ArrayList<PersonPath> paths = ((StateOne) getState()).paths;
			if(paths.size() > 0) {
				while(path == null || path.location != location) {
					path = paths.get((int)(Math.random() * paths.size()));
				}
				paths.remove(path);
			}
		}
		
		sb.setColor(tint);
		renderBodyTexture(sb, person, body);
		sb.setColor(1, 1, 1, 1);
		
		arm.setToRender(!isWalking && !sitDown);
		
		if(isCalling() && !isWalking) {
			Vector3 screenPos = camera.project(new Vector3(body.getWorldCenter(), 0));
			
			if(screenPos.x < 0 || screenPos.x > Gdx.graphics.getWidth()) {
				calling_object.setToRender(true);
				if(screenPos.x < 0) {
					Vector2 screenTexturePosition = new Vector2(0,  Gdx.graphics.getHeight() - screenPos.y);
					Vector3 inGamePosition = camera.unproject(new Vector3(screenTexturePosition, 0));
					calling_object.setPosition(new Vector2(inGamePosition.x + calling_left.getWidth()/2f / State.PHYS_SCALE, inGamePosition.y));
					calling_object.setImage(calling_left);
				}

				if(screenPos.x > Gdx.graphics.getWidth()) {
					Vector2 screenTexturePosition = new Vector2(Gdx.graphics.getWidth(),  Gdx.graphics.getHeight() - screenPos.y);
					Vector3 inGamePosition = camera.unproject(new Vector3(screenTexturePosition, 0));
					calling_object.setPosition(new Vector2(inGamePosition.x - calling_right.getWidth()/2f / State.PHYS_SCALE, inGamePosition.y));
					calling_object.setImage(calling_right);
				}
			}
			else {
				calling_object.setToRender(false);
			}
		}
		
		sb.end();
		
		//desenha caminho se nao tiver sentado
//		if(isWalking && location == -1) {
//			sr.begin(ShapeType.Filled);
//			sr.setColor(Color.BLUE);
//			for(int i = 1; i < path.getPath().size(); i ++) {
//				sr.rectLine(path.getPath().get(i - 1).cpy().scl(1/State.PHYS_SCALE), path.getPath().get(i).cpy().scl(1/State.PHYS_SCALE), 0.05f);
//			}
//			sr.end();
//		}
//		
		if(!isWalking) {
			//Desenha shapes
			sr.begin(ShapeType.Filled);
			sr.setColor(Color.WHITE);

			
			sr.setColor(Color.ORANGE.cpy().lerp(Color.RED, Math.max(0, angryTime) / totalAngry));
			float width = Math.max(0, angryTime) / totalAngry * 100f;
			sr.rect(body.getWorldCenter().x - width/2f / State.PHYS_SCALE, body.getWorldCenter().y + 10 / State.PHYS_SCALE, width / State.PHYS_SCALE, 5 / State.PHYS_SCALE);
			
			if(isCalling()) {
				width = Helper.clamp(calling, 0, paciency) / paciency * 100f;
				sr.setColor(Color.GREEN.cpy().lerp(Color.ORANGE, Helper.clamp(calling, 0, paciency) / paciency));
				sr.rect(body.getWorldCenter().x - width/2f / State.PHYS_SCALE, body.getWorldCenter().y + 10 / State.PHYS_SCALE, width / State.PHYS_SCALE, 5 / State.PHYS_SCALE);
			}
			sr.end();
		}
		
		
		

		
		sb.begin();
	
	}
	
	boolean heyed = false;
	boolean timeOver = true;

	@Override
	public boolean update(float delta) {
		
		
		MyPlayer player = ((StateOne) getState()).getPlayer();

		if(sitDown && !isWalking) {
			transform.setAngle(sitAngle);
		}
		
		if(!isWalking) {
			angryTime -= delta;
			if(calling > paciency) {
				tint = Color.WHITE.cpy();
			}
			else {
				tint = Color.WHITE.cpy().lerp(new Color(0.5f, 1, 0.5f, 1), Helper.clamp(calling, 0, paciency) / paciency);
			}
			
			calling -= delta;
			
			if(calling <= 0) {
				if(angryTime < 0) {
					//acabou o tempo
					player.reputation -= 10;
					calling = (float) (Math.random() * compulsivity + paciency);
					heyed = false;
					if(timeOver) {
						GameParticle gp = new GameParticle(info.withZ(5), body.getWorldCenter(), "Grr", font, Color.RED, 1);
						gp.setVelocity(Helper.randomUnit().scl(0.1f));
						gp.setDrag(0.2f);
						getState().addParticle(gp);
					}
					else {
						timeOver = true;
						
					}
				}
			}
			
			//Spawna a partícula de "Hey!"
			if(calling < paciency && ! heyed) {
				if(angryTime < 0) {
					heyPart = new GameParticle(info.withZ(5), body.getWorldCenter().cpy().add(40 / State.PHYS_SCALE, 20 / State.PHYS_SCALE), hey_tex, 0.7f, paciency);
					getState().addParticle(heyPart);
					heyed = true;
				}
			}
			
			if(grabPosition == null && ((StateOne) getState()).handles.size() > 0 && !sitDown) {
				grabPosition = ((StateOne) getState()).handles.get(0);
				for(Vector2 v : ((StateOne) getState()).handles) {
					if(
							v.cpy().scl(1/State.PHYS_SCALE).sub(body.getWorldCenter()).len2() 
							< grabPosition.cpy().scl(1/State.PHYS_SCALE).sub(body.getWorldCenter()).len2()) {
						grabPosition = v.cpy();
					}
				}
			}
			
			if(grabPosition != null && !sitDown) {
				transform.setAngle(grabPosition.cpy().scl(1/State.PHYS_SCALE).sub(body.getWorldCenter()).angle() - 90);
				
				arm.setArmEnd(grabPosition.cpy().scl(1/State.PHYS_SCALE));
				arm.setArmStart(body.getWorldCenter().cpy().add(Helper.newPolarVector(transform.getAngle(), 0.5f)));
			}
			
			body.applyForceToCenter(body.getWorldCenter().sub(originalPosition.cpy().scl(1/State.PHYS_SCALE)).scl(-strength), true);
			
			if(body.getWorldCenter().sub(originalPosition.cpy().scl(1/State.PHYS_SCALE)).len() > 0.3f) {
				if(angryTime < 0) {
					//Spawna a particula de "grr"
					angryTime = totalAngry;
					if(heyPart != null) {
						heyPart.setLife(0);
					}
					calling = 0;
					player.reputation -= 10;
					GameParticle gp = new GameParticle(info.withZ(5), body.getWorldCenter(), "Grr", font, Color.RED, 1);
					gp.setVelocity(Helper.randomUnit().scl(0.1f));
					gp.setDrag(0.2f);
					getState().addParticle(gp);
				}
			}
		}
		
		if(isWalking && path != null) {
			sitAngle = path.getTransform().getAngle();
			//chegou no ponto
			if(path.getPath().get(0).cpy().scl(1/State.PHYS_SCALE).sub(body.getWorldCenter()).len() < 0.5f) {
				if(path.getPath().size() == 1) {
					originalPosition = path.getPath().get(0).cpy();
					body.setTransform(originalPosition.cpy().scl(1/State.PHYS_SCALE), 0);
				}
				path.getPath().remove(0);
				
			
			}
			
			if(path.getPath().size() == 0) {
				isWalking = false;
			}
			
			if(path.getPath().size() > 0) {
				//move até o ponto
				body.applyForceToCenter(new Vector2(path.getPath().get(0).cpy().scl(1/State.PHYS_SCALE).sub(body.getWorldCenter()).nor().scl(5)), true);
				transform.setAngle(body.getLinearVelocity().angle() - 90);
				
				
			}
		}
		
		return false;
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		MyPlayer player = ((StateOne) getState()).getPlayer();
		if(player.getArm().getArmEnd().cpy().scl(1/State.PHYS_SCALE).sub(body.getWorldCenter()).len() < 15 / State.PHYS_SCALE) {
			if(calling < paciency) {
				if(player.mercadoria != -1){
					player.mercadoria = -1;
					player.addMoney(5);
					if(heyPart != null) {
						heyPart.setLife(0);
					}
					calling = 0;
					timeOver = false;
				}
			}
		}
		return false;
	}

	@Override
	public void dispose() {
		getState().deleteBody(body);
	}

	

}
