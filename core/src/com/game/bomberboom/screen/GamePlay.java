package com.game.bomberboom.screen;


import box2dLight.ConeLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.game.bomberboom.core.CollisionListener;
import com.game.bomberboom.core.InputController;
import com.game.bomberboom.core.MyBomb;
import com.game.bomberboom.core.MyBrick;
import com.game.bomberboom.core.MyCrate;
import com.game.bomberboom.core.MyPlayer;
import com.game.bomberboom.core.MyUserData;
import com.game.bomberboom.core.ParticleExplosion;
import com.game.bomberboom.core.MyPlayer.Direction;
import com.game.bomberboom.model.GameObject;

import java.util.ArrayList;


public class GamePlay implements Screen {

	private World world;
	private Box2DDebugRenderer debugRenderer;
	private OrthographicCamera camera;
	
	private SpriteBatch batch;
	private Array<Body> bodies = new Array<Body>();
	private final float TIMESTEP = 1 / 60f;
	private final int VELOCITYITERATIONS = 8;
	private final int POSITIONITERATIONS = 3;
	
	BitmapFont fps;
	
	RayHandler handler;

	CollisionListener cl;
	private MyPlayer player, player2;
	private MyBrick testBrick;
	
	private Sprite backgroundSprite;
	@Override
	public void render(float delta) {
		/**
		 * render background
		 */
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		/**
		 * render game objects
		 */
		for(int i = 0; i < GameObject.gameObjects.size(); i++){
			GameObject o = GameObject.gameObjects.get(i);
			o.update();
		}

		//render player
		player.move();
	
		
	
		/**
		 * search explosion and explode
		 */
		ArrayList<ParticleExplosion> explosions = MyBomb.getExplosions();
		for(int i = 0; i < explosions.size(); i++){
			if(explosions.get(i) != null && !explosions.get(i).isCompleted())
				explosions.get(i).update();
			else MyBomb.getExplosions().remove(i);
		}
		
		
		//render camera 
		camera.position.set(0, 0, 0);
		camera.update();
	
		
		/**
		 * draw sprite
		 */
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		 
	
		fps.draw(batch, Integer.toString(Gdx.graphics.getFramesPerSecond()), -30, 30);
	
	//	backgroundSprite.draw(batch);
		
		world.getBodies(bodies);
		for(Body body : bodies){
			if(body.getUserData() != null){
			Sprite sprite = ((MyUserData)body.getUserData()).getSprite();
			if( sprite != null){
				sprite.setPosition(body.getPosition().x - sprite.getWidth()/2, body.getPosition().y - sprite.getHeight()/2);
				sprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
				sprite.draw(batch);
			}
			}
		}
		batch.end();
		
		/**
		 * lights render
		 */
			handler.setCombinedMatrix(camera.combined);
			handler.updateAndRender();
			
	
	    //world render 
		world.step(TIMESTEP, VELOCITYITERATIONS, POSITIONITERATIONS);
		
		//debug render
		debugRenderer.render(this.world, camera.combined);
		
		
	}

	@Override
	public void resize(int width, int height) {
		camera.viewportWidth = width/10;
		camera.viewportHeight = height/10;
		
		

	}

