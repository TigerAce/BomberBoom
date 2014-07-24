package com.game.bomberboom.InputSource;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.game.bomberboom.core.MyBomb;
import com.game.bomberboom.core.MyPlayer;
import com.game.bomberboom.screen.GamePlay;

/**
 * Note: Not sure whether this is a good idea to separate key bindings from the main class.
 * This class seems is still highly coupled with parent screen by Player, World, and Camera.
 * Created by Tong on 2014/7/24.
 */
public class GameScreenController implements InputProcessor {

    /**
     * Store the reference to its parent screen
     */
    private GamePlay parentScreen;

    public GameScreenController(GamePlay screen) {
        parentScreen = screen;
    }

    @Override
    public boolean keyDown(int keycode) {

        switch (keycode) {
            case Input.Keys.ESCAPE:
                System.exit(1);
                break;
            case Input.Keys.W:
                parentScreen.getPlayer().setUpMove(true);
                parentScreen.getPlayer().setDir(MyPlayer.Direction.UP);
                break;
            case Input.Keys.A:
                parentScreen.getPlayer().setLeftMove(true);
                parentScreen.getPlayer().setDir(MyPlayer.Direction.LEFT);
                break;
            case Input.Keys.S:
                parentScreen.getPlayer().setDownMove(true);
                parentScreen.getPlayer().setDir(MyPlayer.Direction.DOWN);
                break;
            case Input.Keys.D:
                parentScreen.getPlayer().setRightMove(true);
                parentScreen.getPlayer().setDir(MyPlayer.Direction.RIGHT);
                break;

            case Input.Keys.F:
                //testBrick.boom();
                break;

        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {

        switch (keycode) {
            case Input.Keys.ESCAPE:
                System.exit(1);
                break;
            case Input.Keys.W:
                parentScreen.getPlayer().setUpMove(false);
                parentScreen.getPlayer().getPlayerBody().setLinearVelocity(new Vector2(0, 0));
                parentScreen.getWorld().clearForces();
                break;
            case Input.Keys.D:
                parentScreen.getPlayer().setRightMove(false);
                parentScreen.getPlayer().getPlayerBody().setLinearVelocity(new Vector2(0, 0));
                parentScreen.getWorld().clearForces();
                break;
            case Input.Keys.A:
                parentScreen.getPlayer().setLeftMove(false);
                parentScreen.getPlayer().getPlayerBody().setLinearVelocity(new Vector2(0, 0));
                parentScreen.getWorld().clearForces();
                break;
            case Input.Keys.S:
                parentScreen.getPlayer().setDownMove(false);
                parentScreen.getPlayer().getPlayerBody().setLinearVelocity(new Vector2(0, 0));
                parentScreen.getWorld().clearForces();

                break;
            case Input.Keys.SPACE:
                parentScreen.getPlayer().placeBomb();
                for (int i = 0; i < MyBomb.getBombList().size(); i++) {
                    if (MyBomb.getBombList().get(i) != null && !MyBomb.getBombList().get(i).isDrawn()) {
                        MyBomb.getBombList().get(i).draw();
                        MyBomb.getBombList().get(i).setDrawn(true);
                        MyBomb.getBombList().get(i).explode();
                    }
                }

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

        parentScreen.getCamera().zoom += amount / 25f;
        return true;
    }
}
