package com.game.bomberboom.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.game.bomberboom.utils.FragileObject;
import com.game.bomberboom.utils.FragilePoly;

public class TestScreen implements Screen, InputProcessor{

	FragilePoly fragilePoly;
	Array<FragileObject> fragileArray;

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		for (FragileObject obj : fragileArray) {
			obj.draw();
		}
		//fragilePoly.draw();
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		Array<Vector2> vertices = new Array<Vector2>();
		Array<Vector2> textCoord = new Array<Vector2>();
		
		vertices.add(new Vector2(-0.5f, 0.5f));
		vertices.add(new Vector2(-0.5f, -0.5f));
		vertices.add(new Vector2(0.5f, -0.5f));
		vertices.add(new Vector2(0.5f, 0.5f));
		vertices.add(new Vector2(-0.5f, 0.5f));
		
		textCoord.add(new Vector2(0, 0));
		textCoord.add(new Vector2(0, 1));
		textCoord.add(new Vector2(1, 1));
		textCoord.add(new Vector2(1, 0));
		textCoord.add(new Vector2(0, 0));
		
		fragileArray = new Array<FragileObject>();
		
		fragilePoly = new FragilePoly(vertices, textCoord, null, 0, 0);
		
		fragileArray.add(fragilePoly);
		
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		if (fragileArray.contains(fragilePoly, true)) {
			fragileArray.removeValue(fragilePoly, true);
			fragileArray = fragilePoly.breakDown();
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}

