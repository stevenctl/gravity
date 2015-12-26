package com.sugarware.gravity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sugarware.gravity.levels.GameState;
import com.sugarware.gravity.levels.TestState;

public class GameStateManager {
	
	static GameStateManager gsm;
	
	public GameStateManager(State s){
		setState(s);
	}
	
	public static GameStateManager getInstance(){
		if(gsm == null)gsm = new GameStateManager(State.Test);
		return gsm;
	}
	
	public static enum State{
		Menu, Test
	}
	
	public GameState currentState;
	
	public void setState(State s){
		switch(s){
		case Menu:
			break;
		case Test: currentState = new TestState();
			break;
		}
	}
	
	void tick(SpriteBatch g){		
		currentState.update();
		currentState.draw(g);
	}

	public void keyDown(int keycode) {
		currentState.keyDown(keycode);
	}
	
	public void keyUp(int keycode) {
		currentState.keyUp(keycode);
	}
	
}
