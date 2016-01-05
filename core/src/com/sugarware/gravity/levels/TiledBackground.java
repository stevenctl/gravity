package com.sugarware.gravity.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TiledBackground {
	TextureRegion img;
	float w,  h;
	boolean paralax;
	private float pMod = 0.5f;
	public float xshift = 0;
	public float yshift = 0;
	
	public TiledBackground(String path){
		this(path, false);
	}
	
	public TiledBackground(String path, boolean p){
		img = new TextureRegion(new Texture(
				Gdx.files.internal(path)
				));
		w = img.getRegionWidth();
		h = img.getRegionHeight();
		paralax = p;
	}
	
	public TiledBackground(String path, float w, float h){
		this(path, w, h, false);
	}
	
	public TiledBackground(String path, float w, float h, boolean p){
		img = new TextureRegion(new Texture(
				Gdx.files.internal(path)
				));
		this.w = w;
		this.h = h;
		paralax = p;
	}
	
	
	public void setParalaxModifier(float mod){
		pMod = mod;
	}
	
	public float getParalaxModifier(){
		return pMod;
	}
	
	public void draw(SpriteBatch g, OrthographicCamera c){
	
		float x = paralax ? -  c.position.x * pMod: 0;
		x-= xshift;
		while(x > 0)x -= w;
		while(x < c.position.x + c.viewportWidth / 2){
			float y = paralax ? - c.position.y * pMod: 0 ;
			y -= yshift;
			while(y > 0)y -= h;
			while(y < c.position.y + c.viewportHeight / 2){
				g.draw(img, x, y, w, h);
				y+= h;
			}
			x += w;
		}
		
		
		
	}
}
