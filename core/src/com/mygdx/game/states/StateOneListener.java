package com.mygdx.game.states;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.game.objects.EmptyContact;
import com.mygdx.game.objects.PlatformPlayer;
import com.mygdx.game.test.Person;

public class StateOneListener extends EmptyContact{

	
	public StateOneListener(State state) {
		super(state);
	}

	public void beginContact(Contact contact) {
		PlatformPlayer.beginContact(contact, this);
		
		if(compareCollision(contact, Person.class, Person.class)) {
			contact.setEnabled(false);
		}
		
	}

	public void endContact(Contact contact) {
		PlatformPlayer.endContact(contact, this);
	}

	public void preSolve(Contact contact, Manifold oldManifold) {

		if(compareCollision(contact, Person.class, Person.class)) {
			contact.setEnabled(false);
		}
	}

	public void postSolve(Contact contact, ContactImpulse impulse) {
		
	}

}
