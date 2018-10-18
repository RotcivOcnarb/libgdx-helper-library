package com.mygdx.game.objects;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.helper.Helper;
import com.mygdx.game.states.State;

import net.dermetfan.gdx.physics.box2d.Box2DMapObjectParser;

public class TmxRenderer extends GameObject{
	
	TiledMap tiledMap;
	float scale;
	Vector2 scaleVector;
	Box2DMapObjectParser parser;
	HashMap<String, GameObject> instancedObjects;
	ArrayList<TmxInstancedKeyword> keywords;
	State state;
	
	public TmxRenderer(State state, String mapPath, float scale) {
		super(Vector2.Zero);
		this.state = state;
		parser = new Box2DMapObjectParser(scale/State.PHYS_SCALE);
		instancedObjects = new HashMap<String, GameObject>();
		keywords = new ArrayList<TmxInstancedKeyword>();
		loadDefaultKeywords();
		this.scale = scale;
		tiledMap = new TmxMapLoader().load(mapPath);
		scaleVector = new Vector2(scale / State.PHYS_SCALE, scale / State.PHYS_SCALE);
		Iterator<MapLayer> layers = tiledMap.getLayers().iterator();
		while(layers.hasNext()) {
			MapLayer layer = layers.next();
			
			parser.load(state.getWorld(), layer);
	
			if(layer.getProperties().get("block") == null) continue;
			if(layer.getProperties().get("block", Boolean.class)) {
				
				TiledMapTileLayer tileLayer = (TiledMapTileLayer) layer;

				Vector2 tileSize = new Vector2(tileLayer.getTileWidth(), tileLayer.getTileHeight());
					
				for(int x = 0; x < tileLayer.getWidth(); x ++) {
					for(int y = 0; y < tileLayer.getHeight(); y ++) {
						Cell cell = tileLayer.getCell(x, y);
						if(cell != null) {
							Vector2 pos = new Vector2(
									(x * tileSize.x) * scale,
									(y * tileSize.y) * scale
									);
							state.addStaticRectangleBody(pos.cpy().add(tileSize.cpy().scl(1/2f)), tileSize.cpy().scl(scale));
						}
					}
					
				}
			}
		}
	}
	
