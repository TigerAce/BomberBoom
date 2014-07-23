package com.game.bomberboom.core;

import java.util.ArrayList;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;

public class ParticleExplosion {
	private ArrayList<Particles> particles;
	private boolean completed;
	private World world;
	
	int numParticles = 64;
	float blastPower = 100.0f;
	float density = (float) (10000.0 / numParticles);
	float randomStartAngle =  ((float)Math.random() * 90);
	
	ParticleExplosion(float x, float y, World world){
		particles = new ArrayList<Particles>();
		
		this.world = world;
		
		for(int i = 0; i < numParticles; i++){
			float angle = ((((float)i / numParticles) * 360) + randomStartAngle) * MathUtils.degreesToRadians;
			Particles p = new Particles(x, y, angle, density, blastPower, this, world);
			particles.add(p);
		}
		
	}
	
	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public void removeParticles(Particles p){
		world.destroyBody(p.getBody());
		particles.remove(p);
	}
	
	public void update(){
		if(particles.isEmpty()){
			complete();
			return;
		}
		for(int i = 0; i < particles.size(); i++){
			particles.get(i).update();
		}
	}
	
	public void complete(){
		completed = true;
	}
}
