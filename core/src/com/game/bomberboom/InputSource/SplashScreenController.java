package com.game.bomberboom.InputSource;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.game.bomberboom.screen.GamePlay;

/**
 * Created by Tong on 2014/7/24.
 */
public class SplashScreenController implements InputProcessor{

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {

        /**
         * KeyUp bindings for splash screen state
         */
        switch (keycode) {
            case Input.Keys.ESCAPE:
                // Skip the splash screen and go into GamePlay screen
                ((Game) Gdx.app.getApplicationListener()).setScreen(new GamePlay());
                break;
        }
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
