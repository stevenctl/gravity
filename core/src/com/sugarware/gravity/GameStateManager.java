package com.sugarware.gravity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.sugarware.gravity.levels.GameState;
import com.sugarware.gravity.levels.Level1;
import com.sugarware.gravity.levels.Level2;
import com.sugarware.gravity.levels.TestState;

public class GameStateManager {
	final float defTime = 80;
	static GameStateManager gsm;
	boolean transition = false;
	State destination = null;
	int tTime = (int) defTime;
	ShapeRenderer transRenderer;
	public GameStateManager(State s){
		setState(s);
	}
	
	public static GameStateManager getInstance(){
		if(gsm == null)gsm = new GameStateManager(State.Test);
		return gsm;
	}
	
	public static enum State{
		Menu, Test, Level1, Level2
	}
	
	public GameState currentState;
	
	public void setState(State s){
		setState(s, false);
	}
	
	public void setState(State s, boolean t){
		if(t)transition = true;
		if(t){
			transRenderer = new ShapeRenderer();
			tTime = (int) defTime;
			destination = s;
			return;
		}
		switch(s){
		case Menu:
			break;
		case Test: currentState = new TestState();
			break;
		case Level1:
			currentState = new Level1();
			break;
		case Level2:
			currentState = new Level2();
			break;
		}
	}
	void tick(SpriteBatch g){		
		currentState.update();
		currentState.draw(g);
		
		if(transition){
			Gdx.gl.glEnable(GL20.GL_BLEND);
			float alpha = (defTime - Math.abs(tTime - 50))/defTime;
			transRenderer.begin(ShapeType.Filled);
			transRenderer.setColor(0, 0, 0, alpha);
			transRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			transRenderer.end();
			if(alpha == 1){
				setState(destination);
			}
			Gdx.gl.glDisable(GL20.GL_BLEND);
			tTime--;
			if(tTime <= 0){
				transRenderer.dispose();
				transRenderer = null;
				transition = false;
			}
		}
		
		
	}

	public void keyDown(int keycode) {
		currentState.keyDown(keycode);
	}
	
	public void keyUp(int keycode) {
		currentState.keyUp(keycode);
	}

	public boolean isTransitioning() {
		// TODO Auto-generated method stub
		return transition;
	}
	
}
