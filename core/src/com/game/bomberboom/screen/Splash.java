package com.game.bomberboom.screen;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.game.bomberboom.core.InputController;
import com.game.bomberboom.tween.SpriteAccessor;
import com.badlogic.gdx.Game;

/**
 * Created by toli9_000 on 2014/7/20.
 */

public class Splash implements Screen{

    private SpriteBatch batch;
    private Sprite splash;
    private TweenManager tweenManager;

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        tweenManager.update(delta);

        batch.begin();
        splash.draw(batch);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {

        batch = new SpriteBatch();
        tweenManager = new TweenManager();
        /**
         * skip sprite
         */
        Gdx.input.setInputProcessor(new InputController(){
			public boolean keyUp(int keycode){
				switch(keycode){
				case Keys.ESCAPE:
					 ((Game)Gdx.app.getApplicationListener()).setScreen(new GamePlay());
					
					break;
				}
				return true;
			}
        });
        
        Tween.registerAccessor(Sprite.class, new SpriteAccessor());
        Texture splashTexture = new Texture(Gdx.files.internal("img/splash/bomb_splash_screen.jpg"));
      //  Texture splashTexture = new Texture("img/bomb_splash_screen.jpg");
        splash = new Sprite(splashTexture);
        splash.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Tween.set(splash, SpriteAccessor.ALPHA).target(0).start(tweenManager);
        Tween.to(splash, SpriteAccessor.ALPHA, 2).target(1).repeatYoyo(1, 2).setCallback(
                new TweenCallback() {
                    @Override
                    public void onEvent(int i, BaseTween<?> baseTween) {
                        ((Game)Gdx.app.getApplicationListener()).setScreen(new GamePlay());
                    }
                }
        ).start(tweenManager);
        
       
    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        splash.getTexture().dispose();
    }
}
