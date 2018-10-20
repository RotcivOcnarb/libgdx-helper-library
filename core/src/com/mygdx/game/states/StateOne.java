package com.mygdx.game.states;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.objects.ObjectInfo;
import com.mygdx.game.objects.TmxRenderer;
import com.mygdx.game.test.MyPlayer;

public class StateOne extends State{
	
	TmxRenderer mapRenderer;
	MyPlayer player;
	
	public ArrayList<Vector2> handles;
	
	public StateOne(StateManager manager) {
		super(manager);
	}

	public void create() {
		enablePhysics(new StateOneListener(this));
		enableDebugDraw();
		setGravity(new Vector2(0, 0));
		
		handles = new ArrayList<Vector2>();
		
		mapRenderer = new TmxRenderer(new ObjectInfo(this, 0, 4f), "maps/trem.tmx");
		mapRenderer.instanceObjects();
		
		
		player = (MyPlayer) mapRenderer.getInstancedObject(91);
		
		
	}

	public void render(SpriteBatch sb) {
		mapRenderer.render(sb, sr, camera);
	}

	public void update(float delta) {
		mapRenderer.update(delta);
		
		camera.position.add(
				new Vector3(player.getBodyPosition().cpy().sub(camera.position.x, camera.position.y).scl(1/15f), 0)
				);
		
	}

	public void dispose() {
		
	}

}
