package com.sugarware.gravity.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.sugarware.gravity.levels.PlayState;
import com.sugarware.gravity.levels.PlayState.Directions;

public class SpaceJunk extends Entity{

	float vel;
	public SpaceJunk(PlayState gs, float x, float y, String image, int w, int h, int[] f, float vel) {
		super(gs);
		this.vel = vel;
		this.width = w / 8;
		this.height = h / 8;
		this.pwidth = width;
		this.pheight = height;
		BodyDef def = new BodyDef();
		def.position.set(x,y);
		def.type = BodyType.DynamicBody;
		body = gs.getWorld().createBody(def);
		
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
	
	public void draw(SpriteBatch g){

		g.setProjectionMatrix(gs.cam.combined);
		g.draw(anim.getImage(),
				body.getPosition().x - pwidth / 2 , body.getPosition().y - pheight / 2 ,
				pwidth / 2   , pheight / 2   ,
				  pwidth, pheight,
				1f,1f, (float) (180 * body.getAngle() / Math.PI)
				 
				
			);
		
		//g.draw(region, x, y, originX, originY, width, height, scaleX, scaleY, rotation);
	}

	@Override
	public void update() {
		body.setLinearVelocity(vel, 0);
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
