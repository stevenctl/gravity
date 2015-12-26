package com.sugarware.gravity.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.sugarware.gravity.MathUtils;
import com.sugarware.gravity.levels.PlayState;
import com.sugarware.gravity.levels.PlayState.Directions;

public abstract class Entity {
 
	public Body body;
	Animation anim;
	boolean facingRight;
	PlayState gs;
	public Directions myDir;
	int width;
	int height;
	public int pwidth; 
	public int pheight; 
	
	public Entity(PlayState gs){
		this.gs = gs;

		
	}
	
	public void draw(SpriteBatch g){
	if(!gs.entities.contains(this) && !(this instanceof Player)){
		gs.world.destroyBody(body);
		return;
	}
		
	/*
		if(bmf == null)bmf = new BitmapFont();
		if(turnme)bmf.draw(g, String.valueOf(diff), 10, 100);
		bmf.draw(g,cleanCoords(new Vector2 ((float) (body.getAngle()  - Math.PI / 2)   , gs.gTheta)), 20, 80);
		bmf.draw(g,cleanCoords(body.getLinearVelocity()),20,20);
		bmf.draw(g, "Grounded: " + String.valueOf(((PlayState)gs).isPlayerGrounded(1/60f)),20,40);
		bmf.draw(g,"Turnme: " + String.valueOf(turnme),20,60);
		bmf.setColor(Color.WHITE);*/
		float flip = 1;
		float hshift = pwidth / 2;
		float vshift = 0;
		
		if(getGravityDir() == null)System.out.println("throw a dance party");
		switch(getGravityDir()){
		case Up: 
			flip = facingRight ? 1 : -1;
			hshift = facingRight ?   pwidth / 2: - pwidth/ 2;
			break;
		case Down: 
			flip = facingRight ? -1 : 1;
			hshift = facingRight ? 3f / 2f*  pwidth : pwidth/ 2;
			break;
		case Left:
			flip = facingRight ? 1 : -1;
			hshift = facingRight ? pwidth / 2 : pwidth/ 2;
			vshift = facingRight ? 0 : pwidth;
			break;
		case Right:
			flip = facingRight ? -1 : 1;
			hshift = facingRight ? pwidth / 2 : pwidth/ 2;
			vshift = facingRight ? -pwidth : 0;
			break;
		
		}
		
		
		float mTheta = MathUtils.dirToAngle(getGravityDir()) * 180f / (float)Math.PI - 90;
		
		g.setProjectionMatrix(gs.cam.combined);
		g.draw(anim.getImage(),
				body.getPosition().x - hshift, body.getPosition().y - pheight / 2 + vshift,
				pwidth / 2, pheight / 2,
				 flip * pwidth, pheight,
				1f,1f, mTheta
				 
				
			);
		//System.out.println(gs.getGravityDirection().name());
	}
	
	public abstract void update();

	public Directions getGravityDir() {
		return myDir;
	}

	public void setGravityDir(Directions myDir) {
		this.myDir = myDir;
	}
	
	public abstract void activate();
	
	public void destroy(){
		gs.world.destroyBody(this.body);
		
	}
	
}
