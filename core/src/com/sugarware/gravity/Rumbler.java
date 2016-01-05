package com.sugarware.gravity;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;

public class Rumbler  {
	Camera cam;
	
	public boolean killme = false;
	
	float radius;
	float ang = (float) (Math.random() * Math.PI * 2f);
	Vector2	offset;
	
	
	public Rumbler(Camera c, float r){
		cam = c;
		radius = r;
		offset=  new Vector2( (float)Math.sin(ang) * radius ,(float) Math.cos(ang) * radius); 
		
	}
	
	

	
	public void update() {
		if(radius < 2)killme = true;
		radius *= 0.9f;
		ang += Math.PI + (Math.random() > 0.5 ? 1 : -1 ) * (Math.PI / 3f);
		offset.set( (float)Math.sin(ang) * radius ,(float) Math.cos(ang) * radius); 
		cam.position.set(cam.position.x + offset.x, cam.position.y + offset.y, 0);
		cam.update();
		try{Thread.sleep(1000 / 30);}catch(Exception e){}
	}
}
