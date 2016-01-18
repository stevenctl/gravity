package com.sugarware.gravity.levels;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.sugarware.gravity.Angles;
import com.sugarware.gravity.CollisionBits;
import com.sugarware.gravity.GameStateManager.State;
import com.sugarware.gravity.GdxGame;
import com.sugarware.gravity.entities.Animation;
import com.sugarware.gravity.entities.Door;
import com.sugarware.gravity.entities.GravSwitch;
import com.sugarware.gravity.entities.Hint;
import com.sugarware.gravity.entities.Player;

import box2dLight.ConeLight;
import box2dLight.Light;
import box2dLight.PointLight;

public class Level1 extends PlayState {


	 Sound alarm ; 
	Animation snoop;
	TiledBackground bg;
	BitmapFont bmf;
	PointLight pl;
	public Level1() {
		super("level1.tmx", Angles.DOWN, 6 * 9.8f);
		
		alarm =  Gdx.audio.newSound(Gdx.files.internal("alarm.ogg"));
		cam.viewportWidth = 60; cam.viewportHeight = GdxGame.aspect * cam.viewportWidth;
		cam.update();
		bg = new TiledBackground("stars.jpg",256,256, false);
		snoop = new Animation("snoop.png", 64, 64, new int[]{20});
		snoop.setDelay(100);
		//cam.position.set(p.body.getPosition().x,p.body.getPosition().y,0);
		//cam.update();
		alarm.loop(0.7f);
		TextDisplay.init();
		init();
	}
	int lightamp = 100;int ldir = -1;
	ArrayList<ConeLight> lights = new ArrayList<ConeLight>();
	
	@Override
	public void init(){
		super.init();
		p = new Player(this,15f, 82.01f);
		
		rayHandler.setAmbientLight(0.2f,0.2f,0.2f,0.0f);
		Door door = new Door(this, 8, 44f);
		door.setDestination(State.Level2);
		entities.add(door);
		
		entities.add(new Hint(this, "Switches can change the direction of gravity.",8,82,true));
		entities.add(new Hint(this, "Press space to jump. Press it again while in air for an extra boost.",117,41,true));
		
		float i = 1.8f;
		while(i < w/8){
			ConeLight light = new ConeLight(rayHandler, 150, Color.RED, 100, i, h / 8 -  5,270, 30);
			light.setContactFilter(CollisionBits.CATEGORY_LIGHT, (short) 0, CollisionBits.MASK_LIGHT);
			lights.add(light);
			i+= 15.4f;
		}
		
		TextDisplay.pleaseDraw("I've got a bad feeling about this..", 300);
	}

	
	SpriteBatch testBatch;
	ShapeRenderer sr;
	
	
	
	public void update(){
		super.update();
		if(p.body.getPosition().y < -50)init();
		
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
		bg.xshift+= 0.25;
		
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
	
	public void unload(){
		alarm.dispose();
		for(Light l : lights){
			l.dispose();
		}
	}

}
