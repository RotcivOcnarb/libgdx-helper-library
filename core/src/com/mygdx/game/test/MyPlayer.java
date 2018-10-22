package com.mygdx.game.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.helper.Helper;
import com.mygdx.game.objects.GameParticle;
import com.mygdx.game.objects.ObjectInfo;
import com.mygdx.game.objects.TopDownPlayer;
import com.mygdx.game.states.State;
import com.mygdx.game.structs.Transform;

public class MyPlayer extends TopDownPlayer{
	
	Texture texture;
	Texture sacolaTexture;
	Body sacola_body;
	Transform sacolaTransform;
	Arm arm;
	
	Texture[] mercadorias;
	
	int money = 0;
	int reputation = 1000;
	int mercadoria = -1;
	
	public MyPlayer(ObjectInfo info, MapProperties properties) {
		super(info, properties);
		setSpeed(5);
		
		texture = new Texture("arm/person.png");
		
		sacola_body = Helper.PhysHelp.createDynamicCircleBody(info.getState().getWorld(), get("sacola_pos", Vector2.class), get("width", Float.class)/2f, true);
		sacola_body.setLinearDamping(10f);
		
		Helper.PhysHelp.ropeJoinBodies(body, sacola_body, 2);
		
		sacolaTexture = new Texture("arm/sacola.png");
		
		sacolaTransform = new Transform(Vector2.Zero.cpy());
		
		arm = new Arm(info.withZ(4), body.getWorldCenter(), Helper.Game.mouseWorldPosition(info.getState().getCamera()));
		info.getState().putInScene(arm);

		setKeyUp(Keys.W);
		setKeyLeft(Keys.A);
		setKeyDown(Keys.S);
		setKeyRight(Keys.D);
		
		mercadorias = new Texture[4];
		
		for(int i = 0; i < 4; i ++) {
			mercadorias[i] = new Texture("arm/mercadoria" + i + ".png");
		}
	}
	
	@Override
	public boolean update(float delta) {
		super.update(delta);
		transform.setAngle(Helper.Game.mouseWorldPosition(getState().getCamera()).cpy().sub(body.getWorldCenter()).angle() - 90);
		sacolaTransform.setAngle(body.getWorldCenter().cpy().sub(sacola_body.getWorldCenter()).angle() - 90);
		arm.setArmStart(body.getWorldCenter().add(Helper.newPolarVector(transform.getAngle(), 0.5f)));
		Vector2 toMouse = Helper.Game.mouseWorldPosition(getCamera()).cpy().sub(body.getWorldCenter());
		arm.setArmEnd(body.getWorldCenter().add(Helper.newPolarVector(toMouse.angle(), Math.min(toMouse.len(), 1.5f))));
		
		return false;
	}


	public void render(SpriteBatch sb, ShapeRenderer sr, OrthographicCamera camera) {
		sb.end();
		
		sr.begin(ShapeType.Filled);
		sr.setColor(Color.ORANGE);
			sr.rectLine(body.getWorldCenter(), sacola_body.getWorldCenter(), 0.05f);
		
		sr.end();
		
		sb.begin();
		
		renderBodyTexture(sb, texture, body);
		renderBodyTexture(sb, sacolaTexture, sacola_body, sacolaTransform);
		
		if(mercadoria != -1) {
			Helper.renderTex(sb, mercadorias[mercadoria], arm.getArmEnd().cpy().scl(1/State.PHYS_SCALE), transform.getAngle() + 90, new Vector2(2/State.PHYS_SCALE, 2/State.PHYS_SCALE), false, false);
		}
		
		Helper.drawUIFont(sb, Person.font, camera, "Money: $" + money, new Vector2(10, Gdx.graphics.getHeight() - 10));
		Helper.drawUIFont(sb, Person.font, camera, "Reputation: " + (int)((reputation / 1000f) * 100f) + "%", new Vector2(10, Gdx.graphics.getHeight() - 40));
		Helper.drawUIFont(sb, Person.font, camera, "FPS: " + 1/Gdx.graphics.getDeltaTime(), new Vector2(10, Gdx.graphics.getHeight() - 70));
		
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if(arm.getArmEnd().cpy().scl(1/State.PHYS_SCALE).sub(sacola_body.getWorldCenter()).len() < 15 / State.PHYS_SCALE) {
			if(mercadoria == -1) {
				mercadoria = (int)(Math.random() * 4);
			}
		}
		return false;
	}

	public Arm getArm() {
		return arm;
	}

	public void addMoney(int money) {
		this.money += money;
		
		GameParticle gp = new GameParticle(info.withZ(4), body.getWorldCenter(), "+ $" + money, Person.font, Color.GREEN);
		gp.setVelocity(Vector2.Y.cpy().scl(0.3f));
		gp.setDrag(0.2f);
		
		getState().addParticle(gp);
	}

	@Override
	public void dispose() {
		getState().deleteBody(body);
		getState().deleteBody(sacola_body);
	}

	@Override
	public void create() {
		// TODO Auto-generated method stub
		
	}

}
