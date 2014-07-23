package com.game.bomberboom.core;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Segment;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.game.bomberboom.model.Crate;

public class MyCrate extends Crate{

	private FixtureDef fixtureDef;
	
	private BodyDef[][] brickDef;   //crate create by 2darrays of bricks
	private World world;
	private Shape shape;
	private float brickSize;
	
	public MyCrate(float x, float y, int size, float brickSize, World world) {
		super(x, y, size);
		brickDef = new BodyDef[size][size];
		this.world = world;
		this.brickSize = brickSize;
		this.fixtureDef = new FixtureDef();
		
		// default crate
		//shape
		shape = new PolygonShape();
		((PolygonShape) shape).setAsBox(brickSize, brickSize);
		
		//fixture
		
		fixtureDef.density = 10.0f;
		fixtureDef.friction = 0.6f;
		fixtureDef.restitution = 0;
		
		
		//put bricks around crate position to form a crate
		
	}

	public FixtureDef getFixtureDef() {
		return fixtureDef;
	}

	public void setFixtureDef(FixtureDef fixtureDef) {
		this.fixtureDef = fixtureDef;
	}

	public BodyDef[][] getBrickDef() {
		return brickDef;
	}

	public void setBrickDef(BodyDef[][] brickDef) {
		this.brickDef = brickDef;
	}

	public Shape getShape() {
		return shape;
	}

	public void setShape(Shape shape) {
		this.shape = shape;
	}

	public float getBrickSize() {
		return brickSize;
	}

	public void setBrickSize(float brickSize) {
		this.brickSize = brickSize;
	}

	@Override
	public void draw() {
		fixtureDef.shape = shape;
		float factor = 0f;
		for(int w = 0; w < size; w++){
			for(int h = 0; h < size; h++){
				//brickDef[w][h] = new BodyDef();
				//brickDef[w][h].type = BodyType.DynamicBody;
				MyBrick b = new MyBrick(x - ((brickSize * size)/2) + brickSize/2 + (w * (brickSize + factor)) , y - ((brickSize * size)/2) + brickSize/2 + (h * (brickSize + factor)),1, world);
				b.draw();
				//brickDef[w][h].position.set(x - ((brickSize * size)/2) + brickSize/2 + (w * (brickSize + factor)) , y - ((brickSize * size)/2) + brickSize/2 + (h * (brickSize + factor)) );
					
				//brickDef[w][h].linearDamping = 3.0f;
				//brickDef[w][h].angularDamping = 2.0f;
				
				//Body brickBody = world.createBody(brickDef[w][h]);
				//brickBody.createFixture(this.fixtureDef);
				//brickBody.setUserData(new MyUserData("brick", null, null));
				
			}
		}
		
		

	}
	
	
	protected void finalize ()  {
		shape.dispose();
    }

}
