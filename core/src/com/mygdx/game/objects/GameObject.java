package com.mygdx.game.objects;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.game.helper.Game;
import com.mygdx.game.helper.Helper;
import com.mygdx.game.states.State;
import com.mygdx.game.structs.Transform;

public abstract class GameObject {
	
	protected Transform transform;
	
	public GameObject(Vector2 position) {
		transform = new Transform(position);
	}
	
	public abstract void render(SpriteBatch sb, ShapeRenderer sr, OrthographicCamera camera);
	public abstract boolean update(float delta);
	
	protected void renderRegion(SpriteBatch sb, TextureRegion region) {
		Helper.renderRegion(sb, region, transform, false, false);
	}
	
	protected void renderRegion(SpriteBatch sb, TextureRegion region, boolean flipX, boolean flipY) {
		Helper.renderRegion(sb, region, transform, flipX, flipY);
	}
	
	protected void renderAnimationBody(SpriteBatch sb, Animation<TextureRegion> animation, Body body, float timer, boolean flipX, boolean flipY) {
		TextureRegion region = animation.getKeyFrame(Game.globalTimer);
		
		renderBodyRegion(sb, region, body, flipX, flipY);
	}
	
	/**Renders the texture using the object transform, with the origin as the center of the texture
	 * 
	 * @param sb The SpriteBatch used to render
	 * @param tex The texture to render
	 */
	protected void renderTexture(SpriteBatch sb, Texture tex) {
		Helper.renderTex(sb, tex, transform, false, false);
	}
	
	/**Renders the texture using the object transform, with the origin as the center of the texture
	 * 
	 * @param sb The SpriteBatch used to render
	 * @param tex The texture to render
	 * @param flipX if the texture is flipped on the X axis
	 * @param flipY if the texture ir flipped in the Y axis
	 */
	protected void renderTexture(SpriteBatch sb, Texture tex, boolean flipX, boolean flipY) {
		Helper.renderTex(sb, tex, transform, flipX, flipY);
	}
	
	protected void renderBodyRegion(SpriteBatch sb, TextureRegion region, Body body) {
		renderBodyRegion(sb, region, body, false, false);
	}
	
	protected void renderBodyRegion(SpriteBatch sb, TextureRegion region, Body body, boolean flipX, boolean flipY) {
		Helper.renderRegion(sb, region, body.getWorldCenter().cpy().add(transform.getPosition().cpy().scl(1/State.PHYS_SCALE)), (float)Math.toDegrees(body.getAngle()) + transform.getAngle(), transform.getScale().cpy().scl(1/State.PHYS_SCALE), flipX, flipY);
	}
	
	/**Renders the texture to match the body transform (except scale). The transform of the object is used as a relative transform
	 * (if you want to draw at the exact center, be sure to set the transform position to (0, 0)
	 * 
	 * @param sb The SpriteBatch used to render
	 * @param texture The texture to render
	 * @param body The body used as reference transform
	 */
	protected void renderBodyTexture(SpriteBatch sb, Texture texture, Body body) {
		renderBodyTexture(sb, texture, body, false, false);
	}
	/**Renders the texture to match the body transform (except scale). The transform of the object is used as a relative transform
	 * (if you want to draw at the exact center, be sure to set the transform position to (0, 0)
	 * 
	 * @param sb The SpriteBatch used to render
	 * @param texture The texture to render
	 * @param body The body used as reference transform
	 * @param flipX if the texture is flipped on the X axis
	 * @param flipY if the texture ir flipped in the Y axis
	 */
	protected void renderBodyTexture(SpriteBatch sb, Texture texture, Body body, boolean flipX, boolean flipY) {
		Helper.renderTex(sb, texture, body.getWorldCenter().add(transform.getPosition()), (float)Math.toDegrees(body.getAngle()) + transform.getAngle(), transform.getScale().cpy().scl(1/State.PHYS_SCALE), flipX, flipY);
	}
	
	//Inputs
	

	public Transform getTransform() {
		return transform;
	}

	public void setTransform(Transform transform) {
		this.transform = transform;
	}
	
	public void setPosition(Vector2 position) {
		transform.setPosition(position);
	}
	
	public void setScale(Vector2 scale) {
		transform.setScale(scale);
	}
	
	public void setAngle(float angle) {
		transform.setAngle(angle);
	}

	public boolean keyDown(int keycode) {
		return false;
	}

	public boolean keyUp(int keycode) {
		return false;
	}

	public boolean keyTyped(char character) {
		return false;
	}

	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	public boolean scrolled(int amount) {
		return false;
	}

	public void connected(Controller controller) {
	}

	public void disconnected(Controller controller) {		
	}

	public boolean buttonDown(Controller controller, int buttonCode) {
		return false;
	}

	public boolean buttonUp(Controller controller, int buttonCode) {
		return false;
	}

	public boolean axisMoved(Controller controller, int axisCode, float value) {
		return false;
	}

	public boolean povMoved(Controller controller, int povCode, PovDirection value) {
		return false;
	}

	public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
		return false;
	}

	public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
		return false;
	}

	public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
		return false;
	}

}
