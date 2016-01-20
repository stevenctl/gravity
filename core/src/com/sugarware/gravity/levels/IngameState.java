package com.sugarware.gravity.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.sugarware.gravity.entities.MapBodyBuilder;

import box2dLight.RayHandler;

public abstract class IngameState extends GameState {
	
	//Constants
	static final float TIME_STEP = 1/60f;
	static final int VELOCITY_ITERATIONS = 6, POSITION_ITERATIONS = 2;
	//ShapeRenderer ambientRenderer;
	//Color ambient;
	public float w, h;
	public World world;
	private Vector2 gVector;
	public float gTheta;
	public float gVal;
	private FrameBuffer fbo;
	public TiledMap tilemap;
	private OrthogonalTiledMapRenderer mapRenderer;
	public RayHandler rayHandler;
	public IngameState(String map_path, float g_theta, float g_val){
		gVector = new Vector2();
		gTheta = g_theta;
		gVal = g_val;
		//ambientRenderer = new ShapeRenderer();

		
		//ambient = new Color(0.0f,0.0f,0.001f, 0.999f);
		cam = new OrthographicCamera();
		
		updateGravity();
		
		fbo = new FrameBuffer( Format.RGBA4444,Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		
		tilemap = new TmxMapLoader().load(map_path);
		
		
		MapProperties prop = tilemap.getProperties();
		w = prop.get("width",Integer.class) * prop.get("tilewidth", Integer.class);
		h = prop.get("height", Integer.class)* prop.get("tileheight", Integer.class);
		
	}
	
	public void init(){
		if(world != null)world.dispose();
		world = new World(gVector, false);
		world.setContactListener(new CollisionListener());
		rayHandler = new RayHandler(world);
		rayHandler.setAmbientLight(0, 0, 0.08f, 0.3f);
		RayHandler.useDiffuseLight(true);
	//	rayHandler.setLightMapRendering(false);
		MapBodyBuilder.buildShapes(tilemap, world);
	
	}
		
	
	public void update(){
		doPhysicsStep(Gdx.graphics.getDeltaTime());
	}
	
	Box2DDebugRenderer renderer;
	
	static final int[] backLayers = new int[]{0,1};
	static final int[] frontLayers = new int[]{2,3};
	
	Vector2 op;
	public boolean debugCollisions;
	
	SpriteBatch tbatch = new SpriteBatch();
	public void draw(SpriteBatch g){
		
		if(mapRenderer == null){
			mapRenderer = new OrthogonalTiledMapRenderer(tilemap, g);
		}
		//mapRenderer.setView(cam);
		
		
		fbo.begin();
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		g.begin();
		draw1(g);
		g.end();
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
		rayHandler.updateAndRender();
		fbo.end();
		tbatch.begin();
		
		tbatch.draw(fbo.getColorBufferTexture(), 0, Gdx.graphics.getHeight()  , Gdx.graphics.getWidth(), -Gdx.graphics.getHeight());
		tbatch.end();
		
		Gdx.gl.glEnable(GL20.GL_BLEND);
	//	ambientRenderer.begin(ShapeType.Filled);
	//	ambientRenderer.setColor(ambient);
		//ambientRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		//ambientRenderer.end();
	
		
		
		
		//if(renderer == null)renderer = new Box2DDebugRenderer();
		
		//if(debugCollisions)renderer.render(world, cam.combined);
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
	
	/***
	 * Draws before anything
	 * 
	 */
	public abstract void draw1(SpriteBatch g);

	/***
	 * Draws between tile layers 2 and 3
	 * 
	 */
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
