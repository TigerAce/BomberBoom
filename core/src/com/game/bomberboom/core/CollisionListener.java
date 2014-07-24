package com.game.bomberboom.core;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;



public class CollisionListener implements ContactListener {

	@Override
	public void beginContact(Contact contact) {
		// TODO Auto-generated method stub
		//System.out.println("BC");
	}

	@Override
	public void endContact(Contact contact) {
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		//System.out.println("PRS");
		if(contact.getFixtureA().getBody().getUserData() != null && contact.getFixtureB().getBody().getUserData() != null)
		{
			String nameA = ((MyUserData)contact.getFixtureA().getBody().getUserData()).getName();
		
			String nameB = ((MyUserData)contact.getFixtureB().getBody().getUserData()).getName();
			if(nameA != null && nameB != null){
				if(nameA == "player" && nameB == "brick"){
		
	//		System.out.println("c");
		//contact.getFixtureB().getBody().s;
				}
				if(nameA == "brick" && nameB == "player"){
					contact.setEnabled(false);
				}
			}
		}
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
	
		MyUserData userDataA = (MyUserData)contact.getFixtureA().getBody().getUserData();
		MyUserData userDataB = (MyUserData)contact.getFixtureB().getBody().getUserData();
		if(userDataA != null && userDataB != null)
		{
			String nameA = userDataA.getName();
		
			String nameB = userDataB.getName();
			if(nameA != null && nameB != null){
				if(nameA == "particle" && nameB == "crate"){
					((MyCrate)userDataB.getObject()).setLife(((MyCrate)userDataB.getObject()).getLife() - impulse.getNormalImpulses()[0]);
					
				}
				if(nameA == "crate" && nameB == "particle"){
					((MyCrate)userDataA.getObject()).setLife(((MyCrate)userDataA.getObject()).getLife() - impulse.getNormalImpulses()[0]);
				
				}
			}
		}
	}

}