	@Override
	public void show() {
		
		/**********************************************************
		 *                 environment setup                      *
		 **********************************************************/
		/**
		 * box2d world setup
		 */
		world = new World(new Vector2(0, 0), true);
		debugRenderer = new Box2DDebugRenderer();
		
	
		/**
		 * fps setup
		 */
		fps = new BitmapFont();
		fps.setScale(0.1f);
		
		/**
		 * sprite batch setup
		 */
		batch = new SpriteBatch();
		
		/**
		 * camera setup
		 */
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		
		/**
		 * collision listener setup
		 */
		 cl = new CollisionListener();
		 world.setContactListener(cl);
	
		  /**
		   * lights setup
		   */
		  handler = new RayHandler(world);
		  new ConeLight(handler, 1000, new Color(1,1,1,1), 130, -49.9f, -34.9f, 45, 45);
		  new ConeLight(handler, 1000, new Color(1,1,1,1), 130, 49.9f, 34.9f, 225, 45);
//			 new PointLight(handler, 5000, new Color(1,1,1,1), 1000 ,Gdx.graphics.getWidth()/2-500, Gdx.graphics.getHeight()/2 - 400);
		//  handler.useDiffuseLight(true);
	     handler.setAmbientLight(0.1f, 0.1f, 0.1f, 0.4f);
	     
/*	    Texture texture = new Texture(Gdx.files.internal("img/texture/grass1.jpg"));
	     texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	     backgroundSprite = new Sprite(texture);
	     backgroundSprite.setSize(100 , 70 );

	     backgroundSprite.setPosition(0 - 50, 0 - 35);*/
	
		
		  
		  
		  
		  /**********************************************************
		   *                 game objects creation and draw            *
		   **********************************************************/
		  
		  /**
		   * create player 
		   */
		  player = new MyPlayer(0, -10, 10, 1,world, Direction.LEFT);
	
		  player.draw();
		  
		  /**
		   * create brick
		   */
		
		  testBrick = new MyBrick(-20, 0, 4, world);
		  testBrick.draw();
		  
		/**
		 * create bunch of crate
		 */
		  float factor = 0f;
		  float crateSize = 4;
		  float scaleSize = 5;
		  float x = 10;
		  float y = 10;
		  
			for(int w = 0; w < scaleSize; w++){
				for(int h = 0; h < scaleSize; h++){
					MyCrate c = new MyCrate(x - ((crateSize * scaleSize)/2) + crateSize/2 + (w * (crateSize + factor)) , y - ((crateSize * scaleSize)/2) + crateSize/2 + (h * (crateSize + factor)), crateSize, world);
					c.draw();
				}
			}
	//	brick = new MyBrick(0,0,1,world);
	//	brick.draw();
	//	MyCrate crate1 = new MyCrate(0, 0, 4 ,world);
	//	crate1.draw();
		
	//	MyCrate crate2 = new MyCrate(-10, -10, 8, 0.2f,world);
	//	crate2.draw();
		
	//	MyCrate crate3 = new MyCrate(10, -10, 10, 0.2f,world);
	//	crate3.draw();
		
	//	MyCrate crate4 = new MyCrate(-10, 10, 7, 0.2f,world);
	//	crate4.draw();
		//player2.draw();
		

	
		/**
		 *  create walls      -----------need encapsulate
		 */
		/**
		 * up wall
		 */
		//body 
		BodyDef upWallDef = new BodyDef();
		upWallDef.type = BodyType.StaticBody;
		upWallDef.position.set(0, 35);
		
		//shape
		ChainShape upWallShape = new ChainShape();
		upWallShape.createChain(new Vector2[]{new Vector2(-50,0), new Vector2(50,0)});
		
		//fixture
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = upWallShape;
		fixtureDef.friction = .5f;
		fixtureDef.restitution = 0;
		
		Body upWall = world.createBody(upWallDef);
		upWall.createFixture(fixtureDef);
		upWall.setUserData(new MyUserData("upWall", null, null));
		
		upWallShape.dispose();
		/**
		 * left wall
		 */
		BodyDef leftWallDef = new BodyDef();
		leftWallDef.type = BodyType.StaticBody;
		leftWallDef.position.set(-50, 0);
		
		//shape
		ChainShape leftWallShape = new ChainShape();
		leftWallShape.createChain(new Vector2[]{new Vector2(0, 50), new Vector2(0, -50)});
		
		//fixture
		fixtureDef.shape = leftWallShape;
		fixtureDef.friction = .5f;
		fixtureDef.restitution = 0;
		
		Body leftWall = world.createBody(leftWallDef);
		leftWall.createFixture(fixtureDef);
		leftWall.setUserData(new MyUserData("leftWall", null, null));
		
		leftWallShape.dispose();
		
		/**
		 * down wall
		 */
		BodyDef downWallDef = new BodyDef();
		downWallDef.type = BodyType.StaticBody;
		downWallDef.position.set(0, -35);
		
		//shape
		ChainShape downWallShape = new ChainShape();
		downWallShape.createChain(new Vector2[]{new Vector2(-50, 0), new Vector2(50, 0)});
		
		//fixture
		fixtureDef.shape = downWallShape;
		fixtureDef.friction = .5f;
		fixtureDef.restitution = 0;
		
		Body downWall = world.createBody(downWallDef);
		downWall.createFixture(fixtureDef);
		downWall.setUserData(new MyUserData("downWall", null, null));
		
		downWallShape.dispose();
		
		/**
		 * right wall
		 */
		BodyDef rightWallDef = new BodyDef();
		rightWallDef.type = BodyType.StaticBody;
		rightWallDef.position.set(50, 0);
		
		//shape
		ChainShape rightWallShape = new ChainShape();
		rightWallShape.createChain(new Vector2[]{new Vector2(0, 50), new Vector2(0, -50)});
		
		//fixture
		fixtureDef.shape = rightWallShape;
		fixtureDef.friction = .5f;
		fixtureDef.restitution = 0;
		
		Body rightWall = world.createBody(rightWallDef);
		rightWall.createFixture(fixtureDef);
		
		rightWall.setUserData(new MyUserData("rightWall", null, null));
		
	    rightWallShape.dispose();
		
	    
	    /**********************************************************
		 *                 input listener                         *
		 **********************************************************/
		Gdx.input.setInputProcessor(new InputController(){
			public boolean keyDown(int keycode){
				switch(keycode){
				case Keys.ESCAPE:
					System.exit(1);
					break;
				case Keys.W:
					player.setUpMove(true);
					player.setDir(Direction.UP);
					//player.moveUp();
					break;
				case Keys.A:
					player.setLeftMove(true);
					player.setDir(Direction.LEFT);
					//player.moveLeft();
					break;
				case Keys.S:
					player.setDownMove(true);
					player.setDir(Direction.DOWN);
					//player.moveDown();
					break;
				case Keys.D:
					player.setRightMove(true);
					player.setDir(Direction.RIGHT);
					//player.moveRight();
					break;
					
				case Keys.F:
					//testBrick.boom();
					break;
				
				}
				return true;
			}
			
			public boolean keyUp(int keycode){
				switch(keycode){
				case Keys.ESCAPE:
					System.exit(1);
					break;
				case Keys.W:
					player.setUpMove(false);
					player.getPlayerBody().setLinearVelocity(new Vector2(0,0));
					world.clearForces();
					break;
				case Keys.D:
					player.setRightMove(false);
					player.getPlayerBody().setLinearVelocity(new Vector2(0,0));
					world.clearForces();
					break;
				case Keys.A:
					player.setLeftMove(false);
					player.getPlayerBody().setLinearVelocity(new Vector2(0,0));
					world.clearForces();
					break;
				case Keys.S:
					player.setDownMove(false);
					player.getPlayerBody().setLinearVelocity(new Vector2(0,0));
					world.clearForces();
				
					break;
				case Keys.SPACE:
					player.placeBomb();
					for(int i = 0; i < MyBomb.getBombList().size(); i++){
						if(MyBomb.getBombList().get(i) != null && !MyBomb.getBombList().get(i).isDrawn()){
							MyBomb.getBombList().get(i).draw();
							MyBomb.getBombList().get(i).setDrawn(true);
							MyBomb.getBombList().get(i).explode();
						}
					}
					
					break;
				}
				return true;
			}
		
			public boolean scrolled(int amount){
				camera.zoom += amount / 25f;
				return true;
			}
		});
	
	    
	  

	}

	@Override
	public void hide() {
		dispose();

	}

	@Override
	public void pause() {
		

	}

	@Override
	public void resume() {
		

	}

	@Override
	public void dispose() {
		world.dispose();
		debugRenderer.dispose();

	}

}
