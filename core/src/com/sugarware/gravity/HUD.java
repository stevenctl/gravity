package com.sugarware.gravity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class HUD {
	protected int top;
	protected int right;
	protected SpriteBatch g;
	protected BitmapFont bmf;
	public HUD(){
		g = new SpriteBatch();
		top = Gdx.graphics.getHeight();
		right = Gdx.graphics.getWidth();
		bmf = new BitmapFont();
	}
	
	public abstract void draw();
}
