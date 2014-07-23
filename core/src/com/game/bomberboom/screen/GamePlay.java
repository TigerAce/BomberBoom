package com.game.bomberboom.screen;


import box2dLight.ConeLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.game.bomberboom.core.*;
import com.game.bomberboom.core.MyPlayer.Direction;

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

    private BitmapFont fps;

    private RayHandler rayHandler;
    private CollisionListener collisionListener;
    private MyPlayer player;

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //render world
        world.step(TIMESTEP, VELOCITYITERATIONS, POSITIONITERATIONS);

        //move player
        player.move();

        /**
         * search explosion and explode
         */
        ArrayList<ParticleExplosion> explosions = MyBomb.getExplosions();
        for (int i = 0; i < explosions.size(); i++) {
            if (explosions.get(i) != null && !explosions.get(i).isCompleted())
                explosions.get(i).update();
            else MyBomb.getExplosions().remove(i);
        }

        //if(e.isCompleted())
        camera.position.set(0, 0, 0);
        camera.update();

        /**
         * draw sprites
         */
        batch.setProjectionMatrix(camera.combined);
        batch.begin();


        fps.draw(batch, Integer.toString(Gdx.graphics.getFramesPerSecond()), -30, 30);


        world.getBodies(bodies);
        for (Body body : bodies) {
            Sprite sprite = ((MyUserData) body.getUserData()).getSprite();
            if (sprite != null) {
                sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2, body.getPosition().y - sprite.getHeight() / 2);
                sprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
                sprite.draw(batch);
            }
        }
        batch.end();

        /**
         * lights render
         */
        rayHandler.setCombinedMatrix(camera.combined);
        rayHandler.updateAndRender();


        debugRenderer.render(this.world, camera.combined);


    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width / 10;
        camera.viewportHeight = height / 10;


    }

    @Override
    public void show() {

        // Set the current game state to BomberBoom.SPLASH_SCREEN_STATE.GAME_PLAY_STATE
        ((BomberBoom) Gdx.app.getApplicationListener()).setGameState(BomberBoom.GAME_PLAY_STATE);

        // Create a World with no gravity
        world = new World(new Vector2(0, 0), true);
        debugRenderer = new Box2DDebugRenderer();

        // Set up a BitmapFont to display FPS
        fps = new BitmapFont();
        fps.setScale(0.1f);

        batch = new SpriteBatch();

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Set up CollisionListener to the world
        collisionListener = new CollisionListener();
        world.setContactListener(collisionListener);

        /**
         * Set up lighting system
         */
        rayHandler = new RayHandler(world);
        new ConeLight(rayHandler, 1000, new Color(1, 1, 1, 1), 700, -30, -30, 60, 60);
        //	 new PointLight(rayHandler, 5000, new Color(1,1,1,1), 1000 ,Gdx.graphics.getWidth()/2-500, Gdx.graphics.getHeight()/2 - 400);

        /**
         * Set up characters
         */
        player = new MyPlayer(0, -10, 10, world, Direction.LEFT);
        player.draw();

        /**
         * Set up other environments, craft, bricks, walls etc.
         */
        MyCrate crate1 = new MyCrate(0, 0, 5, 4, world);
        crate1.draw();

        /**
         * Top wall
         */
        //body
        BodyDef upWallDef = new BodyDef();
        upWallDef.type = BodyType.StaticBody;
        upWallDef.position.set(0, 35);

        //shape
        ChainShape upWallShape = new ChainShape();
        upWallShape.createChain(new Vector2[]{new Vector2(-50, 0), new Vector2(50, 0)});

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
         * Left wall
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

        /**
         * Set up in-game key bindings
         */
        Gdx.input.setInputProcessor(new InputController() {
            @Override
            public boolean keyDown(int keycode) {
                switch (keycode) {
                    case Input.Keys.W:
                        player.setUpMove(true);
                        player.setDir(Direction.UP);
                        break;
                    case Input.Keys.A:
                        player.setLeftMove(true);
                        player.setDir(Direction.LEFT);
                        break;
                    case Input.Keys.S:
                        player.setDownMove(true);
                        player.setDir(Direction.DOWN);
                        break;
                    case Input.Keys.D:
                        player.setRightMove(true);
                        player.setDir(Direction.RIGHT);
                        break;

                    case Input.Keys.F:
                        break;

                }
                return true;
            }
            @Override
            public boolean keyUp(int keycode) {
                switch (keycode) {
                    case Input.Keys.W:
                        player.setUpMove(false);
                        player.getBody().setLinearVelocity(new Vector2(0, 0));
                        world.clearForces();
                        break;
                    case Input.Keys.D:
                        player.setRightMove(false);
                        player.getBody().setLinearVelocity(new Vector2(0, 0));
                        world.clearForces();
                        break;
                    case Input.Keys.A:
                        player.setLeftMove(false);
                        player.getBody().setLinearVelocity(new Vector2(0, 0));
                        world.clearForces();
                        break;
                    case Input.Keys.S:
                        player.setDownMove(false);
                        player.getBody().setLinearVelocity(new Vector2(0, 0));
                        world.clearForces();

                        break;
                    case Input.Keys.SPACE:
                        player.placeBomb();
                        /**
                         * Shouldn't this move to the main game loop and get rid of threading?
                         */
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
            public boolean scrolled(int amount) {
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
