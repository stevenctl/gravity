package com.sugarware.gravity.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.sugarware.gravity.entities.MapBodyBuilder;

public abstract class GameState {
	
	//Constants
	static final float TIME_STEP = 1/60f;
	static final int VELOCITY_ITERATIONS = 6, POSITION_ITERATIONS = 2;
	
	public float w, h;
	public World world;
	private Vector2 gVector;
	public float gTheta;
	public float gVal;
	public OrthographicCamera cam;
	public TiledMap tilemap;
	private OrthogonalTiledMapRenderer mapRenderer;
	
	public GameState(String map_path, float g_theta, float g_val){
		gVector = new Vector2();
		gTheta = g_theta;
		gVal = g_val;
		cam = new OrthographicCamera();
		updateGravity();
		world = new World(gVector, false);
		world.setContactListener(new CollisionListener());
		
		
		tilemap = new TmxMapLoader().load(map_path);
		MapBodyBuilder.buildShapes(tilemap, world);
		
		MapProperties prop = tilemap.getProperties();
		w = prop.get("width",Integer.class) * prop.get("tilewidth", Integer.class);
		h = prop.get("height", Integer.class)* prop.get("tileheight", Integer.class);
		
	}
	
	public void update(){
		doPhysicsStep(Gdx.graphics.getDeltaTime());
	}
	
	Box2DDebugRenderer renderer;
	
	static final int[] backLayers = new int[]{0,1};
	static final int[] frontLayers = new int[]{2,3};
	
	Vector2 op;
	public boolean debugCollisions;
	
	public void draw(SpriteBatch g){
		
		if(mapRenderer == null){
			mapRenderer = new OrthogonalTiledMapRenderer(tilemap, g);
		}
		//mapRenderer.setView(cam);
		
		
		
		
		toggleMapCam();
		mapRenderer.setView(cam);
		mapRenderer.render(backLayers);
		toggleMapCam();
		g.begin();
		draw2(g);
		g.end();
		toggleMapCam();
		mapRenderer.setView(cam);
		mapRenderer.render(frontLayers);
		toggleMapCam();
		
		if(renderer == null)renderer = new Box2DDebugRenderer();
		
		if(debugCollisions)renderer.render(world, cam.combined);
	}
	
	
	float oh;
	float ow;
	private boolean mapCam = false;
	protected void toggleMapCam(){
		if(mapCam){
			cam.position.set(op,0);
			cam.viewportHeight = oh;
			cam.viewportWidth = ow;
			mapCam = false;
		}else{
			if(op == null){
				op = new Vector2(cam.position.x, cam.position.y);
			}else{
				op.set(cam.position.x,cam.position.y);
			}
			oh = cam.viewportHeight;
			ow = cam.viewportWidth;
			cam.viewportHeight = oh * 8;
			cam.viewportWidth = ow * 8;
			cam.position.set(op.x *8,op.y * 8, 0);
			mapCam = true;
			
			
				

			
			
			
		}
		cam.update();
	}
	
	
	public void setGravity(float theta){
		gTheta = theta;
		updateGravity();
	}
	
	public abstract void draw2(SpriteBatch g);
	
	
	protected void updateGravity(){
		float xgrav = (float) (Math.cos(gTheta) * gVal);
		float ygrav = (float) (Math.sin(gTheta) * gVal);
		if(gVector == null)gVector = new Vector2();
		gVector.set(xgrav,ygrav);
		if(this instanceof PlayState){
			((PlayState) this).notifyGravity();
		}
		
		if(world != null)world.setGravity(gVector);
		
		
	}
	
	private void doPhysicsStep(float deltaTime) {
	    
	       world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
	        
	    
	}
	
	public World getWorld(){
		return world;
	}

	public abstract void keyDown(int keycode);
	public abstract void keyUp(int keycode);
}
