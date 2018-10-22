package com.mygdx.game.test;

import java.util.ArrayList;
import java.util.function.UnaryOperator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.helper.Helper;
import com.mygdx.game.objects.AStarPathFinder;
import com.mygdx.game.objects.GameObject;
import com.mygdx.game.objects.ObjectInfo;
import com.mygdx.game.objects.Triangulator;
import com.mygdx.game.states.State;
import com.mygdx.game.states.StateOne;

public class TriangleTest extends GameObject{
	
	ArrayList<Vector2> points;
	ArrayList<Triangulator.TriangulatedNode> nodes;
	
	public TriangleTest(ObjectInfo info, MapProperties properties) {
		super(info.withZ(1), properties);
		
		PolygonMapObject pmo = (PolygonMapObject) get("this");
		
		points = Helper.floatArrayToVectorList(pmo.getPolygon().getTransformedVertices());
		points.replaceAll(new UnaryOperator<Vector2>() {

			public Vector2 apply(Vector2 v) {
				return v.scl(getScale()  / State.PHYS_SCALE);
			}
			
		});
		
		setToRender(false);
		
		((StateOne) getState()).setStandupPoints(points);
		info.setZ(5);
		
		Triangulator triangulator = new Triangulator(Helper.vector2ListHardClone(points));
		triangulator.setThreshold(0.05f);
		
		nodes = triangulator.triangulate();
		System.out.println(nodes.size());

//		for(Triangulator.TriangulatedNode node : nodes) {
//			System.out.println(node);
//			for(Triangulator.TriangulatedNode cons : node.connections) {
//				System.out.println("\t" + cons.point);
//			}
//			
//		}
	
	}

	public void dispose() {
		
	}

	public void render(SpriteBatch sb, ShapeRenderer sr, OrthographicCamera camera) {
		sb.end();
		
		
		sr.begin(ShapeType.Filled);
		
		/*
		ArrayList<Triangulator.TriangulatedNode> closedList = new ArrayList<Triangulator.TriangulatedNode>();
		ArrayList<Triangulator.TriangulatedNode> openList = new ArrayList<Triangulator.TriangulatedNode>();
		
		openList.add(nodes.get(0));
	
		while(!openList.isEmpty()) {
			for(int j = 0; j < openList.get(0).connections.size(); j ++) {
				if(!closedList.contains(openList.get(0).connections.get(j))) {
					sr.setColor(Color.ORANGE);
					sr.rectLine(openList.get(0).point, openList.get(0).connections.get(j).point, 0.05f);
					openList.add(openList.get(0).connections.get(j));
				}
			}
			closedList.add(openList.get(0));
			openList.remove(0);
		}
		*/
		sr.setColor(Color.ORANGE);

		for(Triangulator.TriangulatedNode node : nodes) {
			
			for(Triangulator.TriangulatedNode cons : node.connections) {
				sr.rectLine(node.point, cons.point, 0.05f);
				
				if(node.point.cpy().sub(cons.point).len() > 1000) {
//					System.out.println(node.point);
//					System.out.println(cons.point);
				}
			}
			
		}
		

		for(int i = 0; i < nodes.size(); i ++) {
			sr.setColor(Color.RED);
			sr.circle(nodes.get(i).point.x, nodes.get(i).point.y, 0.1f, 20);
		}
		
		sr.end();
		
		sb.begin();
	}

	public boolean update(float delta) {
		return false;
	}

	@Override
	public void create() {
		// TODO Auto-generated method stub
		
	}

}
