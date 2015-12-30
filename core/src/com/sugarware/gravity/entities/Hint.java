package com.sugarware.gravity.entities;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.sugarware.gravity.levels.PlayState;
import com.sugarware.gravity.levels.PlayState.Directions;
import com.sugarware.gravity.levels.TextDisplay;

public class Hint extends Entity{

	int time = 120;
	String text;
	static Animation img;
	boolean draw;
	public Hint(PlayState gs, String s, float x, float y) {
		this(gs, s, x, y, false);
	}
	
	public Hint(PlayState gs, String s, float x, float y, boolean draw){
		super(gs);
		int ntime = (int) (s.split(" ").length * 42);
		if(ntime > time)time = ntime;
		this.draw = draw;
		
		text = s;
		
		myDir= Directions.Down;
		width = height = 1;
		pwidth = pheight = 2 * width;
		
		BodyDef def = new BodyDef();
		def.position.set(x,y);
		body = gs.getWorld().createBody(def);
		body.setUserData(this);
		//body.setFixedRotation(true);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width , height );
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.density = 1.0f;
		fdef.friction = 0.0f;
		fdef.restitution = 0.1f;
		fdef.isSensor = true;
		body.createFixture(fdef);
	}

	@Override
	public boolean canActivate() {
		return !TextDisplay.isDrawing();
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}
	
	public void draw(SpriteBatch g){
		if(draw ){
			if(img == null)img = new Animation("hint.png",64,64,new int[]{1});
			if(anim == null)anim = img;
			super.draw(g);
		}else{
			return;
		}
	}

	@Override
	public void activate() {
		TextDisplay.pleaseDraw(text, time);
	}

}
