package com.sugarware.gravity.levels;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.sugarware.gravity.Angles;
import com.sugarware.gravity.CollisionBits;
import com.sugarware.gravity.GameStateManager;
import com.sugarware.gravity.GameStateManager.State;
import com.sugarware.gravity.GdxGame;
import com.sugarware.gravity.MathUtils;
import com.sugarware.gravity.Rumbler;
import com.sugarware.gravity.Script;
import com.sugarware.gravity.entities.Animation;
import com.sugarware.gravity.entities.Door;
import com.sugarware.gravity.entities.GravSwitch;
import com.sugarware.gravity.entities.Player;
import com.sugarware.gravity.entities.SpaceJunk;

import box2dLight.ConeLight;

public class Level4 extends PlayState {


	
	
	Animation snoop;
	TiledBackground bg;
	BitmapFont bmf;
	
	
	
	long ticks = 0;
	public Level4() {
		super("level4.tmx", Angles.DOWN, 6 * 9.8f);
	
		cam.viewportWidth = 56; cam.viewportHeight = GdxGame.aspect * cam.viewportWidth;
		System.out.println(GdxGame.aspect);
		cam.update();
		bg = new TiledBackground("stars.jpg",256,256, false);
		snoop = new Animation("snoop.png", 64, 64, new int[]{20});
		snoop.setDelay(100);
		//cam.position.set(p.bodyfds.getPosition().x,p.body.getPosition().y,0);
		//cam.update();testtestasdfafsd
		TextDisplay.init();
		
		init();
	}
	
	@Override
	public void init(){
		super.init();
		p = new Player(this,3f, 22.02f);
		this.setGravity(3 * (float)Math.PI / 2f);
		
	   rayHandler.setAmbientLight(0.4f, 0.4f, 0.6f, 0.78f);
		
		
		Door door = new Door(this, 39, 92.02f);
		door.setDestination(State.Level5);
		entities.add(door);
	}

	DecimalFormat df = new DecimalFormat("#.##");
	SpriteBatch testBatch;
	ShapeRenderer sr;
	private Rumbler rumb;
	
	
	int lightamp = 100;int ldir = -1;
	
	public void update(){
		super.update();
		ticks++;
		lightamp += ldir;
		if(lightamp <= 0)ldir = 1; else if(lightamp >= 100)ldir = -1;
		
	
		for(ConeLight l : lights){
			l.setDistance(lightamp);
		}
		
	
		if(rumb!=null){
			rumb.update();
			if(rumb.killme)rumb = null;
		}
	}
	
	@Override
	public void draw(SpriteBatch g){
		
		snoop.update();
		
		
		g.begin();
		this.toggleMapCam();
		bg.xshift-= 0.25;
		
		bg.draw(g, cam);
		
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
	

	
	ArrayList<ConeLight> lights = new ArrayList<ConeLight>();
	public void keyDown(int k){
		super.keyDown(k);
		
		if(k == Keys.CONTROL_LEFT){
		//	entities.add(new GravSwitch(this, p.body.getPosition(), this.getGravityDirection()));
		}else if(k == Keys.ALT_LEFT){
			//entities.remove(entities.size() - 1);
		}else if(k == Keys.NUM_8){
			
		}else if(k==Keys.NUM_0){
			
		}else if (k == Keys.NUM_9){
			
	
		
			
		}
	}
	
	public void keyUp(int k){
		super.keyUp(k);
		
	}

}
