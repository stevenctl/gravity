package com.sugarware.gravity.levels;

import java.text.DecimalFormat;
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
import com.sugarware.gravity.GameStateManager;
import com.sugarware.gravity.GameStateManager.State;
import com.sugarware.gravity.GdxGame;
import com.sugarware.gravity.MathUtils;
import com.sugarware.gravity.Rumbler;
import com.sugarware.gravity.Script;
import com.sugarware.gravity.entities.Animation;
import com.sugarware.gravity.entities.GravSwitch;
import com.sugarware.gravity.entities.Player;
import com.sugarware.gravity.entities.SpaceJunk;

import box2dLight.ConeLight;
import box2dLight.Light;

public class Intro extends PlayState {


	Script script;
	
	Animation snoop;
	TiledBackground bg;
	BitmapFont bmf;
	
	 Sound alarm ; 
	 Sound boom ;
	long ticks = 0;
	public Intro() {
		super("intro.tmx", Angles.DOWN, 6 * 9.8f);
		script = new Script(Gdx.files.internal("intro.ks").read());
		alarm =  Gdx.audio.newSound(Gdx.files.internal("alarm.ogg"));
		boom = Gdx.audio.newSound(Gdx.files.internal("explosion1.wav"));
		cam.viewportWidth = 56; cam.viewportHeight = GdxGame.aspect * cam.viewportWidth;
		System.out.println(GdxGame.aspect);
		cam.update();
		bg = new TiledBackground("stars.jpg",256,256, false);
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
		p = new Player(this,2f, 10.02f);
		rayHandler.setAmbientLight(Color.WHITE);

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
		
		if(p.body.getPosition().x > (w / 8) -18 && !GameStateManager.getInstance().isTransitioning()){
			alarm.stop();
			GameStateManager.getInstance().setState(State.Level1, true);
		}
		for(ConeLight l : lights){
			l.setDistance(lightamp);
		}
		
		script.update(this, ticks);
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
	
	public void addMeteor(){
		int size = (int)(Math.random() * 3f) + 1;
		String p = "m";
		float angvel = 3f / size;
		switch(size){
		case 1: p+="tiny.png"; size = 12; break;
		case 2: p+="small.png"; size = 32; break;
		case 3: p+="med.png"; size = 63; break;
		}
		
		
		
		SpaceJunk meteor = new SpaceJunk(this,  -10 - (float)Math.random() * 10f, (float) 17 + (float)Math.random() * 29f, p, size, size, new int[]{1}, (float) (3 + Math.random() * 4));
		meteor.body.setAngularDamping(0);
		meteor.body.setAngularVelocity(angvel);
		entities.add(meteor);
	}
	
	public void bigMeteor(){
		SpaceJunk meteor = new SpaceJunk(this,  -15 , (float) 22 , "mbig.png", 128, 128, new int[]{1},11);
		meteor.body.setAngularDamping(0);
		meteor.body.setAngularVelocity(4.5f);
		
		entities.add(meteor);
	}
	
	ArrayList<ConeLight> lights = new ArrayList<ConeLight>();
	public void keyDown(int k){
		super.keyDown(k);
		
		if(k == Keys.CONTROL_LEFT){
			entities.add(new GravSwitch(this, p.body.getPosition(), this.getGravityDirection()));
		}else if(k == Keys.ALT_LEFT){
			entities.remove(entities.size() - 1);
		}else if(k == Keys.NUM_8){
			addMeteor();
		}else if(k==Keys.NUM_0){
			bigMeteor();
		}else if (k == Keys.NUM_9){
			rumb = new Rumbler(cam, 30);
			alarm.loop(0.7f);
			boom.play();
			rayHandler.setAmbientLight(0.2f, 0.2f, 0.4f, 0.78f);
			float i = 1.8f;
			while(i < 300){
				ConeLight light = new ConeLight(rayHandler, 150, Color.RED, 100, i, 9, 90, 30);
				light.setContactFilter(CollisionBits.CATEGORY_LIGHT, (short) 0, CollisionBits.MASK_LIGHT);
				lights.add(light);
				i+= 8.4f;
			}
			
			this.gVal = -5;
			this.updateGravity();
			
		}
	}
	
	public void keyUp(int k){
		super.keyUp(k);
		
	}
	
	public void unload(){
		alarm.dispose();
		boom.dispose();
		for(Light l : lights){
			l.dispose();
		}
		
	}

}
