package com.sugarware.gravity.entities;


import java.text.DecimalFormat;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.sugarware.gravity.MathUtils;
import com.sugarware.gravity.levels.PlayState;
import com.sugarware.gravity.levels.PlayState.Directions;
public class Player {

	public Body body;
	PlayState gs;
	
	
	final int width = 2;
	final int height =2;
	final int pwidth = 2 * width;
	final int pheight = 2 * height;
	final float maxvel = 15f;
	final float accel = 10f;
	final float damp1 = 5.0f;
	final float damp2 = 1.0f;
	Vector2 impulse;
	Animation anim;
	boolean facingRight= true;
	final float jumpPower = 40;
	public Fixture playerSensorFixture;
	Vector2 sensorPos;
	boolean jumping = false;
	
	
	int animState = 0;
	static final int run = 0;
	static final int jump = 1;
	
	
	public Player(PlayState gs,float x, float y){
		anim = new Animation("cybertrent.png",48,48,new int[]{4,3},false);
		anim.setDelay(100);
		BodyDef def = new BodyDef();
		def.type = BodyType.DynamicBody;
		def.position.set(x,y);
		this.gs = gs;
		body = gs.getWorld().createBody(def);
		//body.setFixedRotation(true);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width , height );
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.density = 1.0f;
		fdef.friction = 0.0f;
		body.setLinearDamping(damp2);
		fdef.restitution = 0.1f;
		body.createFixture(fdef);
		body.setFixedRotation(false);
		body.setBullet(true);
		PolygonShape pgon = new PolygonShape();		
		pgon.setRadius(0.5f);
		sensorPos = new Vector2(0, - height );
		pgon.setAsBox(width - 0.5f, 0.5f, sensorPos, 0);
		playerSensorFixture = body.createFixture(pgon, 0);	
		playerSensorFixture.setSensor(true);
		pgon.dispose();	
		
		shape.dispose();
		impulse = new Vector2();
		System.out.println(body.getMass());
	}

	boolean turnme = false;
	float diff = -1f;
	
	BitmapFont bmf;
	public void update(){
		if(gs.isPlayerGrounded(1/60f)){
			body.setLinearDamping(damp1);
		}else{
			body.setLinearDamping(damp2);
		}
		if(jumping){
			if(gs.getGravityDirection() == Directions.Right||
					gs.getGravityDirection() == Directions.Left){
				if(Math.abs(body.getLinearVelocity().x) < 2)jumping = false;
			}else{
				if(Math.abs(body.getLinearVelocity().y) < 2)jumping = false;
			}
		}
		
		boolean moving = false;
		if(gs.getGravityDirection() == Directions.Right||
				gs.getGravityDirection() == Directions.Left){
			
			if(UP || DOWN){
				moving = true;
			}else if(animState == run) anim.setFrame(0);	
			
			
			
			
		}else{
		
			if(LEFT || RIGHT){
				moving = true;
			}else if(animState == run) anim.setFrame(0);
			
		}
		
		if(moving || animState == jump)anim.update();
		
		 
		body.setTransform(body.getPosition(), MathUtils.normalAngle(gs.gTheta +  Math.PI / 2));
		//System.out.println(MathUtils.normalAngle(body.getAngle() - Math.PI / 2));
	
		if(gs.getGravityDirection() == Directions.Right||
				gs.getGravityDirection() == Directions.Left){
			if(UP){
				impulse.set(0,accel * body.getMass());
				//if(body.getLinearVelocity().y < maxVelocity)
					body.applyLinearImpulse(impulse, body.getPosition(), true);
				
			}else if(DOWN){
				impulse.set(0,-accel * body.getMass());
				//if(body.getLinearVelocity().y >- maxVelocity)
					body.applyLinearImpulse(impulse, body.getPosition(), true);
				
			}
			
			if(body.getLinearVelocity().y < -maxvel){
				 body.setLinearVelocity(body.getLinearVelocity().x, -maxvel); 
			}
			else if(body.getLinearVelocity().y > maxvel){
				 body.setLinearVelocity(body.getLinearVelocity().x, maxvel); 
			}
		}else{
			if(RIGHT){
				impulse.set(accel *body.getMass(),0);
				//if(body.getLinearVelocity().x < maxVelocity)
					body.applyLinearImpulse(impulse, body.getPosition(), true);
			}else if(LEFT){
				impulse.set(-accel * body.getMass(),0);
				//if(body.getLinearVelocity().x >- maxVelocity)
					body.applyLinearImpulse(impulse, body.getPosition(), true);
			}
			if(body.getLinearVelocity().x < -maxvel){
				 body.setLinearVelocity(-maxvel, body.getLinearVelocity().y); 
			}
			else if(body.getLinearVelocity().x > maxvel){
				 body.setLinearVelocity(maxvel,body.getLinearVelocity().y); 
			}
		}
		
		updateAnimation();
	
		
	}
	