	public void instanceObjects() {
		Iterator<MapLayer> layers = tiledMap.getLayers().iterator();
		while(layers.hasNext()) {
			MapLayer layer = layers.next();
			
			parser.load(state.getWorld(), layer);
			
			MapObjects mos = layer.getObjects();
			
			for(int k = 0; k < mos.getCount(); k ++) {
				MapProperties props =  mos.get(k).getProperties();
				
				String objClass = props.get("class", String.class);
				if(objClass != null) {
					
					
					
					try {
						//Pega a classe que vai ser instanciada
						Class goClass = Class.forName(objClass);
						//Pega a quantidade de argumentos do construtor
						int numFields = props.get("constructorFields", Integer.class);
						System.out.println("Classe com " + numFields + " valores pra passar pro construtor");
						//Pega todas as classes da cada argumento do construtor
						Class constructorTypes[] = new Class[numFields];
						for(int i = 0; i < numFields; i ++) {
							
							constructorTypes[i] = Class.forName(props.get("field" + (i+1), String.class));
							System.out.println("Valor " + (i+1) + ": " + constructorTypes[i].getName());
						}
						
						//Pega os valores dos argumentos pra serem passados
						
						//TODO: Fazer uma interface pra essa porra
						
						Object[] fields = new Object[numFields];
						for(int i = 0; i < numFields; i ++) {
							Object obj = null;
							
							boolean found = false;
							
							for(TmxInstancedKeyword tik : keywords) {
								if(props.get("fieldValue" + (i+1), String.class).startsWith(tik.getKeyword())) {
									if(props.get("fieldValue" + (i+1), String.class).endsWith("_")) {
										obj = tik.getObject(layer.getObjects().get(props.get("fieldValue" + (i+1), String.class).split("_")[1]));
										found = true;
										break;
									}
									else {
										obj = tik.getObject(mos.get(k));
										found = true;
										break;
									}
									
									
								}
							}
							
							if(!found)
							obj = props.get("fieldValue" + (i+1));
							
							fields[i] = obj;
						}
						
						GameObject go = (GameObject) goClass.getConstructor(constructorTypes).newInstance(fields);
						state.putToUpdate(go);
						System.out.println("Botando objeto " + mos.get(k).getName() + " na lista");
						instancedObjects.put(mos.get(k).getName(), go);
						
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		}
	}
	
	public void loadDefaultKeywords() {
		addKeywordInterpreter(new TmxInstancedKeyword("{world}") {
			public Object getObject(MapObject mo) {
				return state.getWorld();
			}
		});
		addKeywordInterpreter(new TmxInstancedKeyword("{position}") {
			public Object getObject(MapObject mo) {
				return new Vector2(mo.getProperties().get("x", Float.class) * scale, mo.getProperties().get("y", Float.class) * scale);
			}
		});
		addKeywordInterpreter(new TmxInstancedKeyword("{rotation}") {
			public Object getObject(MapObject mo) {
				return mo.getProperties().get("angle", Float.class);
			}
		});
		addKeywordInterpreter(new TmxInstancedKeyword("{size}") {
			public Object getObject(MapObject mo) {
				return new Vector2(mo.getProperties().get("width", Float.class) * scale, mo.getProperties().get("height", Float.class) * scale);
			}
		});
		addKeywordInterpreter(new TmxInstancedKeyword("{state}") {
			public Object getObject(MapObject mo) {
				return state;
			}
		});
		addKeywordInterpreter(new TmxInstancedKeyword("{center}") {
			public Object getObject(MapObject mo) {
				return new Vector2(
						(mo.getProperties().get("x", Float.class) + mo.getProperties().get("width", Float.class)/2) * scale,
						(mo.getProperties().get("y", Float.class) + mo.getProperties().get("height", Float.class)/2) * scale
						);
			}
		});

	}
	
	public void addKeywordInterpreter(TmxInstancedKeyword tik) {
		keywords.add(tik);
	}
	
	public GameObject getInstancedObject(String name) {
		return instancedObjects.get(name);
	}

	Vector2 positionTemp = new Vector2(0, 0);
	public void render(SpriteBatch sb, ShapeRenderer sr, OrthographicCamera camera) {

		sb.begin();
		for(int i = 0; i < tiledMap.getLayers().getCount(); i ++) {
			MapLayer layer = tiledMap.getLayers().get(i);
			
			if(layer instanceof TiledMapTileLayer) {
				
				TiledMapTileLayer tileLayer = (TiledMapTileLayer) layer;

				Vector2 tileSize = new Vector2(tileLayer.getTileWidth(), tileLayer.getTileHeight());
					
				//Desenha os tiles
				for(int x = 0; x < tileLayer.getWidth(); x ++) {
					for(int y = 0; y < tileLayer.getHeight(); y ++) {
						Cell cell = tileLayer.getCell(x, y);
						if(cell != null) {
							positionTemp.set(
									(x * tileSize.x) * scale / State.PHYS_SCALE,
									(y * tileSize.y) * scale / State.PHYS_SCALE
									);
							
							
							Helper.renderRegion(
									sb,
									cell.getTile().getTextureRegion(),
									positionTemp.cpy().add(tileSize.cpy().scl(1/2f / State.PHYS_SCALE)),
									cell.getRotation(),
									scaleVector,
									cell.getFlipHorizontally(),
									cell.getFlipVertically());

						}
					}
					
				}
			}
			else {
				MapObjects mos = layer.getObjects();
				
				//Desenha as imagens
				for(int k = 0; k < mos.getCount(); k ++) {
					if(mos.get(k) instanceof TiledMapTileMapObject) {
						TiledMapTileMapObject imgObj = (TiledMapTileMapObject) mos.get(k);
						Helper.renderRegion(
								sb,
								imgObj.getTile().getTextureRegion(),
								new Vector2(imgObj.getX(), imgObj.getY()).cpy().scl(scale/State.PHYS_SCALE),
								360 - imgObj.getRotation(),
								new Vector2(scale * imgObj.getScaleX() / State.PHYS_SCALE, scale * imgObj.getScaleY() / State.PHYS_SCALE),
								imgObj.isFlipHorizontally(),
								imgObj.isFlipVertically(),
								new Vector2(scale * imgObj.getOriginX() / State.PHYS_SCALE, scale * imgObj.getOriginY() / State.PHYS_SCALE)
								);
						;
					}
				}
			}
		}
		sb.end();
	}
	
	public Vector2 getPositionFromObject(String objectName) {
		for(int i = 0; i < tiledMap.getLayers().getCount(); i ++) {
			MapLayer layer = tiledMap.getLayers().get(i);
			MapObject obj = layer.getObjects().get(objectName);
			if(obj != null) {
				RectangleMapObject posObj = (RectangleMapObject) obj;
				Vector2 position = new Vector2();
				posObj.getRectangle().getPosition(position);
				return position.scl(scale);
			}
		}
		
		return null;
	}

	public boolean update(float delta) {
		return false;
	}

}
