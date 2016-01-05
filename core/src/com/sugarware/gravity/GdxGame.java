package com.sugarware.gravity;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class GdxGame extends ApplicationAdapter implements InputProcessor {
	
	public static boolean smokeweed;
	SpriteBatch g;
	
	public static float aspect;
	@Override
	public void create () {
		g = new SpriteBatch();
		aspect = (float) Gdx.graphics.getHeight() /(float) Gdx.graphics.getWidth() ;
		GameStateManager.getInstance();
		Gdx.input.setInputProcessor(this);
		
		new Thread(new Console(this)).start();
	}

	
	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		GameStateManager.getInstance().tick(g);
	
		//Gdx.gl.glDisable(GL20.GL_BLEND);
	}

	@Override
	public boolean keyDown(int keycode) {
		
		GameStateManager.getInstance().keyDown(keycode);
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		GameStateManager.getInstance().keyUp(keycode);
		return false;
	}

	
	
	
	
	
	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Vector3 coords = new Vector3(screenX, screenY, 0);
		coords =GameStateManager.getInstance().currentState.cam.unproject(coords);
		
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}
