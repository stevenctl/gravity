package com.sugarware.gravity.levels;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.sugarware.gravity.entities.Door;
import com.sugarware.gravity.entities.Entity;
import com.sugarware.gravity.entities.Player;

public class CollisionListener implements ContactListener {

	@Override
	public void beginContact(Contact contact) {
		Fixture a = contact.getFixtureA();
		Fixture b = contact.getFixtureB();
		Player p = null;
		Object o = null;
		
		
		
		if(a.getBody().getUserData() instanceof Player){
			p = (Player)a.getBody().getUserData();
			o = (Object) b.getBody().getUserData();
		}
		if(b.getBody().getUserData() instanceof Player){
			p = (Player)b.getBody().getUserData();
			o = (Object)b.getBody().getUserData();
		}
		
		if(p == null)return;
		
		
		if(o instanceof Entity){
			if(o != p){
				if(o instanceof Door)((Door) o).open();
				p.colitem = o;
			}
				
		}
		//if(!(o instanceof Player))System.out.println(o);
		
		
	}

	@Override
	public void endContact(Contact contact) {
		Fixture a = contact.getFixtureA();
		Fixture b = contact.getFixtureB();
		Player p = null;
		Object o = null;
		
		if(a.getBody().getUserData() instanceof Player){
			p = (Player)a.getBody().getUserData();
			o = (Object) b.getBody().getUserData();
		}
		if(b.getBody().getUserData() instanceof Player){
			p = (Player)b.getBody().getUserData();
			o = (Object)b.getBody().getUserData();
		}
		
		if(p != null){
			if(p.colitem != null)
				if(o == p.colitem){
					if(o instanceof Door)((Door) o).close();
					p.colitem = null;
				}
					
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		
	}
	
}
