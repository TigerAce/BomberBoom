package com.game.bomberboom.core;

import java.util.ArrayList;




import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.game.bomberboom.model.Bomb;


public class MyBomb extends Bomb{

	public static ArrayList<MyBomb> BombList = new ArrayList<MyBomb>();
	public static ArrayList<ParticleExplosion> Explosions = new ArrayList<ParticleExplosion>();
	
	private boolean isDrawn;
	private MyPlayer owner;
	private World world;
	private Body bombBody;
	
	
	private BodyDef bombDef;
	private FixtureDef fixtureDef;
	private Shape shape;
	
	MyBomb(int x, int y, float time, float powerX, float powerY, World world) {
		super(x, y, time, powerX, powerY);
	
		this.world = world;
		bombDef = new BodyDef();
		fixtureDef = new FixtureDef();
	
		//default setting of bomb
	
		bombDef.type = BodyType.DynamicBody;
		bombDef.position.set(x, y);
		
		//shape
		shape = new CircleShape();
		shape.setRadius(.50f);
		
		//fixture
		fixtureDef.density = 2.5f;
		fixtureDef.friction = .25f;
		fixtureDef.restitution = .75f;
	}

	public static ArrayList<ParticleExplosion> getExplosions() {
		return Explosions;
	}

	public static void setExplosions(ArrayList<ParticleExplosion> explosions) {
		Explosions = explosions;
	}

	@Override
	public void setX(float x){
		this.x = x;
		bombDef.position.set(x, y);
	}
	@Override
	public void setY(float y){
		this.y = y;
		bombDef.position.set(x, y);
	}
	public boolean isDrawn() {
		return isDrawn;
	}

	public void setDrawn(boolean isDrawn) {
		this.isDrawn = isDrawn;
	}

	public MyPlayer getOwner() {
		return owner;
	}

	public void setOwner(MyPlayer owner) {
		this.owner = owner;
	}

	public Body getBody(){
		return this.bombBody;
	}
	/**
	 * getter & setter
	 * @return
	 */
	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public Body getBombBody() {
		return bombBody;
	}

	public void setBombBody(Body bombBody) {
		this.bombBody = bombBody;
	}

	public BodyDef getBombDef() {
		return bombDef;
	}

	public void setBombDef(BodyDef bombDef) {
		this.bombDef = bombDef;
	}

	public FixtureDef getFixtureDef() {
		return fixtureDef;
	}

	public void setFixtureDef(FixtureDef fixtureDef) {
		this.fixtureDef = fixtureDef;
	}

	public Shape getShape() {
		return shape;
	}

	public void setShape(Shape shape) {
		this.shape = shape;
	}

	public static ArrayList<MyBomb> getBombList() {
		return BombList;
	}

	public static void setBombList(ArrayList<MyBomb> bombList) {
		BombList = bombList;
	}

	
	/**
	 * threading
	 */
	@Override
	public void explode(){
	
			new Thread(new Runnable() {
			
			   @Override
			   public void run() {
		
				   try {
						Thread.sleep(3000);
		
					} catch (InterruptedException e) {
					
						e.printStackTrace();
					}
		//	 post a Runnable to the rendering thread that processes the result
		      Gdx.app.postRunnable(new Runnable() {
			         @Override
			         public void run() {
			        	 
								ParticleExplosion e = new ParticleExplosion(bombBody.getPosition().x, bombBody.getPosition().y, world);
								MyBomb.getExplosions().add(e);
								
								
								world.destroyBody(getBody());  
								MyBomb.getBombList().remove(this);
								
			         }
			      });
			   }
			}).start();
		
	}

	@Override
	public void draw() {
		fixtureDef.shape = shape;
		
		bombBody = world.createBody(bombDef);
		bombBody.createFixture(fixtureDef);
		bombBody.setUserData(new MyUserData("bomb", this, null));
		
		shape.dispose();
		
	}
}
