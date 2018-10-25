package com.mygdx.game.states;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class StateOne extends State{

	public StateOne(StateManager manager) {
		super(manager);
	}
	
	public void create() {
		enablePhysics(new StateOneListener(this));
		enableDebugDraw();
		setGravity(new Vector2(0, -20));
		
		setTmxMap("maps/test.tmx", 1);
	}

	public void render(SpriteBatch sb) {
		
	}

	public void update(float delta) {		
	
	}

	public void dispose() {
		
	}

}
