package com.mygdx.game.phys;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.states.State;

public class PhysHelp {
	
	
	//BOXES
	public static Body createDynamicBoxBody(World world, Vector2 position, Vector2 size) {
		
		BodyDef def = new BodyDef();
		def.position.set(position.cpy().scl(1/State.PHYS_SCALE));
		def.type = BodyType.DynamicBody;
		
		Body b =  world.createBody(def);
		
		createBoxFixture(b, Vector2.Zero, size);
		
		return b;
	}

	public static Body createStaticBoxBody(World world, Vector2 position, Vector2 size) {
		
		BodyDef def = new BodyDef();
		def.position.set(position.cpy().scl(1/State.PHYS_SCALE));
		def.type = BodyType.StaticBody;
		
		Body b =  world.createBody(def);
		
		createBoxFixture(b, Vector2.Zero, size);
		
		return b;
	}
	
	public static Body createBoxBody(World world, Vector2 size, BodyDef def) {
		Body b =  world.createBody(def);
		createBoxFixture(b, Vector2.Zero, size);
		return b;
	}
	
	public static Body creatCircleBody(World world, float radius, BodyDef def) {
		Body b =  world.createBody(def);
		createCircleFixture(b, Vector2.Zero, radius);
		return b;
	}
	
	//BALLS
	public static Body createDynamicCircleBody(World world, Vector2 position, float radius) {
		
		BodyDef def = new BodyDef();
		def.position.set(position.cpy().scl(1/State.PHYS_SCALE));
		def.type = BodyType.DynamicBody;
		
		Body b =  world.createBody(def);
		
		createCircleFixture(b, Vector2.Zero, radius);
		
		return b;
	}
	
	public static Body createStaticCircleBody(World world, Vector2 position, float radius) {
		
		BodyDef def = new BodyDef();
		def.position.set(position.cpy().scl(1/State.PHYS_SCALE));
		def.type = BodyType.StaticBody;
		
		Body b =  world.createBody(def);
		
		createCircleFixture(b, Vector2.Zero, radius);
		
		return b;
	}
	//FIXTURES
	
	public static Fixture createCircleFixture(Body body, Vector2 relative, float radius) {
		CircleShape shape = new CircleShape();
		shape.setPosition(relative.cpy().scl(1/State.PHYS_SCALE));
		shape.setRadius(radius / State.PHYS_SCALE);
		return body.createFixture(shape, 1);
	}
	
	public static Fixture createBoxFixture(Body body, Vector2 relative, Vector2 size) {
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(size.x/2 / State.PHYS_SCALE, size.y/2 / State.PHYS_SCALE, relative.cpy().scl(1/State.PHYS_SCALE), 0);
		return body.createFixture(shape, 1);
	}

}
