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
import com.game.bomberboom.model.GameObject;


public class MyBomb extends Bomb{

	/**
	 * record bombs and explosions
	 */
	public static ArrayList<MyBomb> BombList = new ArrayList<MyBomb>();
	public static ArrayList<ParticleExplosion> Explosions = new ArrayList<ParticleExplosion>();

	/**
	 * box2d parameters
	 */
	public World world;
	private Body bombBody;
	private BodyDef bombDef;
	private FixtureDef bombFixtureDef;
	private Shape bombShape;
	
	
	 
	private boolean isDrawn;
	private MyPlayer owner;
	
	
	MyBomb(int x, int y, float time, float powerX, float powerY, World world) {
		super(x, y, time, powerX, powerY);
	
		GameObject.gameObjects.add(this);
		
		this.world = world;
		bombDef = new BodyDef();
		bombFixtureDef = new FixtureDef();
	
		//default setting of bomb
	
		bombDef.type = BodyType.DynamicBody;
		bombDef.position.set(x, y);
		
		//shape
		bombShape = new CircleShape();
		bombShape.setRadius(.50f);
		
		//fixture
		bombFixtureDef.density = 2.5f;
		bombFixtureDef.friction = .25f;
		bombFixtureDef.restitution = .75f;
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

	public FixtureDef getBombFixtureDef() {
		return bombFixtureDef;
	}

	public void setBombFixtureDef(FixtureDef bombFixtureDef) {
		this.bombFixtureDef = bombFixtureDef;
	}

	public Shape getBombShape() {
		return bombShape;
	}

	public void setBombShape(Shape bombShape) {
		this.bombShape = bombShape;
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
								
								
								world.destroyBody(getBombBody());  
								MyBomb.getBombList().remove(this);
								GameObject.gameObjects.remove(this);
								
			         }
			      });
			   }
			}).start();
		
	}

	@Override
	public void draw() {
		bombFixtureDef.shape = bombShape;
		
		bombBody = world.createBody(bombDef);
		bombBody.createFixture(bombFixtureDef);
		bombBody.setUserData(new MyUserData("bomb", this, null));
		
		
	}
	
	protected void finialize(){
		bombShape.dispose();
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}
}
