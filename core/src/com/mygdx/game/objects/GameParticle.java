package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.mygdx.game.helper.Helper;
import com.mygdx.game.structs.Transform;

public class GameParticle extends GameObject{


	//User defined
	Texture image;
	String text;
	Transform relative;
	BitmapFont font;
	Color fontColor;
	Vector2 velocity;
	Vector2 gravity;
	float life;
	float drag;
	
	//Interno	
	float globalTimer;
	GlyphLayout layout;
	
	private GameParticle(Vector2 position) {
		super(position);
		layout = new GlyphLayout();
		life = 1;
		velocity = new Vector2(0, 0);
		gravity = new Vector2(0, 0);
	}

	public GameParticle(Vector2 position, Texture texture) {
		this(position);
		setTexture(texture);
	}
	
	public GameParticle(Vector2 position, String text, BitmapFont font, Color color) {
		this(position);
		setText(text);
		setTextLayout(font, color);
	}
	
	public GameParticle(Vector2 position, String text, BitmapFont font, Color color, float life) {
		this(position, text, font, color);
		this.life = life;
	}
	
	public GameParticle(Vector2 position, String text, BitmapFont font) {
		this(position, text, font, Color.WHITE);
	}
	
	public GameParticle(Vector2 position, String text, BitmapFont font, float life) {
		this(position, text, font, Color.WHITE);
		this.life = life;
	}
	
	public void setTexture(Texture texture) {
		image = texture;
	}
	
	public void setRelativeTransform(Transform transform) {
		relative = transform;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public void setTextLayout(BitmapFont font, Color color) {
		this.font = font;
		this.fontColor = color;
	}

	public void render(SpriteBatch sb, ShapeRenderer sr, OrthographicCamera camera) {
		Helper.enableBlend();
		sb.setColor(1, 1, 1, Math.min(1, life - globalTimer));
		
		if(image != null) {
			renderTexture(sb, image);
		}

		if(text != null) {
			font.setColor(1, 1, 1, Math.min(1, life - globalTimer));
			layout.setText(font, text);
			font.draw(sb, text, transform.getPosition().x - layout.width/2f, transform.getPosition().y);
			font.setColor(1, 1, 1, 1);
		}
		
		sb.setColor(1, 1, 1, 1);
		Helper.disableBlend();
	}

	public boolean update(float delta) {
		globalTimer += delta;
		
		if(life - globalTimer <= 0) {
			return true;
		}
		
		transform.setPosition(transform.getPosition().add(velocity));
		velocity.add(gravity);
		
		velocity.scl((float) Math.exp(-drag));
		
		return false;
	}

	public Vector2 getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2 velocity) {
		this.velocity = velocity;
	}

	public Vector2 getGravity() {
		return gravity;
	}

	public void setGravity(Vector2 gravity) {
		this.gravity = gravity;
	}

	public float getLife() {
		return life;
	}

	public void setLife(float life) {
		this.life = life;
	}

	public float getDrag() {
		return drag;
	}

	public void setDrag(float drag) {
		this.drag = drag;
	}

	public Color getFontColor() {
		return fontColor;
	}

	public void setFontColor(Color fontColor) {
		this.fontColor = fontColor;
	}

}
