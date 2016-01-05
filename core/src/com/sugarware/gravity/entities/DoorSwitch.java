package com.sugarware.gravity.entities;

import com.badlogic.gdx.math.Vector2;
import com.sugarware.gravity.levels.PlayState;
import com.sugarware.gravity.levels.PlayState.Directions;
import com.sugarware.gravity.levels.TextDisplay;

public class DoorSwitch extends Switch {

	
	public Directions mySwitchDir;
	Door door;
	
	public DoorSwitch(PlayState gs, Vector2 pos, Directions d, Door dr) {
		this(gs, pos.x, pos.y, d, dr);
	}
	
	
	
	public DoorSwitch(PlayState gs, float x, float y, Directions d, Door dr) {
		super(gs, x, y, d);
		door = dr;
	}

	@Override
	public void activate() {
		
		if(door.locked){
			TextDisplay.pleaseDraw("Door unlocked!");
			door.unlock();
		}
		
	}

	@Override
	public void update() {
		if(!door.locked){
			anim.setFrame(0);
			activated = true;
		}else{
			anim.setFrame(1);
			activated = false;
		}
	}



	
	
	

}
