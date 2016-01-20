package com.sugarware.gravity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.sugarware.gravity.levels.PlayState;
import com.sugarware.gravity.levels.PlayState.Directions;

public class OnscreenController implements InputProcessor {

	static int w,h;
	int[] keys;
	GdxGame game;
	public OnscreenController(GdxGame game){
		keys = new int[10];
		this.game = game;
	}
	
	
	@Override
	public boolean keyDown(int keycode) {
		game.keyDown(keycode);
		return false;
	}

	public void assignKey(int x, int y, int p){
		y = h - y;
		if(x  < w / 2){
			if(GameStateManager.isPlayState()){
			
				Directions gdir = ((PlayState)GameStateManager.getInstance().currentState).getGravityDirection();
				if(gdir == Directions.Left || gdir == Directions.Right){
					if(y > h / 2){
						keys[p] = Keys.W;
						
					}else{
						keys[p] = Keys.S;
						
					}
				}else{
					if(x > w / 4){
					   keys[p] = Keys.D;
					   
					}else{
						keys[p] = Keys.A;
						
					}
				}
			}
		
		}else{
			if(x > 7 * w / 8 - 5 && y < w / 8 + 5){
				keys[p] = Keys.E;
			}else{
				keys[p] = Keys.SPACE;
			}
		}
		
	}
	
	@Override
	public boolean keyUp(int keycode) {
		game.keyUp(keycode);
		return false;
	}

	
	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int p, int button) {
		
		assignKey(x,y,p);
		keyDown(keys[p]);
		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int p, int button) {
		assignKey(x, y, p);
		keyUp(keys[p]);
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
	TextureRegion ebtn;
	SpriteBatch uibatch;
	public void draw(){
		if(uibatch == null){
			uibatch = new SpriteBatch();
			//dpad = new TextureRegion(new Texture("dpad.png"));
		//	jbtn = new TextureRegion(new Texture("ubtn.png"));
			ebtn = new TextureRegion(new Texture("ebtn.png"));
			w = Gdx.graphics.getWidth();
			h = Gdx.graphics.getHeight();
		}
		
		uibatch.begin();
		//uibatch.draw(dpad, 5, 5,  w / 6, w / 6);
		//uibatch.draw(jbtn, 3 * w / 4, 5, w / 8, w / 8);
		uibatch.draw(ebtn, 7 * w / 8 - 5, 5, w / 8, w/ 8);
		uibatch.end();
		
		
		
	}

}
