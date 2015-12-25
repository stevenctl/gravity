package com.sugarware.gravity;

import java.util.Scanner;

import com.sugarware.gravity.entities.GravSwitch;
import com.sugarware.gravity.levels.PlayState;
import com.sugarware.gravity.levels.PlayState.Directions;

public class Console implements Runnable{

	GdxGame g;
	
	public Console(GdxGame game){
		g = game;
	}
	
	@Override
	public void run() {
		Scanner sc = new Scanner(System.in);
		while(true){
			String in = sc.nextLine();
			cmd(in);
			
			
		}
	}

	
	
	public static void cmd(String in){
		try{
			
			String[] parts = in.split(" ");
			
			
			//6 piece (5 arg) commands
			if(parts.length > 5){
				if(parts[0].equals("add")){
					if(parts[1].equals("switch")){
						float x = Float.parseFloat(parts[2]);
						float y = Float.parseFloat(parts[3]);
						Directions d1 = strToDir(parts[4]);
						Directions d2 = strToDir(parts[5]);
						GravSwitch s = new GravSwitch((PlayState)GameStateManager.getInstance().currentState,x,y,d1);
						s.mySwitchDir = d2;
						((PlayState)GameStateManager.getInstance().currentState).entities.add(s);
					}
				}
			}
			
			}catch(Exception e){
				System.err.println(e.getCause());
			}
	}
	
	
	public static Directions strToDir(String s){
		System.out.println("d: " + s);
		if(s.equals("up"))return Directions.Up;
		if(s.equals("down"))return Directions.Down;
		if(s.equals("left"))return Directions.Left;
		if(s.equals("right"))return Directions.Right;
		
		
		return null;
	}
}
