package com.sugarware.gravity;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class GdxGame extends ApplicationAdapter implements InputProcessor {
	
	public static boolean smokeweed;
	SpriteBatch g;
	private ExecutorService executor;
	
	@Override
	public void create () {
		g = new SpriteBatch();
		GameStateManager.getInstance();
		Gdx.input.setInputProcessor(this);
		executor = Executors.newFixedThreadPool(25);
		new Thread(new Console(this)).start();
	}

	boolean record = false;
	boolean ee = false;
	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		GameStateManager.getInstance().tick(g);
		if(record){
			if(ee){
				ScreenShot worker = new ScreenShot();
				worker.prepare();			
				executor.execute(worker);	
			}
			ee = !ee;
			
			
		}
		//Gdx.gl.glDisable(GL20.GL_BLEND);
	}

	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Keys.R){record = !record;
		if(record == false){
			try {
				File dir = new File("C:\\tmp");
				File[] files = dir.listFiles();
				if(files == null){
					System.err.println("Couldn't out gif");
					return false;
				}
				BufferedImage firstImage = ImageIO.read(files[0]);
				File outFile = new File("gameplay.gif");
				int n = 1;
				while(outFile.exists()){
					outFile = new File("gameplay" + (n++) + ".gif");
				}
				ImageOutputStream output = 
					      new FileImageOutputStream(outFile);
				
				
				GifSequenceWriter writer = 
					      new GifSequenceWriter(output, firstImage.getType(), 1, false);
				writer.writeToSequence(firstImage);
			    for(int i=1; i< files.length-1; i++) {
			      BufferedImage nextImage = ImageIO.read(files[i]);
			      files[i].delete();
			      writer.writeToSequence(nextImage);
			    }

			    writer.close();
			    output.close();
				
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		
		}
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
