package com.game.bomberboom.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
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
import com.badlogic.gdx.utils.Array;
import com.game.bomberboom.interfaces.Destructible;
import com.game.bomberboom.model.Barrier;
import com.game.bomberboom.model.GameObject;
import com.game.bomberboom.utils.FragileObject;
import com.game.bomberboom.utils.FragilePoly;

public class MyCrate extends Barrier implements Destructible{

	/**
	 * box2d parameters
	 */
	private FixtureDef crateFixtureDef;
	private Body crateBody;
	private BodyDef crateDef;
	private World world;
	private Shape crateShape;
	
	private float life = 500;
	
	private Sprite crateSprite;
	

	public MyCrate(float x, float y, float size, World world) {
		super(x, y, size);
		
		GameObject.gameObjects.add(this);
		
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

	public float getLife() {
		return life;
	}

	public void setLife(float life) {
		this.life = life;
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
		crateBody.setUserData(new MyUserData("crate", this, crateSprite));
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

	@Override
	public void update() {
		if(this.life <= 0){
			GameObject.gameObjects.remove(this);
			this.world.destroyBody(this.getCrateBody());
		}
		
	}


	@Override
	public void fragInit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fragment() {
		
		Vector2 breakPoint = new Vector2();
		Vector2 breakPointTextCoord = new Vector2();
		float randx = MathUtils.random(0.f, 1.f);
		float randy = MathUtils.random(0.f, 1.f);
		breakPoint.x = (Interpolation.linear).apply(-0.5f, 0.5f, randx);
		breakPoint.y = (Interpolation.linear).apply(0.5f, -0.5f, randy);
		breakPointTextCoord.set(randx, randy);
		
		//shape vertices
		Array<Vector2> vertices = new Array<Vector2>();
		
		Array<Vector2> textCoord = new Array<Vector2>();
		
		textCoord.add(new Vector2(0, 0));
		textCoord.add(new Vector2(0, 1));
		textCoord.add(new Vector2(1, 1));
		textCoord.add(new Vector2(1, 0));
		textCoord.add(new Vector2(0, 0));
		
		Array<Vector2> tempVertices = new Array<Vector2>();
		Array<Vector2> tempTextCoord = new Array<Vector2>();
		
		for(int i = 0; i < ((PolygonShape)crateShape).getVertexCount(); i++){
			vertices.add(new Vector2());
			((PolygonShape)crateShape).getVertex(i, vertices.get(i));
		}
		vertices.add(new Vector2(vertices.get(0)));
		
		for (int i = 0; i < vertices.size; i++) {
			if (i == vertices.size - 1) {
				Vector2 start = new Vector2(vertices.get(i));
				Vector2 end = vertices.get(0);
				
				Vector2 text_start = new Vector2(textCoord.get(i));
				Vector2 text_end = textCoord.get(0);
				
				tempVertices.add(vertices.get(i));
				tempTextCoord.add(textCoord.get(i));
				
				int numCuts = MathUtils.random(0, 3);
				for (int j = 0; j < numCuts; j++) {
					float rand = MathUtils.random(0.f, 1.f);
					Vector2 p = new Vector2(start.interpolate(end, rand, Interpolation.linear));
					tempVertices.add(p);
					Vector2 c = new Vector2(text_start.interpolate(text_end, rand, Interpolation.linear));
					tempTextCoord.add(c);
				}
				break;
			} else {
				Vector2 start = new Vector2(vertices.get(i));
				Vector2 end = vertices.get(i+1);
				
				Vector2 text_start = new Vector2(textCoord.get(i));
				Vector2 text_end = textCoord.get(i+1);
				
				tempVertices.add(vertices.get(i));
				tempTextCoord.add(textCoord.get(i));
				
				int numCuts = MathUtils.random(0, 3);
				for (int j = 0; j < numCuts; j++) {
					float rand = MathUtils.random(0.f, 1.f);
					Vector2 p = new Vector2(start.interpolate(end, rand, Interpolation.linear));
					tempVertices.add(p);
					Vector2 c = new Vector2(text_start.interpolate(text_end, rand, Interpolation.linear));
					tempTextCoord.add(c);
				}
			}
		}
		
		Array<FragileObject> fragileArray = new Array<FragileObject>();
		
		for (int i = 0; i < tempVertices.size;) {
			Array<Vector2> newVertices = new Array<Vector2>();
			Array<Vector2> newTextCoord = new Array<Vector2>();
			
			int numVetices = 0;
			
			if (tempVertices.size - 1 - i >= 3 ) {
				numVetices = MathUtils.random(1, 3);
			} else {
				numVetices = tempVertices.size - 1 - i;
				if (numVetices == 0) {
					break;
				}
			}
			
			newVertices.add(new Vector2(breakPoint));
			newTextCoord.add(new Vector2(breakPointTextCoord));
			
			for (int j = 0; j <= numVetices; j++) {
				newVertices.add(new Vector2(tempVertices.get(i+j)));
				newTextCoord.add(new Vector2(tempTextCoord.get(i+j)));
			}
			i += numVetices;
			
			FragilePoly fragilePoly = new FragilePoly(newVertices, newTextCoord, null, MathUtils.random(-0.5f, 0.5f), MathUtils.random(-0.5f, 0.5f));
			fragileArray.add(fragilePoly);
		}
		
	}


}
