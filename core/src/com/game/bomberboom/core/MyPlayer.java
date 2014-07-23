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
import com.game.bomberboom.model.Player;

public class MyPlayer extends Player{

	
	private boolean overSpeed;
	private float maxSpeed = 0.3f;
	private boolean leftMove, rightMove, upMove, downMove;
	private World world;
	private Vector2 movement;
	private Body playerBody;
	
	public enum Direction {UP, DOWN, LEFT, RIGHT};
	
	private Direction dir;
	private BodyDef playerDef;
	private FixtureDef fixtureDef;
	private Shape shape;
	
	
	public MyPlayer(int xPos, int yPos, float speed, World world, Direction dir) {
		super(xPos, yPos, speed);
		this.world = world;
		this.movement = new Vector2(0,0);
		this.dir = dir;
		playerDef = new BodyDef();
		fixtureDef = new FixtureDef();
		
		//default settings of player
		playerDef.type = BodyType.DynamicBody;
		playerDef.position.set(x, y);
		playerDef.fixedRotation = true;
		
		//shape
		shape = new PolygonShape();
		((PolygonShape) shape).setAsBox(0.5f, 0.5f);
		
		//fixture
	
		fixtureDef.density = 2f;
		fixtureDef.friction = .25f;
		fixtureDef.restitution = 0f;
	}

	protected void finalize ()  {
		shape.dispose();
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

	/**
	 * getter setter
	 * @return
	 */
	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public Vector2 getMovement() {
		return movement;
	}

	public void setMovement(Vector2 movement) {
		this.movement = movement;
	}

	public Body getBody() {
		return playerBody;
	}

	public void setBody(Body playerBody) {
		this.playerBody = playerBody;
	}

	public Body getPlayerBody() {
		return playerBody;
	}

	public void setPlayerBody(Body playerBody) {
		this.playerBody = playerBody;
	}

	public BodyDef getBodyDef() {
		return playerDef;
	}

	public void setBodyDef(BodyDef bodyDef) {
		this.playerDef = bodyDef;
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

	
	
	
	
	
	
	
	
	@Override
	public void draw() {
		
		//set up whatever the shape is
		fixtureDef.shape = shape;

		playerBody = world.createBody(playerDef);
		playerBody.createFixture(fixtureDef);
		playerBody.setUserData(new MyUserData("player", this, null));
		
	}
	
	public void move(){
		if(leftMove){
			
			Vector2 vel = playerBody.getLinearVelocity();
			float velChange = (-speed) - vel.x;  //move left: -speed on x
			float force = (float) (playerBody.getMass() * velChange / (1.0 /60.0));
			getBody().applyForce(new Vector2(force, 0), playerBody.getWorldCenter(), true);
		}
		if(rightMove){
			
			Vector2 vel = playerBody.getLinearVelocity();
			float velChange = (speed) - vel.x;  
			float force = (float) (playerBody.getMass() * velChange / (1.0 /60.0));
			getBody().applyForce(new Vector2(force, 0), playerBody.getWorldCenter(), true);
		}
		if(upMove){

			Vector2 vel = playerBody.getLinearVelocity();
			float velChange = (speed) - vel.y;  
			float force = (float) (playerBody.getMass() * velChange / (1.0 /60.0));
			getBody().applyForce(new Vector2(0, force), playerBody.getWorldCenter(), true);
		}
		if(downMove){

			Vector2 vel = playerBody.getLinearVelocity();
			float velChange = (-speed) - vel.y;  
			float force = (float) (playerBody.getMass() * velChange / (1.0 /60.0));
			getBody().applyForce(new Vector2(0, force), playerBody.getWorldCenter(), true);
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
			b.setY(playerBody.getPosition().y + 1 + b.getShape().getRadius());
			b.setOwner(this);
			b.setDrawn(false);
		
			MyBomb.getBombList().add(b);
			break;
			
		case DOWN:
			b = new MyBomb(0, 0, 3, 5000, 5000, world);
			b.setX(playerBody.getPosition().x);
			b.setY(playerBody.getPosition().y  - 1 - b.getShape().getRadius());
			b.setOwner(this);
			b.setDrawn(false);
	
			MyBomb.getBombList().add(b);
			break;
			
		case LEFT:
			b = new MyBomb(0, 0, 3, 5000, 5000, world);
			b.setY(playerBody.getPosition().y );
			b.setX(playerBody.getPosition().x - 1 - b.getShape().getRadius());
			b.setOwner(this);
			b.setDrawn(false);
	
			MyBomb.getBombList().add(b);
			break;
			
		case RIGHT:
			b = new MyBomb(0, 0, 3, 5000, 5000, world);
			b.setY(playerBody.getPosition().y );
			b.setX(playerBody.getPosition().x + 1 + b.getShape().getRadius());
			b.setOwner(this);
			b.setDrawn(false);
		
			MyBomb.getBombList().add(b);
			break;
			
		}
		
	}
	
	

}
