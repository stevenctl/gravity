package com.sugarware.gravity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import com.sugarware.gravity.levels.PlayState;

public class Script {
	HashMap<Long, Integer> up;
	HashMap<Long, Integer> down;
	public Script(){
		up = new HashMap<Long, Integer>();
		down = new HashMap<Long, Integer>();
	}
	
	
	
	public Script(InputStream read) {
		this();
		BufferedReader sc = new BufferedReader(new InputStreamReader(read));
		
		String line;
		try {
			line = sc.readLine();
		
		while(line != null){
			if(line.length() <= 0)break;
			String[] l = line.split(" ");
			if(l.length != 3)continue;
			(l[0].equals("u") ? up : down).put(Long.parseLong(l[1]), Integer.parseInt(l[2]));
			line = sc.readLine();
		}
		sc.close();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
/*
	public void write(String filename) throws FileNotFoundException{
		File f = new File(filename);
		PrintWriter pw = new PrintWriter(f);
		for(Long t : up.keySet()){
			pw.println("u " + t + " " + up.get(t));
		}
		for(Long t : down.keySet()){
			pw.println("d " + t + " " + down.get(t));
		}
		pw.close();
	}*/
	
	public void keyDown(long t, int k){
		down.put(t, k);
	}
	
	public void keyUp(long t, int k){
		up.put(t, k);
	}
	
	public void update(PlayState gs, long t){
		if(up.containsKey(t)){
			gs.keyUp(up.get(t));
		}
		
		if(down.containsKey(t)){
			gs.keyDown(down.get(t));
		}
	}
}
