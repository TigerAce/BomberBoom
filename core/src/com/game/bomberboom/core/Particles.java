package com.game.bomberboom.core;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Particles {

	private float x;
	private float y;
	private Body body;
	private int lifespan = 1000;
	private ParticleExplosion parent;
	
	private World world;
	private Vector2 dirVector;
	private FixtureDef fixtureDef;
	private BodyDef bodyDef;
	
	
	
	public Particles(float x, float y, float angle, float density, float blastPower,
			ParticleExplosion parent, World world) {
		dirVector = new Vector2((float)Math.sin(angle), (float)Math.cos(angle));
		fixtureDef = new FixtureDef();
		bodyDef = new BodyDef();
		
		this.world = world;
		this.x = x;
		this.y = y;
		this.parent = parent;
	
		fixtureDef.density = density;
		fixtureDef.friction = 0;
		fixtureDef.restitution = 0.99f;
		fixtureDef.filter.groupIndex = -1;
	
		CircleShape shape = new CircleShape();
		shape.setRadius(0.1f);
		
		fixtureDef.shape = shape;
		
		bodyDef.type = BodyType.DynamicBody;
		
		//really need?
		bodyDef.position.x = x;
		bodyDef.position.y = y;
		
		bodyDef.fixedRotation = true;
		
		bodyDef.bullet = true;
		
		bodyDef.linearDamping = 3.5f;
		
		
		body = world.createBody(bodyDef);
		body.createFixture(fixtureDef);
		
		
		body.setLinearVelocity(blastPower * dirVector.x,
							 blastPower * dirVector.y);
		
		body.setUserData(new MyUserData("particles", this, null));
		
	}
	
	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}

	public void update(){
		this.lifespan -= 15;
		
		if(this.lifespan <= 0){
			
			kill(); 
			return;
		}
		
		this.x = this.body.getPosition().x ;
		this.y = this.body.getPosition().y ;
	}
	
	public void kill(){
		parent.removeParticles(this);
	}

}
