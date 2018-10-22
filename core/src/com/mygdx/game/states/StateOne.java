package com.mygdx.game.states;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.helper.Helper;
import com.mygdx.game.test.MyPlayer;
import com.mygdx.game.test.PersonPath;

public class StateOne extends State{
	
	MyPlayer player;
	
	public ArrayList<Vector2> handles;
	public ArrayList<PersonPath> paths;
	public ArrayList<Vector2> remainingStandupPoints;
	public ArrayList<Vector2> allStandupPoints;
	
	public StateOne(StateManager manager) {
		super(manager);
	}
	
	public void create() {
		enablePhysics(new StateOneListener(this));
		//enableDebugDraw();
		setGravity(new Vector2(0, 0));
		
		handles = new ArrayList<Vector2>();
		paths = new ArrayList<PersonPath>();
		setStandupPoints(new ArrayList<Vector2>());
		//enableLights();
		setTmxMap("maps/trem.tmx", 4);
		//updateTmxLightInfo();
		
		
		player = (MyPlayer) getTmxRenderer().getInstancedObject(91);
		
		
	}
	
	public MyPlayer getPlayer() {
		return player;
	}

	public void render(SpriteBatch sb) {
		
	}

	public void update(float delta) {		
		camera.position.add(
				new Vector3(player.getBodyPosition().cpy().sub(camera.position.x, camera.position.y).scl(1/15f), 0)
				);
		
	}

	public void dispose() {
		
	}

	public void setStandupPoints(ArrayList<Vector2> standupPoints) {
		this.allStandupPoints = Helper.vector2ListHardClone(standupPoints);
		this.remainingStandupPoints = Helper.vector2ListHardClone(standupPoints);
	}

}
