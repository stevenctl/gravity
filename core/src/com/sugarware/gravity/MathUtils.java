package com.sugarware.gravity;

import com.badlogic.gdx.math.Vector2;
import com.sugarware.gravity.levels.PlayState.Directions;

public class MathUtils {
	
	public static float normalAngle(float theta){
		while(theta >= Math.PI * 2){
			theta -= Math.PI * 2;
		}
		while(theta < 0){
			theta +=Math.PI * 2;
		}
		return theta;
	}
	
	public static float normalAngle(double theta){
		return normalAngle((float) theta);
	}
	
	public static Vector2 angMagToXY(Vector2 v2){
		float theta = v2.x;
		float mag = v2.y;
		v2.set((float)Math.cos(theta) * mag,
			   (float)Math.sin(theta) * mag);
		return v2;
	}
	
	public static float dirToAngle(Directions d){
		switch(d){
		case Up: return (float) (Math.PI / 2);
		case Down: return (float) (3 * Math.PI / 2);
		case Right: return 0;
		case Left: return (float) Math.PI;
		}
		return -1;
	}
	
	
}
