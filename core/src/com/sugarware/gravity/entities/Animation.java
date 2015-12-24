package com.sugarware.gravity.entities;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

//Controlls image cycling for animatons.

public class Animation {
	
	ArrayList<TextureRegion[]> animationList;
	
	private TextureRegion[] frames;
	private int curFrame;
	
	private long startTime;
	private long delay;
	
	private boolean playOnce;
	
	public Animation(String path,int w, int h,int[] numF) {
		this(path,w, h, numF, false);
	}
	
	public Animation() {
		this(false);
	}
	
	public Animation(boolean playOnce) {
		this.playOnce = playOnce;
	}
	
	public Animation(String path,int w, int h,int[] numF, boolean playOnce) {
		animationList = loadImages(path,w,h,numF);
		setFrames(animationList.get(0));
		this.playOnce = playOnce;
	}
	
	public void setFrames(TextureRegion[] frames) {
		 this.frames = frames;
		 curFrame = 0;
		 startTime = System.nanoTime();
		 playOnce = false;
	}
	
	public void setFrames(int i){
		setFrames(animationList.get(i));
	}
	
	public void setFrames(int i, int f){
		setFrames(animationList.get(i), f);
	}
	
	public void setFrames(TextureRegion[] frames, int f){
		 this.frames = frames;
		 //playOnce = false;
	}
	
	public void setDelay(long d) { delay = d;}
	public void setFrame(int i) { curFrame = i;}
	
	public void update(){
		if(delay == -1 ) return; //NO ANIMATION
		
		long elapsed = (System.nanoTime() - startTime) / 1000000;
		if (elapsed > delay) {
			curFrame++;
			
			startTime = System.nanoTime();
		}
		if(frames != null){
		if(curFrame == frames.length - 1){
			playOnce = true;
		}
		if(curFrame == frames.length){
			curFrame = 0;
			
			
		}
		}
		
	}
	
	public Animation clone(){
		Animation n = new Animation();
		n.animationList = this.animationList;
		n.setFrames(0);
		n.delay = delay;
		n.playOnce = playOnce;
		return n;
	}
	
	
	public TextureRegion[] getFrames(){return frames;}
	public int getFrame() {return curFrame;}
	public int getNumFrames(){return frames.length;}
	public TextureRegion getImage() {if(frames != null) {return frames[curFrame];}else{System.out.println("NO IMAGE!");return null;}}
	public boolean hasPlayedOnce(){return playOnce;}
	public void setPlayedOnce(boolean b){playOnce = b;}
	public long getDelay() {return delay;}
	
	
public static ArrayList<TextureRegion[]> loadImages(String path, int w, int h, int[] numFrames){
		
		
		
	System.out.println("Loading " + path);
		
		
	
		
		
		int numSprites = numFrames.length;
		ArrayList<TextureRegion[]> sprites = new ArrayList<TextureRegion[]>();
		
		
		try {
			Texture spritesheet = new Texture(Gdx.files.internal(path));
			TextureRegion[][] sheet = TextureRegion.split(spritesheet,w,h);
			
			for(int i = 0; i < numSprites; i++){
				TextureRegion[] sprite = new TextureRegion[numFrames[i]];
				for(int j = 0; j <numFrames[i]; j++){
					sheet[i][j].flip(false, true);
					sprite[j] = sheet[i][j];
				}
				sprites.add(sprite);
			}
			

			
			return sprites;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
			
		}
	}
	
}