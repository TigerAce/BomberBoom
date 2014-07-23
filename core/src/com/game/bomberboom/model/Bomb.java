package com.game.bomberboom.model;

public abstract class Bomb extends GameObject{
	
	protected float time;
	protected float powerX;
	protected float powerY;
	
	protected Bomb(float x, float y, float time, float powerX, float powerY){
		super(x, y);
		this.time = time;
		this.powerX = powerX;
		this.powerY = powerY;
	}
	
	
	 public abstract void explode();
	
}
