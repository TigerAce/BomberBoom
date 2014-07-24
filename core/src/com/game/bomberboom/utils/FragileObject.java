package com.game.bomberboom.utils;

import com.badlogic.gdx.utils.Array;
import com.game.bomberboom.model.GameObject;

public abstract class FragileObject extends GameObject{

	public FragileObject() {
		// TODO Auto-generated constructor stub
		super(0, 0);
	}

	@Override
	public void draw() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}
	
	public abstract Array<FragileObject> breakDown();

}
