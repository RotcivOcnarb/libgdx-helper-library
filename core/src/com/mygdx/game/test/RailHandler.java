package com.mygdx.game.test;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.objects.GameObject;
import com.mygdx.game.objects.ObjectInfo;
import com.mygdx.game.states.State;
import com.mygdx.game.states.StateOne;

public class RailHandler extends GameObject{
	
	
	public RailHandler(ObjectInfo info, MapProperties properties) {
		super(info, properties);
		
		System.out.println("RAILD: " + get("polyline"));
		PolylineMapObject pmo = (PolylineMapObject) get("polyline", MapObject.class);
		
		float[] verts = pmo.getPolyline().getTransformedVertices();
				
		for(int i = 0; i < verts.length / 2; i ++) {
			Vector2 point = new Vector2(verts[i*2] * info.getScale(), verts[i*2+1] * info.getScale());
			
			((StateOne) getState()).handles.add(point);
		}
		
	}

	public void render(SpriteBatch sb, ShapeRenderer sr, OrthographicCamera camera) {

	}

	public boolean update(float delta) {
		return false;
	}

}
