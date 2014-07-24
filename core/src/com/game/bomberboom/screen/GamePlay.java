package com.game.bomberboom.screen;


import box2dLight.ConeLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
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
import com.game.bomberboom.InputSource.GameScreenController;
import com.game.bomberboom.core.*;
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

    private BitmapFont fps;
    private RayHandler rayHandler;
    private CollisionListener collisionListener;
    private MyPlayer player;



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
        for (int i = 0; i < GameObject.gameObjects.size(); i++) {
            GameObject o = GameObject.gameObjects.get(i);
            o.update();
        }

        //render player
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


        //render camera
        camera.position.set(0, 0, 0);
        camera.update();


        /**
         * draw sprite
         */
        batch.setProjectionMatrix(camera.combined);
        batch.begin();


        fps.draw(batch, Integer.toString(Gdx.graphics.getFramesPerSecond()), -30, 30);

        world.getBodies(bodies);
        for (Body body : bodies) {
            if (body.getUserData() != null) {
                Sprite sprite = ((MyUserData) body.getUserData()).getSprite();
                if (sprite != null) {
                    sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2, body.getPosition().y - sprite.getHeight() / 2);
                    sprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
                    sprite.draw(batch);
                }
            }
        }
        batch.end();

        /**
         * lights render
         */
        rayHandler.setCombinedMatrix(camera.combined);
        rayHandler.updateAndRender();


        //world render
        world.step(TIMESTEP, VELOCITYITERATIONS, POSITIONITERATIONS);

        //debug render
        debugRenderer.render(this.world, camera.combined);


    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width / 10;
        camera.viewportHeight = height / 10;


    }

    @Override
    public void show() {

        /**********************************************************
         *                 Environment Setup                      *
         **********************************************************/
        /**
         * box2d world setup
         */
        world = new World(new Vector2(0, 0), true);
        debugRenderer = new Box2DDebugRenderer();


        /**
         * Setup FPS display using a BitmapFont
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
        collisionListener = new CollisionListener();
        world.setContactListener(collisionListener);

        /**
         * lights setup
         */
        rayHandler = new RayHandler(world);
        new ConeLight(rayHandler, 1000, new Color(1, 1, 1, 1), 130, -49.9f, -34.9f, 45, 45);
        new ConeLight(rayHandler, 1000, new Color(1, 1, 1, 1), 130, 49.9f, 34.9f, 225, 45);
        rayHandler.setAmbientLight(0.1f, 0.1f, 0.1f, 0.4f);

        /**********************************************************
         *                 game objects creation and draw            *
         **********************************************************/

        /**
         * create player
         */
        player = new MyPlayer(0, -10, 10, 1, world, Direction.LEFT);
        player.draw();

        /**
         * create brick
         */
        testBrick = new MyBrick(-20, 0, 4, world);
        testBrick.draw();

        /**
         * create a bunch of crates
         */
        float factor = 0f;
        float crateSize = 4;
        float scaleSize = 5;
        float x = 10;
        float y = 10;

        for (int w = 0; w < scaleSize; w++) {
            for (int h = 0; h < scaleSize; h++) {
                MyCrate c = new MyCrate(x - ((crateSize * scaleSize) / 2) + crateSize / 2 + (w * (crateSize + factor)), y - ((crateSize * scaleSize) / 2) + crateSize / 2 + (h * (crateSize + factor)), crateSize, world);
                c.draw();
            }
        }

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
        Gdx.input.setInputProcessor(new GameScreenController(this));
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



    /**
     * Stupi Getters and Setters
     */

    public OrthographicCamera getCamera() {
        return camera;
    }

    public void setCamera(OrthographicCamera camera) {
        this.camera = camera;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public MyPlayer getPlayer() {
        return player;
    }

    public void setPlayer(MyPlayer player) {
        this.player = player;
    }
}
