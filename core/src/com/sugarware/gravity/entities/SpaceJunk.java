package com.sugarware.gravity.entities;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.sugarware.gravity.levels.PlayState;
import com.sugarware.gravity.levels.PlayState.Directions;

public class SpaceJunk extends Entity{

	
	public SpaceJunk(PlayState gs, float x, float y, String image, int w, int h, int[] f) {
		super(gs);
		BodyDef def = new BodyDef();
		def.position.set(x,y);
		def.type = BodyType.DynamicBody;
		body = gs.getWorld().createBody(def);
		pheight = h;pwidth = w;
		anim = new Animation(image, w, h, f);
		CircleShape cs = new CircleShape();
		cs.setRadius(2);
		FixtureDef fdef = new FixtureDef();
		fdef.shape = cs;
		Fixture fd = body.createFixture(fdef);
		fd.setSensor(true);
		myDir = Directions.Down;
		cs.dispose();
	}

	@Override
	public void update() {
		body.setLinearVelocity(-5f, 0);
	}

	@Override
	public void activate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean canActivate() {
		// TODO Auto-generated method stub
		return false;
	}

	
	
	

}
