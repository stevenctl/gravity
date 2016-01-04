package com.sugarware.gravity.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.sugarware.gravity.ResourceManager;
import com.sugarware.gravity.levels.PlayState;

public class Box extends Entity {

	public static final String spritePath = "Box.png";
	
	public Box(PlayState gs, float x, float y) {
		this(gs,x, y, 3, 3);
	}
	
	public Box(PlayState gs, float x, float y, float w, float h) {
		super(gs);
		if(ResourceManager.sprites.containsKey(spritePath)){
			anim = ResourceManager.sprites.get(spritePath).clone();
		}else{
			anim = new Animation(spritePath,32,32,new int[]{1},false);
			anim.setDelay(100);
			ResourceManager.sprites.put(spritePath, anim);
		}
		
		setGravityDir(gs.getGravityDirection());
		width = height = w;
		pwidth = pheight = 2 * width;
		
		BodyDef def = new BodyDef();
		def.type = BodyType.DynamicBody;
		def.position.set(x,y);
		body = gs.getWorld().createBody(def);
		body.setUserData("world");
		//body.setFixedRotation(true);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width , height );
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.density = 2.0f;
		fdef.friction = 0.85f;
		fdef.restitution = 0.1f;
		body.createFixture(fdef);
		body.setFixedRotation(false);
		body.setBullet(true);
		shape.dispose();
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
	public boolean canActivate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void activate() {
		// TODO Auto-generated method stub
		
	}

}
