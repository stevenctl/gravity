package com.sugarware.gravity.entities;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.sugarware.gravity.GameStateManager.State;
import com.sugarware.gravity.GameStateManager;
import com.sugarware.gravity.ResourceManager;
import com.sugarware.gravity.levels.PlayState;
import com.sugarware.gravity.levels.PlayState.Directions;
import com.sugarware.gravity.levels.TextDisplay;

public class Door extends Entity {

	
	static final String spritePath = "door.png";
	public boolean activated = false, open = false;
	public boolean locked = false;
	State destination = State.Test;
	
	public Door(PlayState gs, float x, float y) {
		super(gs);
		
		if(ResourceManager.sprites.containsKey(spritePath)){
			anim = ResourceManager.sprites.get(spritePath).clone();
		}else{
			anim = new Animation(spritePath,64,106,new int[]{3},false);
			anim.setDelay(100);
			ResourceManager.sprites.put(spritePath, anim);
		}
		myDir= Directions.Down;
		width = 2;
		pwidth = height = 4;
		pheight = 8;
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

	static final int active = 0, lockd = 1, opened = 2;
	@Override
	public void update() {
		 if(open){
				anim.setFrame(opened);
		 }else if(activated){
			anim.setFrame(active);
			openTimer--;
			if(openTimer < 0)open = true;
		}else{
			anim.setFrame(lockd);
		}
	}

	int openTimer = 40;
	@Override
	public void activate() {
		
		if(locked){
			TextDisplay.pleaseDraw("This door is locked.");
			return;
		}
		if(open)GameStateManager.getInstance().setState(destination , true);
	}
	
	public void open(){
		if(!locked)
		activated = true;	
	}
	
	public void close(){
		open = false;
		activated = false;
		openTimer = 40;
	}

	@Override
	public boolean canActivate() {
		// TODO Auto-generated method stub
		return open || locked ;
	}

	
	public State getDestination(){
		return destination;
	}
	
	public void setDestination(State d){
		destination = d;
	}

	public void lock() {
		locked = true;
		
	}
	
	public void unlock(){
		locked = false;
	}
}
