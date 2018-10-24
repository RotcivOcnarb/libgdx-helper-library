package com.mygdx.game.states;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.helper.Helper.Position;
import com.mygdx.game.helper.Helper.Size;
import com.mygdx.game.objects.ObjectInfo;
import com.mygdx.game.test.MyPlayer;

public class StateOne extends State{
	
	MyPlayer player;
	
	public StateOne(StateManager manager) {
		super(manager);
	}
	
	public void create() {
		enablePhysics(new StateOneListener(this));
		enableDebugDraw();
		setGravity(new Vector2(0, -20));
		
		player = new MyPlayer(new ObjectInfo(this, 0, 1f), Position.CENTER, new Vector2(30, 40));
		player.setJumpStrength(10);
		player.setSpeed(7);
		player.setTotalJumps(1);
		
		putInScene(player);
		
		addStaticRectangleBody(Position.CENTERX, Size.WIDTH.cpy().add(0, 100));
	}

	public void render(SpriteBatch sb) {
		
	}

	public void update(float delta) {		
	
	}

	public void dispose() {
		
	}

}
