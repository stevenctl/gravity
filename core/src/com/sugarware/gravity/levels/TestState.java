package com.sugarware.gravity.levels;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;
import com.sugarware.gravity.Angles;
import com.sugarware.gravity.GameStateManager;
import com.sugarware.gravity.GdxGame;
import com.sugarware.gravity.Rumbler;
import com.sugarware.gravity.entities.Animation;
import com.sugarware.gravity.entities.Player;

import box2dLight.ConeLight;

public class TestState extends PlayState {


	
	
	Animation snoop;
	TiledBackground bg;
	BitmapFont bmf;
	
	
	
	long ticks = 0;
	public TestState() {
		super("intro.tmx", Angles.DOWN, 6 * 9.8f);
	
		cam.viewportWidth = 57; cam.viewportHeight = GdxGame.aspect * cam.viewportWidth;
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
		
		rayHandler.setAmbientLight(Color.WHITE);
		
		
		
	}

	
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
			bmf = new BitmapFont(Gdx.files.internal("kr.fnt"));
		}

		
		super.draw(g);
		
		rayHandler.setCombinedMatrix(cam);
		
		rayHandler.updateAndRender();
		p.hud.draw();
		testBatch.begin();
		if(GdxGame.smokeweed)testBatch.draw(snoop.getImage(), 0,100, 100,-100);
		testBatch.end();
		
		TextDisplay.draw(testBatch);
		bmf.getData().setScale((float) Gdx.graphics.getWidth() / 300f);
		testBatch.begin();
		bmf.setColor(1, 1, 1, 1);
		bmf.draw(testBatch, "Gravity", Gdx.graphics.getWidth() / 4, 3 * Gdx.graphics.getHeight() / 4, 0, "Gravity".length(), Gdx.graphics.getWidth() / 2, Align.center, false, "...");
	
		bmf.setColor(1, 1, 1, (float)Math.sin(ticks / 50f));
		bmf.getData().setScale((float) Gdx.graphics.getWidth() / 600f);
		bmf.draw(testBatch, "Press any key...", 0, Gdx.graphics.getHeight() / 4, 0, "Press any key...".length(), Gdx.graphics.getWidth() , Align.center, false, "...");
		
		testBatch.end();
		bmf.getData().setScale(1);
	}
	
	@Override
	public void draw2(SpriteBatch g) {	
		
		super.draw2(g);
		
		if(g.isDrawing()){
			g.end();
		}
	
		g.begin();
	}
	

	
	ArrayList<ConeLight> lights = new ArrayList<ConeLight>();
	public void keyDown(int k){
	//	super.keyDown(k);
		GameStateManager.getInstance().setState(GameStateManager.State.Intro, false);
	}
	
	public void keyUp(int k){
		//super.keyUp(k);
		
	}

}
