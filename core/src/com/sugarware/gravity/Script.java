package com.sugarware.gravity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;

import com.sugarware.gravity.levels.PlayState;

public class Script {
	HashMap<Long, Integer> up;
	HashMap<Long, Integer> down;
	public Script(){
		up = new HashMap<Long, Integer>();
		down = new HashMap<Long, Integer>();
	}
	
	public Script(String path) throws FileNotFoundException{
		this();
		File f = new File(path);
	 
		
		Scanner	sc = new Scanner(f);
			while(sc.hasNextLine()){
				String[] l = sc.nextLine().split(" ");
				if(l.length != 3)continue;
				(l[0].equals("u") ? up : down).put(Long.parseLong(l[1]), Integer.parseInt(l[2]));
			}
			sc.close();
		
	}
	
	public Script(InputStream read) {
		this();
		Scanner sc = new Scanner(read);
		while(sc.hasNextLine()){
			String[] l = sc.nextLine().split(" ");
			if(l.length != 3)continue;
			(l[0].equals("u") ? up : down).put(Long.parseLong(l[1]), Integer.parseInt(l[2]));
		}
		sc.close();
	}

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
	}
	
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
