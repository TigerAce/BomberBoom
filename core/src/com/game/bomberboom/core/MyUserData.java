package com.game.bomberboom.core;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class MyUserData {
	private String name;
	private Object object;
	private Sprite sprite;
	
	public MyUserData(String name, Object object, Sprite sprite) {
		this.name = name;
		this.object = object;
		this.sprite = sprite;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Object getObject() {
		return object;
	}
	public void setObject(Object object) {
		this.object = object;
	}

	public Sprite getSprite() {
		return sprite;
	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}
}
