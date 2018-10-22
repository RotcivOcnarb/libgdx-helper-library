package com.mygdx.game.test;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.objects.GameObject;
import com.mygdx.game.objects.ObjectInfo;
import com.mygdx.game.states.StateOne;

public class PersonPath extends GameObject{
	
	ArrayList<Vector2> points;
	int location;

	public PersonPath(ObjectInfo info, MapProperties properties) {
		super(info, properties);
		
		PolylineMapObject myself = (PolylineMapObject) get("object", MapObject.class);
		
		((StateOne) getState()).paths.add(this);
		
		location = get("location", Integer.class);
		
		transform.setAngle(get("angle", Float.class));
		points = new ArrayList<Vector2>();
		
		float[] ps = myself.getPolyline().getTransformedVertices();
		for(int i = 0; i < ps.length/2; i ++) {
			
			points.add(new Vector2(ps[i*2] * info.getScale(), ps[i*2+1] * info.getScale()));
		}
		
		setToRender(false);
	}
	
	public PersonPath(ObjectInfo info, ArrayList<Vector2> points, int location) {
		super(info, new MapProperties());
		
		this.location = location;
		this.points = points;
		
		setToRender(false);
	}

	public void dispose() {
		
	}

	public void render(SpriteBatch sb, ShapeRenderer sr, OrthographicCamera camera) {
		
	}

	public boolean update(float delta) {
		return false;
	}

	public ArrayList<Vector2> getPath() {
		return points;
	}

	@Override
	public void create() {
		// TODO Auto-generated method stub
		
	}

}
