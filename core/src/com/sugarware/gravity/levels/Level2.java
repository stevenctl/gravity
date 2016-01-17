package com.sugarware.gravity.levels;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.sugarware.gravity.Angles;
import com.sugarware.gravity.CollisionBits;
import com.sugarware.gravity.GameStateManager.State;
import com.sugarware.gravity.GdxGame;
import com.sugarware.gravity.MathUtils;
import com.sugarware.gravity.entities.Animation;
import com.sugarware.gravity.entities.Box;
import com.sugarware.gravity.entities.Door;
import com.sugarware.gravity.entities.DoorSwitch;
import com.sugarware.gravity.entities.GravSwitch;
import com.sugarware.gravity.entities.Player;

import box2dLight.ConeLight;
import box2dLight.PointLight;

public class Level2 extends PlayState {


	
	Animation snoop;
	//TiledBackground bg;
	BitmapFont bmf;
	PointLight pl;
	public Level2() {
		super("level2.tmx", Angles.DOWN, 6 * 9.8f);
		
		
		cam.viewportWidth = 60; cam.viewportHeight = GdxGame.aspect * cam.viewportWidth;
		cam.update();
		//bg = new TiledBackground("stars.jpg",256,256, false);
		snoop = new Animation("snoop.png", 64, 64, new int[]{20});
		snoop.setDelay(100);
		//cam.position.set(p.body.getPosition().x,p.body.getPosition().y,0);
		//cam.update();
		
		TextDisplay.init();
		init();
	}
	
	@Override
	public void init(){
		super.init();
		
		this.setGravity(3 * (float)Math.PI / 2f);
		p = new Player(this,15f, 10.02f);
		entities.add(new Box(this,18,16));
		entities.add(new Box(this,18,13));
		Door door = new Door(this, 12.62f, 128f);
		entities.add(new DoorSwitch(this, 211.75f, 105.7f, Directions.Up,door));
		rayHandler.setAmbientLight(0.4f, 0.4f, 0.4f, 0);
		float i = 1.8f;
		while(i < 300){
			ConeLight light = new ConeLight(rayHandler, 150, Color.RED, 100, i, 0, 90, 30);
			light.setContactFilter(CollisionBits.CATEGORY_LIGHT, (short) 0, CollisionBits.MASK_LIGHT);
			lights.add(light);
			i+= 8.4f;
		}
		door.setDestination(State.Level3);
		door.lock();
		door.setGravityDir(Directions.Up);
		entities.add(door);

	}
	int lightamp = 100;int ldir = -1;
	DecimalFormat df = new DecimalFormat("#.##");
	SpriteBatch testBatch;
	ShapeRenderer sr;
	
	ArrayList<ConeLight> lights = new ArrayList<ConeLight>();
	
	public void update(){
		super.update();
		
		lightamp += ldir;
		if(lightamp <= 0)ldir = 1; else if(lightamp >= 100)ldir = -1;
		
	
		for(ConeLight l : lights){
			l.setDistance(lightamp);
		}
		
	}
	
	@Override
	public void draw(SpriteBatch g){
		
		snoop.update();
		
		
		g.begin();
		this.toggleMapCam();
		//bg.xshift+= 0.25;
		
		//bg.draw(g, cam);
		
		this.toggleMapCam();
		g.end();
		if(testBatch == null){
			testBatch = new SpriteBatch();
			bmf = new BitmapFont();
		}

		
		super.draw(g);
		
		rayHandler.setCombinedMatrix(cam);
		
		rayHandler.updateAndRender();
		p.hud.draw();
		testBatch.begin();
		if(GdxGame.smokeweed)testBatch.draw(snoop.getImage(), 0,100, 100,-100);
		testBatch.end();
		
		TextDisplay.draw(testBatch);
	}
	
	@Override
	public void draw2(SpriteBatch g) {	
		
		super.draw2(g);
		
		if(g.isDrawing()){
			g.end();
		}
		
		if(debugCollisions){
		testBatch.begin();
		bmf.draw(testBatch, "Gravity Angle: " 
		+ df.format(MathUtils.normalAngle(gTheta) / Math.PI) + "pi rad"
		,20,20 );
		bmf.draw(testBatch, df.format(p.body.getPosition().x) +", "+ df.format(p.body.getPosition().y),20, 40);
		if(p.colitem != null)bmf.draw(testBatch, p.colitem.getClass().toString(),20,60);
		testBatch.end();
		}
		g.begin();
	}
	
	public void keyDown(int k){
		super.keyDown(k);
		if(k == Keys.CONTROL_LEFT){
			entities.add(new GravSwitch(this, p.body.getPosition(), this.getGravityDirection()));
		}else if(k == Keys.ALT_LEFT){
			entities.remove(entities.size() - 1);
		}else if(k == Keys.NUM_8){
			TextDisplay.pleaseDraw("Look at all this dummy text. It's so dumb. It also wraps perfectly."+
					" Look at all this dummy text. It's so dumb. It also wraps perfectly."+
					" Look at all this dummy text. It's so dumb. It also wraps perfectly."+
					" Look at all this dummy text. It's so dumb. It also wraps perfectly." );
		}else if (k == Keys.NUM_9){
			//ambient = new Color(0.01f, 0.0f, 0.0f, 0.7f);
		}
	}

}
