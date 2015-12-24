package com.sugarware.gravity.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.sugarware.gravity.ResourceManager;
import com.sugarware.gravity.levels.PlayState;
import com.sugarware.gravity.levels.PlayState.Directions;

public abstract class Switch extends Entity {

	public Switch(PlayState gs, Vector2 pos, Directions d){
		this(gs, pos.x, pos.y, d);
	}
	Fixture sensor;
	public Switch(PlayState gs, float x, float y,Directions d) {
		super(gs);
		if(ResourceManager.sprites.containsKey("switch.png")){
			anim = ResourceManager.sprites.get("switch.png").clone();
		}else{
			anim = new Animation("switch.png",9,32,new int[]{2},false);
			anim.setDelay(100);
			ResourceManager.sprites.put("switch.png", anim);
		}
			
			
		myDir = d;
		width = 1;
		height = 2;
		pwidth = 2;
		pheight = 4;
		
		
		BodyDef def = new BodyDef();
		
		def.position.set(x,y);
		body = gs.getWorld().createBody(def);
		body.setUserData(this);
		//body.setFixedRotation(true);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width , height );
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.density = 0.0f;
		fdef.friction = 0.0f;
		fdef.isSensor = true;
		sensor  = body.createFixture(fdef);
		
		shape.dispose();
		
	}
	
	



}
