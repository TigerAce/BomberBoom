package com.game.bomberboom.core;

import com.badlogic.gdx.Game;
import com.game.bomberboom.screen.Splash;


public class BomberBoom extends Game {

    /**
     * Define different game state
     */
    public static final int INIT_STATE = 0;
	public static final int SPLASH_SCREEN_STATE = 1;
    public static final int GAME_PLAY_STATE = 2;

    /**
     * Set the initial game state to -1, so that it won't math any of the states declared above.
     * If gameState is -1 after game started, then something bad is happening
     */
    private int gameState = -1;

	@Override
	public void create () {

        /**
         * Set up initial game state
         */
        gameState = INIT_STATE;

        /**
         * A splash screen will be loaded when game starts
         * GamePlay screen will be loaded after this Splash screen, which is implemented inside Splash.show()
         */
        setScreen(new Splash());
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

    public int getGameState() {
        return gameState;
    }

    public void setGameState(int gameState) {
        this.gameState = gameState;
    }
}
