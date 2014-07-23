package com.game.bomberboom.model;

public abstract class Crate extends GameObject{
	protected int size;
	
	protected Crate(float x, float y, int size){
		super(x, y);
		this.size = size;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
	
}
