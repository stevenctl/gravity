package com.sugarware.gravity;

import com.badlogic.gdx.math.Vector2;

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
	
}
