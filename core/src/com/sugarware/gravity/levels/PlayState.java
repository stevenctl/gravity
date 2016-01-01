
package com.sugarware.gravity.levels;

import java.util.ArrayList;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sugarware.gravity.Angles;
import com.sugarware.gravity.CollisionBits;
import com.sugarware.gravity.entities.Entity;
import com.sugarware.gravity.entities.MapBodyBuilder;
import com.sugarware.gravity.entities.Player;
import com.sugarware.gravity.entities.SpaceJunk;

public abstract class PlayState extends IngameState{
	
	boolean loaded = false;
	Player p;
	public ArrayList<Entity> entities;
	public PlayState(String map_path, float ang, float mag) {
		super(map_path, ang, mag);
		entities = new ArrayList<Entity>();
		
	}
	
	public void init(){
		super.init();
		loaded = false;
		entities.clear();
		
	}
	
	public void update(){
		if(!loaded){
			load();
			p.setBits(CollisionBits.CATEGORY_ENTITY, CollisionBits.MASK_ENTITY);
			loaded = true;
		}
		p.update();
		for(Entity e : entities){
			e.update();
			if(!e.bitsSet){
				e.setBits(CollisionBits.CATEGORY_ENTITY, CollisionBits.MASK_ENTITY);
			}
		}
		cam.position.set(p.body.getPosition().x, p.body.getPosition().y, 0);
		
		if(cam.position.x - cam.viewportWidth / 2 < 0){
			cam.position.x = cam.viewportWidth / 2;	
		}else if(cam.position.x + cam.viewportWidth / 2 > w / 8){
			cam.position.x = (w / 8) - cam.viewportWidth / 2;
		}
//		
		if(cam.position.y - cam.viewportHeight / 2 < 0){
			cam.position.y = cam.viewportHeight / 2;	
		}else if(cam.position.y + cam.viewportHeight / 2 > h / 8){
			cam.position.y = h / 8 - cam.viewportHeight / 2;
		}
		
		
		if(p.body.getPosition().y < -1)init();
		
		
		cam.update();
		super.update();
		
	}
	
    

	private void load() {
		MapBodyBuilder.buildEntities(this);
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
		case Keys.NUM_0:
			SpaceJunk sj = new SpaceJunk(this,p.body.getPosition().x, p.body.getPosition().y,
					"manbearpig.jpg",459,348,new int[]{1});
			sj.pwidth = 12;
			sj.pheight = 5;
			entities.add(sj);
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


	public void draw(SpriteBatch g){
		super.draw(g);
		p.hud.draw();
	}
	
	
	
	public void draw1(SpriteBatch g){
		for(Entity e : entities){
			if(e instanceof SpaceJunk)e.draw(g);
		}
	}
	
	public void draw2(SpriteBatch g) {
		
		for(Entity e : entities){
			if(e instanceof SpaceJunk)continue;//Things already drawn
			
			e.draw(g);
		}
		p.draw(g);
		
		
	}

}
