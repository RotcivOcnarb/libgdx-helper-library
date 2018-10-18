package com.mygdx.game.states;

import java.util.ArrayList;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.helper.Game;
import com.mygdx.game.helper.Position;
import com.mygdx.game.objects.GameObject;
import com.mygdx.game.objects.GameParticle;
import com.mygdx.game.phys.PhysHelp;

public abstract class State{
	
	public static final float PHYS_SCALE = 45f;
	
	StateManager manager;
	ShapeRenderer sr;
	OrthographicCamera camera;
	ArrayList<GameParticle> particles;
	ArrayList<GameObject> gos;
	
	//Física
	private World world;
	Box2DDebugRenderer b2dr;
	boolean debugDraw = false;
	ArrayList<Body> forRemoval;
	boolean pause = false;
	
	public void pausePhysics() {
		pause = true;
	}
	
	public void resumePhysics() {
		pause = false;
	}
	
	public void changeState(int nextState) {
		manager.changeState(nextState);
	}
	
	public void addParticle(GameParticle gp) {
		particles.add(gp);
	}
	
	public void putToUpdate(GameObject go) {
		gos.add(go);
	}
	
	public State(StateManager manager) {
		this.manager = manager;
		sr = new ShapeRenderer();
		camera = new OrthographicCamera();
		camera.setToOrtho(false);
		
		forRemoval = new ArrayList<Body>();
		particles = new ArrayList<GameParticle>();
		gos = new ArrayList<GameObject>();
	}
	
	public void enablePhysics(ContactListener listener) {
		setWorld(new World(new Vector2(0, -4f), true));
		getWorld().setContactListener(listener);
		b2dr = new Box2DDebugRenderer();
		camera.zoom = 1/PHYS_SCALE;
		camera.position.set(new Vector3(Position.CENTER.cpy().scl(1/PHYS_SCALE), 0));
	}
	
	public void setGravity(Vector2 gravity) {
		getWorld().setGravity(gravity);
	}
	
	public void enableDebugDraw() {
		debugDraw = true;
	}
	
	public void disableDebugDraw() {
		debugDraw = false;
	}
	
	public abstract void create();
	
	public void preRender(SpriteBatch sb) {
		sb.setProjectionMatrix(camera.combined);
	}
	
	public abstract void render(SpriteBatch sb);
	
	public void postRender(SpriteBatch sb) {
		
		sb.begin();
		for(int i = particles.size() -1; i >= 0; i --) {
			particles.get(i).render(sb, sr, camera);
		}
		sb.end();
		
		if(b2dr != null && debugDraw) {
			b2dr.render(getWorld(), camera.combined);
		}
	}
	
	
	
	public void preUpdate(float delta) {
		camera.update();
		
		for(int i = gos.size() - 1; i>= 0; i --) {
			gos.get(i).update(delta);
		}
		
		if(getWorld() != null) {
			if(!pause) {
				for(int i = forRemoval.size() -1; i >= 0; i --) {
					getWorld().destroyBody(forRemoval.get(i));
				}
				forRemoval.clear();
				
				getWorld().step(1/60f, 6, 2);
			}
		}
		
		
		for(int i = particles.size() -1; i >= 0; i --) {
			if(particles.get(i).update(delta)) {
				particles.remove(i);
			}
			

		}
	}
	
	public void deleteBody(Body body) {
		if(!forRemoval.contains(body)) {
			forRemoval.add(body);
		}
	}
	
	public Body addDynamicRectangleBody(Vector2 position, Vector2 size) {
		return PhysHelp.createDynamicBoxBody(getWorld(), position, size);
	}
	
	public Body addStaticRectangleBody(Vector2 position, Vector2 size) {
		return PhysHelp.createStaticBoxBody(getWorld(), position, size);
	}
	
	public abstract void update(float delta);
	public abstract void dispose();

	public boolean keyDown(int keycode) {
		for(int i = gos.size() - 1; i>= 0; i --) {
			gos.get(i).keyDown(keycode);
		}
		return false;
	}

	public boolean keyUp(int keycode) {
		for(int i = gos.size() - 1; i>= 0; i --) {
			gos.get(i).keyUp(keycode);
		}
		return false;
	}

	public boolean keyTyped(char character) {
		for(int i = gos.size() - 1; i>= 0; i --) {
			gos.get(i).keyTyped(character);
		}
		return false;
	}

	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		for(int i = gos.size() - 1; i>= 0; i --) {
			gos.get(i).touchDown(screenX, screenY, pointer, button);
		}
		return false;
	}

	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		for(int i = gos.size() - 1; i>= 0; i --) {
			gos.get(i).touchUp(screenX, screenY, pointer, button);
		}
		return false;
	}

	public boolean touchDragged(int screenX, int screenY, int pointer) {
		for(int i = gos.size() - 1; i>= 0; i --) {
			gos.get(i).touchDragged(screenX, screenY, pointer);
		}
		return false;
	}

	public boolean mouseMoved(int screenX, int screenY) {
		for(int i = gos.size() - 1; i>= 0; i --) {
			gos.get(i).mouseMoved(screenX, screenY);
		}
		return false;
	}

	public boolean scrolled(int amount) {
		for(int i = gos.size() - 1; i>= 0; i --) {
			gos.get(i).scrolled(amount);
		}
		return false;
	}

	public void connected(Controller controller) {
		for(int i = gos.size() - 1; i>= 0; i --) {
			gos.get(i).connected(controller);
		}
	}

	public void disconnected(Controller controller) {	
		for(int i = gos.size() - 1; i>= 0; i --) {
			gos.get(i).disconnected(controller);
		}
	}

	public boolean buttonDown(Controller controller, int buttonCode) {
		for(int i = gos.size() - 1; i>= 0; i --) {
			gos.get(i).buttonDown(controller, buttonCode);
		}
		return false;
	}

	public boolean buttonUp(Controller controller, int buttonCode) {
		for(int i = gos.size() - 1; i>= 0; i --) {
			gos.get(i).buttonUp(controller, buttonCode);
		}
		return false;
	}

	public boolean axisMoved(Controller controller, int axisCode, float value) {
		for(int i = gos.size() - 1; i>= 0; i --) {
			gos.get(i).axisMoved(controller, axisCode, value);
		}
		return false;
	}

	public boolean povMoved(Controller controller, int povCode, PovDirection value) {
		for(int i = gos.size() - 1; i>= 0; i --) {
			gos.get(i).povMoved(controller, povCode, value);
		}
		return false;
	}

	public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
		for(int i = gos.size() - 1; i>= 0; i --) {
			gos.get(i).xSliderMoved(controller, sliderCode, value);
		}
		return false;
	}

	public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
		for(int i = gos.size() - 1; i>= 0; i --) {
			gos.get(i).ySliderMoved(controller, sliderCode, value);
		}
		return false;
	}

	public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
		for(int i = gos.size() - 1; i>= 0; i --) {
			gos.get(i).accelerometerMoved(controller, accelerometerCode, value);
		}
		return false;
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}
	
}
