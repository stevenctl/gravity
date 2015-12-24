
package com.sugarware.gravity.levels;

import java.util.ArrayList;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.badlogic.gdx.utils.Array;
import com.sugarware.gravity.Angles;
import com.sugarware.gravity.entities.Entity;
import com.sugarware.gravity.entities.Player;

public abstract class PlayState extends GameState{
	
	
	Player p;
	public ArrayList<Entity> entities;
	public PlayState(String map_path, float ang, float mag) {
		super(map_path, ang, mag);
		entities = new ArrayList<Entity>();
	}

	
	
	public void update(){
		
		p.update();
		for(Entity e : entities)e.update();
		cam.position.set(p.body.getPosition().x, p.body.getPosition().y, 0);
		
		if(cam.position.x - cam.viewportWidth / 2 < 0){
			cam.position.x = cam.viewportWidth / 2;	
		}else if(cam.position.x + cam.viewportHeight / 2 > w / 8){
			cam.position.x = w / 8 - cam.viewportHeight / 2;
		}
		
		
		if(cam.position.y - cam.viewportHeight / 2 < 0){
			cam.position.y = cam.viewportHeight / 2;	
		}else if(cam.position.y + cam.viewportHeight / 2 > h / 8){
			cam.position.y = h / 8 - cam.viewportHeight / 2;
		}
		
		
		
		
		
		cam.update();
		super.update();
		
	}
	
    

	@Override
	public void keyDown(int keycode) {
		
		
		switch(keycode){
		case Keys.RIGHT:
			cam.position.x+= 6;
			break;
		case Keys.LEFT:
			cam.position.x-= 6;
			break;
		case Keys.UP:
			cam.position.y+= 6;
			break;
		case Keys.DOWN:
			cam.position.y-= 6;
			break;
			
		case Keys.NUMPAD_8:
			gTheta = Angles.UP;
			break;
		case Keys.NUMPAD_2:
			gTheta = Angles.DOWN;
			break;
		case Keys.NUMPAD_4:
			gTheta = Angles.LEFT;
			break;
		case Keys.NUMPAD_6:
			gTheta = Angles.RIGHT;
			break;
				
		case Keys.NUM_1:
			gVal = 1;
			break;
		case Keys.NUM_2:
			gVal = 5;
			break;
		case Keys.NUM_3:
			gVal = 10;
			break;
		case Keys.NUM_4:
			gVal = 0;
			break;
		
		}
		updateGravity();
		
		p.keyDown(keycode);
		cam.update();
	}

	@Override
	public void keyUp(int keycode) {
		// TODO Auto-generated method stub
		p.keyUp(keycode);
	}

	public void notifyGravity() {
		if(p != null)p.notifyGravity();
	}
	
	public static enum Directions{
		Right, Left, Up, Down
	}
	
	
	
	
	public Directions getGravityDirection(){
		//gTheta = MathUtils.normalAngle(gTheta);
		
		 if(gTheta > Math.PI / 4 && gTheta < Math.PI *  3 / 4){
			return Directions.Up;
		}else if(gTheta < Math.PI * 5 / 4 && gTheta > Math.PI *  3 / 4){
			return Directions.Left;
		}else if(gTheta > Math.PI * 5/ 4 && gTheta < Math.PI *  7 / 4){
			return Directions.Down;
		}else {
			return Directions.Right;
		}
			
	}


	
	public void draw2(SpriteBatch g) {
		
		for(Entity e : entities)e.draw(g);
		p.draw(g);
		
		
	}

}
