package com.game.bomberboom.core;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.game.bomberboom.model.GameObject;
import com.game.bomberboom.model.Player;

public class MyPlayer extends Player{

	
	/**
	 * box2d parameters
	 */
	private World world;
	private BodyDef playerDef;
	private FixtureDef playerFixtureDef;
	private Shape playerShape;
	private Body playerBody;
	
	
	private boolean leftMove, rightMove, upMove, downMove;
	public enum Direction {UP, DOWN, LEFT, RIGHT};	
	private Direction dir;
	private float size;
	
	
	public MyPlayer(int xPos, int yPos, float speed, float size, World world, Direction dir) {
		super(xPos, yPos, speed);
		
		GameObject.gameObjects.add(this);
		
		this.world = world;
		this.size = size;
		this.dir = dir;
		
		playerDef = new BodyDef();
		playerFixtureDef = new FixtureDef();
		
		//default settings of player
		playerDef.type = BodyType.DynamicBody;
		playerDef.position.set(x, y);
		playerDef.fixedRotation = true;
		
		//shape
		playerShape = new PolygonShape();
		((PolygonShape) playerShape).setAsBox(size / 2, size / 2);
		
		//fixture
	
		playerFixtureDef.density = 2f;
		playerFixtureDef.friction = .25f;
		playerFixtureDef.restitution = 0f;
	}

	
	
	public Direction getDir() {
		return dir;
	}

	public void setDir(Direction dir) {
		this.dir = dir;
	}

	public boolean isLeftMove() {
		return leftMove;
	}

	public void setLeftMove(boolean leftMove) {
		this.leftMove = leftMove;
	}

	public boolean isRightMove() {
		return rightMove;
	}

	public void setRightMove(boolean rightMove) {
		this.rightMove = rightMove;
	}

	public boolean isUpMove() {
		return upMove;
	}

	public void setUpMove(boolean upMove) {
		this.upMove = upMove;
	}

	public boolean isDownMove() {
		return downMove;
	}

	public void setDownMove(boolean downMove) {
		this.downMove = downMove;
	}


	public BodyDef getPlayerDef() {
		return playerDef;
	}



	public void setPlayerDef(BodyDef playerDef) {
		this.playerDef = playerDef;
	}



	public FixtureDef getPlayerFixtureDef() {
		return playerFixtureDef;
	}



	public void setPlayerFixtureDef(FixtureDef playerFixtureDef) {
		this.playerFixtureDef = playerFixtureDef;
	}



	public Shape getPlayerShape() {
		return playerShape;
	}



	public void setPlayerShape(Shape playerShape) {
		this.playerShape = playerShape;
	}



	public Body getPlayerBody() {
		return playerBody;
	}



	public void setPlayerBody(Body playerBody) {
		this.playerBody = playerBody;
	}



	public float getSize() {
		return size;
	}



	public void setSize(float size) {
		this.size = size;
	}



	@Override
	public void draw() {
		
		//set up whatever the shape is
		playerFixtureDef.shape = playerShape;

		playerBody = world.createBody(playerDef);
		playerBody.createFixture(playerFixtureDef);
		playerBody.setUserData(new MyUserData("player", this, null));
		
	}
	
	public void move(){
		if(leftMove){
			
			Vector2 vel = playerBody.getLinearVelocity();
			float velChange = (-speed) - vel.x;  //move left: -speed on x
			float force = (float) (playerBody.getMass() * velChange / (1.0 /60.0));
			getPlayerBody().applyForce(new Vector2(force, 0), playerBody.getWorldCenter(), true);
		}
		if(rightMove){
			
			Vector2 vel = playerBody.getLinearVelocity();
			float velChange = (speed) - vel.x;  
			float force = (float) (playerBody.getMass() * velChange / (1.0 /60.0));
			getPlayerBody().applyForce(new Vector2(force, 0), playerBody.getWorldCenter(), true);
		}
		if(upMove){

			Vector2 vel = playerBody.getLinearVelocity();
			float velChange = (speed) - vel.y;  
			float force = (float) (playerBody.getMass() * velChange / (1.0 /60.0));
			getPlayerBody().applyForce(new Vector2(0, force), playerBody.getWorldCenter(), true);
		}
		if(downMove){

			Vector2 vel = playerBody.getLinearVelocity();
			float velChange = (-speed) - vel.y;  
			float force = (float) (playerBody.getMass() * velChange / (1.0 /60.0));
			getPlayerBody().applyForce(new Vector2(0, force), playerBody.getWorldCenter(), true);
		}
	}

/*	public void block(){
		switch(dir){
		case UP:
			setUpMove(false);
			break;
		case DOWN:
			setDownMove(false);
			break;
		case LEFT:
			setLeftMove(false);
			break;
		case RIGHT:
			setRightMove(false);
			break;
		}
	}
	public void unblock(){
		switch(dir){
		case UP:
			setUpMove(true);
			break;
		case DOWN:
			setDownMove(true);
			break;
		case LEFT:
			setLeftMove(true);
			break;
		case RIGHT:
			setRightMove(true);
			break;
		}
	}*/

	@Override
	public void placeBomb() {
		MyBomb b;
		switch(dir){
		case UP:
			b = new MyBomb(0, 0, 3, 5000, 5000, world);
			b.setX(playerBody.getPosition().x);
			b.setY(playerBody.getPosition().y + 1 + b.getBombShape().getRadius());
			b.setOwner(this);
			b.setDrawn(false);
		
			MyBomb.getBombList().add(b);
			break;
			
		case DOWN:
			b = new MyBomb(0, 0, 3, 5000, 5000, world);
			b.setX(playerBody.getPosition().x);
			b.setY(playerBody.getPosition().y  - 1 - b.getBombShape().getRadius());
			b.setOwner(this);
			b.setDrawn(false);
	
			MyBomb.getBombList().add(b);
			break;
			
		case LEFT:
			b = new MyBomb(0, 0, 3, 5000, 5000, world);
			b.setY(playerBody.getPosition().y );
			b.setX(playerBody.getPosition().x - 1 - b.getBombShape().getRadius());
			b.setOwner(this);
			b.setDrawn(false);
	
			MyBomb.getBombList().add(b);
			break;
			
		case RIGHT:
			b = new MyBomb(0, 0, 3, 5000, 5000, world);
			b.setY(playerBody.getPosition().y );
			b.setX(playerBody.getPosition().x + 1 + b.getBombShape().getRadius());
			b.setOwner(this);
			b.setDrawn(false);
		
			MyBomb.getBombList().add(b);
			break;
			
		}
		
	}
	
	protected void finalize ()  {
		playerShape.dispose();
    }



	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}
	

}
