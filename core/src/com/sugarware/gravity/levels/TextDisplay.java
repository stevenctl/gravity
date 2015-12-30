package com.sugarware.gravity.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Align;

public class TextDisplay {
	static BitmapFont bmf;
	static String str;
	static TextureRegion top, mid, bot;
	static int time;
	
	public static void init(){
		bmf = new BitmapFont();
		top = new TextureRegion(new Texture(Gdx.files.internal("uitop.png")));
		mid = new TextureRegion(new Texture(Gdx.files.internal("uimid.png")));
		bot = new TextureRegion(new Texture(Gdx.files.internal("uibot.png")));	
	}
	
	public static void pleaseDraw(String str){
		TextDisplay.str = str;
		time = 300;
	}
	
	public static void pleaseDraw(String str, int t){
		TextDisplay.str = str;
		time = t;
	}
	
	public static void draw(SpriteBatch g){
		if(str == null)return;
		if(g.isDrawing())g.end();
		int w = Gdx.graphics.getWidth();
		int h = Gdx.graphics.getHeight();
		g.begin();
		g.draw(bot, 0, 0, w, h / 10);
		g.draw(mid, 0, h / 10, w,h / 7);
		g.draw(top, 0, h / 10 + h / 7, w, h / 10);
		
		
		bmf.draw(g, str, w / 20, h / 4, 0, str.length() , 9 * w / 10
				, Align.topLeft, true);
		g.end();
		time--;
		if(time <= 0)str = null;
		
	}

	public static boolean isDrawing() {
		return str != null;
		
	}
}
