package com.game.bomberboom.model;

public abstract class GameObject {
	protected float x;
	protected float y;
	
	GameObject(float x, float y){
		this.x = x;
		this.y = y;
	}
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float f) {
		this.y = f;
	}
	
	public abstract void draw();

}
