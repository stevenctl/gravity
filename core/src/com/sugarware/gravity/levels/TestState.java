package com.sugarware.gravity.levels;

import java.text.DecimalFormat;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.sugarware.gravity.Angles;
import com.sugarware.gravity.GdxGame;
import com.sugarware.gravity.MathUtils;
import com.sugarware.gravity.entities.Animation;
import com.sugarware.gravity.entities.Door;
import com.sugarware.gravity.entities.GravSwitch;
import com.sugarware.gravity.entities.Hint;
import com.sugarware.gravity.entities.Player;

import box2dLight.PointLight;
import box2dLight.RayHandler;

public class TestState extends PlayState {


	
	Animation snoop;
	TiledBackground bg;
	BitmapFont bmf;
	PointLight pl;
	public TestState() {
		super("level1.tmx", Angles.DOWN, 6 * 9.8f);
		
		
		cam.viewportWidth = 60; cam.viewportHeight = 30;
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
		p = new Player(this,15f, 82.01f);
		
		
		entities.add(new Door(this, 8, 44f));
		entities.add(new Hint(this, "Switches can change the direction of gravity.",8,82,true));
		entities.add(new Hint(this, "Press space to jump. Press it again while in air for an extra boost.",117,41,true));
	}

	DecimalFormat df = new DecimalFormat("#.##");
	SpriteBatch testBatch;
	ShapeRenderer sr;
	
	
	
	public void update(){
		super.update();
		if(p.body.getPosition().y < -50)init();
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