	private void updateAnimation() {
		if(jumping){
			if(animState != jump){
				animState = jump;
				anim.setFrames(jump);
			}
		}else{
			if(animState != run){
				animState = run;
				anim.setFrames(run);
			}
		}
		
	}

	static float deg = 0;
	
	DecimalFormat df = new DecimalFormat("##.###");
	public void draw(SpriteBatch g){
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
		switch(gs.getGravityDirection()){
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
		
		deg += 0.25f;
		g.setProjectionMatrix(gs.cam.combined);
		g.draw(anim.getImage(),
				body.getPosition().x - hshift, body.getPosition().y - pheight / 2 + vshift,
				pwidth / 2, pheight / 2,
				 flip * pwidth, pheight,
				1f,1f,
				 gs.gTheta * 180f / (float)Math.PI - 90
				
			);
		//System.out.println(gs.getGravityDirection().name());
	}
	
	

	
	
	
	String cleanCoords(Vector2 v2){
		return "(" + 
					df.format(MathUtils.normalAngle(v2.x)) + ", " + 
				df.format(MathUtils.normalAngle(v2.y)) + ")";
	}
	
	
	
	Vector2 jumpVector = new Vector2();
	boolean UP = false, DOWN = false, LEFT = false, RIGHT = false;
	public void keyDown(int k){
		switch(k){
		case Keys.W:
		UP = true;
		if(gs.getGravityDirection() == Directions.Right||
				gs.getGravityDirection() == Directions.Left)facingRight = true;
		
			break;
		case Keys.A: LEFT = true;
		if(gs.getGravityDirection() == Directions.Up ||
				gs.getGravityDirection() == Directions.Down)facingRight = false;
			break;
		case Keys.S: DOWN = true;
			if(gs.getGravityDirection() == Directions.Right||
					gs.getGravityDirection() == Directions.Left)facingRight = false;
			
			break;
		case Keys.D:
			
			RIGHT = true;
			if(gs.getGravityDirection() == Directions.Up ||
					gs.getGravityDirection() == Directions.Down)facingRight = true;
			break;
			
			
			
			
		case Keys.SPACE:
			if(gs.isPlayerGrounded(1 / 60f)){
				if(gs.getGravityDirection() == Directions.Up ||
						gs.getGravityDirection() == Directions.Down){
				jumpVector.set(0,  getJumpVel(jumpPower) );
				jumping = true;
				
				}else{
					jumpVector.set(-getJumpVel(jumpPower),0 );
					jumping = true;
				}
				
				body.setLinearVelocity(jumpVector);
			}
		}
	}
	
	
	
	private float getJumpVel(float dist){
		float g = gs.gVal;
		float t = (float) (  (- g / 2) + Math.sqrt((Math.pow(g, 2) / 4) + (2 * g * dist))   )
				
				
				/g;		
		//System.out.println(g); System.out.println(t);
		
		if(gs.getGravityDirection() == Directions.Up ||
				gs.getGravityDirection() == Directions.Down){
		return (-gs.getWorld().getGravity().y) * t;
		}else{
			return (gs.getWorld().getGravity().x) * t;
		}
	}
	
	
	
	public void keyUp(int k){
		switch(k){
		case Keys.W: UP = false;
			break;
		case Keys.A: LEFT = false;
			break;
		case Keys.S: DOWN = false;
			break;
		case Keys.D: RIGHT = false;
			break;
		}
	}






	public void notifyGravity() {
		
		
	}
	
}
