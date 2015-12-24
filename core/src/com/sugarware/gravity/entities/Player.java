package com.sugarware.gravity.entities;


import java.text.DecimalFormat;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.badlogic.gdx.utils.Array;
import com.sugarware.gravity.MathUtils;
import com.sugarware.gravity.levels.PlayState;
import com.sugarware.gravity.levels.PlayState.Directions;
public class Player extends Entity {

	
	
	public Object colitem;
	
	final float maxvel = 15f;
	final float accel = 10f;
	final float damp1 = 5.0f;
	final float damp2 = 1.0f;
	Vector2 impulse;
	
	final float jumpPower = 40;
	public Fixture playerSensorFixture;
	Vector2 sensorPos;
	boolean jumping = false;
	
	
	int animState = 0;
	static final int run = 0;
	static final int jump = 1;
	
	
	public Player(PlayState gs,float x, float y){
		super(gs);
		anim = new Animation("cybertrent.png",40,40,new int[]{4,3},false);
		anim.setDelay(100);
		setGravityDir(gs.getGravityDirection());
		width = height = 2;
		pwidth = pheight = 4;
		
		
		BodyDef def = new BodyDef();
		def.type = BodyType.DynamicBody;
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

	}

	boolean turnme = false;
	float diff = -1f;
	
	BitmapFont bmf;
	public void update(){
		setGravityDir(gs.getGravityDirection());
		
		
		
		if(isGrounded()){
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

	
	
	DecimalFormat df = new DecimalFormat("##.###");

	
	

	
	
	
	String cleanCoords(Vector2 v2){
		return "(" + 
					df.format(MathUtils.normalAngle(v2.x)) + ", " + 
				df.format(MathUtils.normalAngle(v2.y)) + ")";
	}
	
	
	
	Vector2 jumpVector = new Vector2();
	boolean UP = false, DOWN = false, LEFT = false, RIGHT = false;

	
	public boolean grounded;
	
	public void keyDown(int k){
		switch(k){
		case Keys.EQUALS: gs.debugCollisions = !gs.debugCollisions;
			break;
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
			
		case Keys.E: 
			if(colitem instanceof Entity)
							((Entity)colitem).activate();
			break;
			
		
		case Keys.SPACE:
			if(isGrounded()){
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




	public boolean isGrounded() {				
		
		Array<Contact> contactList = gs.world.getContactList();
		for(int i = 0; i < contactList.size; i++) {
			Contact contact = contactList.get(i);
			if(contact.isTouching() && (contact.getFixtureA() == playerSensorFixture ||
			   contact.getFixtureB() == playerSensorFixture)) {				
				Object ua = contact.getFixtureA().getBody().getUserData();
				Object ub = contact.getFixtureB().getBody().getUserData();
				if(contact.getFixtureA() == playerSensorFixture){
					if(ub instanceof String){
						if(!((String)ub).equals("world")){
							continue;
						}
					}else continue;
				}else{
					if(ua instanceof String){
						if(!((String)ua).equals("world")){
							continue;
						}
					}else continue;
				}
				
				
				Vector2 pos = body.getPosition();
				WorldManifold manifold = contact.getWorldManifold();
				boolean below = true;
				for(int j = 0; j < manifold.getNumberOfContactPoints(); j++) {
					below &= (manifold.getPoints()[j].y < pos.y - 1.5f);
				}
				
				if(below) {
																
					return true;			
				}
				
				return false;
			}
		}
		return false;
	}


	public void notifyGravity() {
		
		
	}

	@Override
	public void activate() {
		//No action
		
	}
	
}
