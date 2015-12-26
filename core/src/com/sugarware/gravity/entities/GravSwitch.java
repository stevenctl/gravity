package com.sugarware.gravity.entities;

import com.badlogic.gdx.math.Vector2;
import com.sugarware.gravity.MathUtils;
import com.sugarware.gravity.levels.PlayState;
import com.sugarware.gravity.levels.PlayState.Directions;

public class GravSwitch extends Switch {

	
	public Directions mySwitchDir;
	
	public GravSwitch(PlayState gs, Vector2 pos, Directions d) {
		this(gs, pos.x, pos.y, d);
	}
	
	
	
	public GravSwitch(PlayState gs, float x, float y, Directions d) {
		super(gs, x, y, d);
		mySwitchDir = d;
	}

	@Override
	public void activate() {
		gs.setGravity(MathUtils.dirToAngle(mySwitchDir));
	}

	@Override
	public void update() {
		if(mySwitchDir == gs.getGravityDirection()){
			anim.setFrame(0);
			activated = true;
		}else{
			anim.setFrame(1);
			activated = false;
		}
	}
	
	

}
