package com.game.bomberboom.core;

import com.badlogic.gdx.Game;
import com.game.bomberboom.screen.Splash;


public class BomberBoom extends Game {

	
	@Override
	public void create () {
        setScreen(new Splash());
		//setScreen(new GamePlay());
	}

	@Override
	public void resize(int width, int height){
		super.resize(width, height);
	}
	
	@Override
	public void render () {
		super.render();
	}
	
	@Override 
	public void pause(){
		super.pause();
	}
	
	@Override
	public void dispose(){
		super.dispose();
	}

}
