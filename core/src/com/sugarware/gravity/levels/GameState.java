package com.sugarware.gravity.levels;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class GameState {
	public OrthographicCamera cam;
	public abstract void update();
	public abstract void draw(SpriteBatch g);
	public abstract void keyDown(int k);
	public abstract void keyUp(int k);
	public abstract void init();
}
