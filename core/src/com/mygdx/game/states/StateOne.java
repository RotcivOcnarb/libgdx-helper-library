package com.mygdx.game.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.objects.TmxRenderer;

public class StateOne extends State{
	
	TmxRenderer mapRenderer;
	public StateOne(StateManager manager) {
		super(manager);
	}

	public void create() {
		enablePhysics(new StateOneListener(this));
		enableDebugDraw();
		setGravity(new Vector2(0, -30));
		
		mapRenderer = new TmxRenderer(this, "maps/trem.tmx", 3);
		mapRenderer.instanceObjects();
		
		putToUpdate(mapRenderer);
	}

	public void render(SpriteBatch sb) {
		mapRenderer.render(sb, sr, camera);
	}

	public void update(float delta) {
		
	}

	public void dispose() {
		
	}

}
