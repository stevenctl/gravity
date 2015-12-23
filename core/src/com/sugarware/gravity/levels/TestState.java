package com.sugarware.gravity.levels;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sugarware.gravity.Angles;
import com.sugarware.gravity.entities.Player;

public class TestState extends PlayState {

	public TestState() {
		super("test.tmx", Angles.DOWN, 6 * 9.8f);
		p = new Player(this,20, 60);
		cam.viewportWidth = 60; cam.viewportHeight = 30;
		cam.update();
		//cam.position.set(p.body.getPosition().x,p.body.getPosition().y,0);
		//cam.update();
	}

	@Override
	public void draw2(SpriteBatch g) {	
		p.draw(g);
	}

}
