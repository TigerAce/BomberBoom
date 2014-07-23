package com.game.bomberboom.model;

public abstract class Player {
	//location of player
	protected float x;
	protected float y;
	
	protected float speed;
	
	protected Player(float xPos, float yPos, float speed){
		this.x = xPos;
		this.y = yPos;
		this.speed = speed;
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

	public void setY(float y) {
		this.y = y;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public abstract void draw();
	public abstract void move();
	public abstract void placeBomb();
	
}
