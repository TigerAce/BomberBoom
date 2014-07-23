package com.game.bomberboom.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
import com.game.bomberboom.model.Barrier;

public class MyCrate extends Barrier{

	/**
	 * box2d parameters
	 */
	private FixtureDef crateFixtureDef;
	private Body crateBody;
	private BodyDef crateDef;
	private World world;
	private Shape crateShape;
	
	private Sprite crateSprite;
	
	public MyCrate(float x, float y, float size, World world) {
		super(x, y, size);
		this.world = world;
		this.crateFixtureDef = new FixtureDef();
		crateDef = new BodyDef();
		// default crate
		
		crateDef.type = BodyType.DynamicBody;
		crateDef.position.set(x, y);
		
		//shape
		crateShape = new PolygonShape();
		((PolygonShape) crateShape).setAsBox(size/2, size/2);
		
		//fixture
		
		crateFixtureDef.density = 10.0f;
		crateFixtureDef.friction = 0.6f;
		crateFixtureDef.restitution = 0;
		
		//sprite
		crateSprite = new Sprite(new Texture(Gdx.files.internal("img/texture/crate4.jpg")));
		crateSprite.setSize(size, size);
		crateSprite.setOrigin(crateSprite.getWidth()/2, crateSprite.getHeight()/2);
		
		
	}

	public Sprite getCrateSprite() {
		return crateSprite;
	}

	public void setCrateSprite(Sprite crateSprite) {
		this.crateSprite = crateSprite;
	}

	public FixtureDef getCrateFixtureDef() {
		return crateFixtureDef;
	}

	public void setCrateFixtureDef(FixtureDef crateFixtureDef) {
		this.crateFixtureDef = crateFixtureDef;
	}

	public Body getCrateBody() {
		return crateBody;
	}

	public void setCrateBody(Body crateBody) {
		this.crateBody = crateBody;
	}

	public BodyDef getCrateDef() {
		return crateDef;
	}

	public void setCrateDef(BodyDef crateDef) {
		this.crateDef = crateDef;
	}

	public Shape getCrateShape() {
		return crateShape;
	}

	public void setCrateShape(Shape crateShape) {
		this.crateShape = crateShape;
	}

	@Override
	public void draw() {
		crateFixtureDef.shape = crateShape;
		
		
		crateBody = world.createBody(crateDef);
		crateBody.createFixture(crateFixtureDef);
		crateBody.setLinearDamping(0.5f);
		crateBody.setAngularDamping(0.5f);
		crateBody.setUserData(new MyUserData("crate", null, crateSprite));
		/*float factor = 0f;
		for(int w = 0; w < size; w++){
			for(int h = 0; h < size; h++){
				//brickDef[w][h] = new BodyDef();
				//brickDef[w][h].type = BodyType.DynamicBody;
				MyBrick b = new MyBrick(x - ((brickSize * size)/2) + brickSize/2 + (w * (brickSize + factor)) , y - ((brickSize * size)/2) + brickSize/2 + (h * (brickSize + factor)),4, world);
				b.draw();
				//brickDef[w][h].position.set(x - ((brickSize * size)/2) + brickSize/2 + (w * (brickSize + factor)) , y - ((brickSize * size)/2) + brickSize/2 + (h * (brickSize + factor)) );
					
				//brickDef[w][h].linearDamping = 3.0f;
				//brickDef[w][h].angularDamping = 2.0f;
				
				//Body brickBody = world.createBody(brickDef[w][h]);
				//brickBody.createFixture(this.fixtureDef);
				//brickBody.setUserData(new MyUserData("brick", null, null));
				
			}*/
	}
		
		

	
	
	
	protected void finalize ()  {
		crateShape.dispose();
    }

}
