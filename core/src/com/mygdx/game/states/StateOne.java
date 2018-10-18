package com.mygdx.game.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.objects.TmxRenderer;
import com.mygdx.game.test.MyPlayer;

public class StateOne extends State{
	
	TmxRenderer mapRenderer;
	MyPlayer player;
	
	public StateOne(StateManager manager) {
		super(manager);
	}

	public void create() {
		enablePhysics(new StateOneListener(this));
		enableDebugDraw();
		setGravity(new Vector2(0, 0));
		
		mapRenderer = new TmxRenderer(this, "maps/untitled.tmx", 1);
		mapRenderer.instanceObjects();
		
		player = (MyPlayer) mapRenderer.getInstancedObject("Player");
		System.out.println(player);
		
		putToUpdate(mapRenderer);
	}

	public void render(SpriteBatch sb) {
		mapRenderer.render(sb, sr, camera);
		player.render(sb, sr, camera);
		
	}

	public void update(float delta) {
		camera.position.add(
				new Vector3(player.getBodyPosition().cpy().sub(camera.position.x, camera.position.y).scl(1/15f), 0)
				);
	}

	public void dispose() {
		
	}

}
