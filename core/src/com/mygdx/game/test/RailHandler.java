package com.mygdx.game.test;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.objects.GameObject;
import com.mygdx.game.states.State;
import com.mygdx.game.states.StateOne;

public class RailHandler extends GameObject{
	
	
	public RailHandler(MapObject mo, State state, Float scale) {
		super(Vector2.Zero);
				
		PolylineMapObject pmo = (PolylineMapObject) mo;
		
		float[] verts = pmo.getPolyline().getTransformedVertices();
				
		for(int i = 0; i < verts.length / 2; i ++) {
			Vector2 point = new Vector2(verts[i*2] * scale, verts[i*2+1] * scale);
			
			((StateOne) state).handles.add(point);
		}
		
	}

	public void render(SpriteBatch sb, ShapeRenderer sr, OrthographicCamera camera) {

	}

	public boolean update(float delta) {
		return false;
	}

}
