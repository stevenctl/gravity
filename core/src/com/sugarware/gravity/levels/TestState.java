package com.sugarware.gravity.levels;

import java.text.DecimalFormat;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sugarware.gravity.Angles;
import com.sugarware.gravity.MathUtils;
import com.sugarware.gravity.entities.GravSwitch;
import com.sugarware.gravity.entities.Player;

public class TestState extends PlayState {

	
	TiledBackground bg;
	BitmapFont bmf;
	public TestState() {
		super("level1.tmx", Angles.DOWN, 6 * 9.8f);
		p = new Player(this,20, 80);
		cam.viewportWidth = 60; cam.viewportHeight = 30;
		cam.update();
		bg = new TiledBackground("stars.jpg",256,256, false);
		
		
		//cam.position.set(p.body.getPosition().x,p.body.getPosition().y,0);
		//cam.update();
	}

	DecimalFormat df = new DecimalFormat("#.##");
	SpriteBatch testBatch;
	
	@Override
	public void draw(SpriteBatch g){
		g.begin();
		this.toggleMapCam();
		bg.xshift+= 0.25;
		
		bg.draw(g, cam);
		
		this.toggleMapCam();
		g.end();
		super.draw(g);
		
	}
	
	@Override
	public void draw2(SpriteBatch g) {	
		
		super.draw2(g);
		if(testBatch == null){
			testBatch = new SpriteBatch();
			bmf = new BitmapFont();
		}
		if(g.isDrawing()){
			g.end();
		}
		testBatch.begin();
		bmf.draw(testBatch, "Gravity Angle: " 
		+ df.format(MathUtils.normalAngle(gTheta) / Math.PI) + "pi rad"
		,20,20 );
		bmf.draw(testBatch, df.format(p.body.getPosition().x) +", "+ df.format(p.body.getPosition().y),20, 40);
		if(p.colitem != null)bmf.draw(testBatch, p.colitem.getClass().toString(),20,60);
		testBatch.end();
		g.begin();
	}
	
	public void keyDown(int k){
		super.keyDown(k);
		if(k == Keys.CONTROL_LEFT){
			entities.add(new GravSwitch(this, p.body.getPosition(), this.getGravityDirection()));
		}else if(k == Keys.ALT_LEFT){
			entities.remove(entities.size() - 1);
		}
	}

}
