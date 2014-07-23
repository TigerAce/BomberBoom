package com.game.bomberboom.core;


import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Segment;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.game.bomberboom.model.Crate;

public class MyBrick extends Crate{

	private Body brickBody;
	private FixtureDef fixtureDef;
	private BodyDef brickDef;
	private Shape shape;
	private World world;
	
	private Sprite brickSprite;
	
	private Vector<Body> affectedByLaser;
	private Vector<Vector2> entryPoint;
	private Vector<Body> explodingBodies;
	float explosionCenterX = 1;
	float explosionCenterY = 1;
	float worldScale = 0.1f;
	private float explosionRadius = 50;
	
	protected MyBrick(float x, float y, int size, World world) {
		super(x, y, size);
		this.world = world;
		
		explodingBodies = new Vector<Body>();
		//default 
		this.fixtureDef = new FixtureDef();
		this.brickDef = new BodyDef();
		// default crate
		
		brickDef.type = BodyType.DynamicBody;
		brickDef.position.set(x, y);
	
		//shape
		shape = new PolygonShape();
		((PolygonShape) shape).setAsBox(2, 2);
		
		//fixture
		
		fixtureDef.density = 10.0f;
		fixtureDef.friction = 0.6f;
		fixtureDef.restitution = 0;
		
		brickSprite = new Sprite(new Texture(Gdx.files.internal("img/texture/brick3.jpg")));
		brickSprite.setSize(2 * 2, 2 * 2);
		brickSprite.setOrigin(brickSprite.getWidth()/2, brickSprite.getHeight()/2);
	}

	public Body getBrickBody() {
		return brickBody;
	}

	public void setBrickBody(Body brickBody) {
		this.brickBody = brickBody;
	}

	public FixtureDef getFixtureDef() {
		return fixtureDef;
	}

	public void setFixtureDef(FixtureDef fixtureDef) {
		this.fixtureDef = fixtureDef;
	}

	public BodyDef getBrickDef() {
		return brickDef;
	}

	public void setBrickDef(BodyDef brickDef) {
		this.brickDef = brickDef;
	}

	public Shape getShape() {
		return shape;
	}

	public void setShape(Shape shape) {
		this.shape = shape;
	}

	public Sprite getBrickSprite() {
		return brickSprite;
	}

	public void setBrickSprite(Sprite brickSprite) {
		this.brickSprite = brickSprite;
	}

	@Override
	public void draw() {
		// TODO Auto-generated method stub
		fixtureDef.shape = shape;
		
		brickBody = world.createBody(brickDef);
		brickBody.createFixture(this.fixtureDef);
		brickBody.setUserData(new MyUserData("brick", null, brickSprite));
	}
	
	public void explodeFragment(){
		//float explosionCenterX = 0;
	//	float explosionCenterY = 0;
		explodingBodies.add(brickBody);
		for (int i = 0; i < 5; i++) {
			double cutAngle=Math.random()*Math.PI*2;
			
			Segment laserSegment=new Segment(new Vector3(),
											new Vector3());
			laserSegment.a.set((float)(explosionCenterX+i/10-200*Math.cos(cutAngle))/worldScale, (float)(explosionCenterY-200*Math.sin(cutAngle))/worldScale, 0);
			
			laserSegment.b.set((float)(explosionCenterX+200*Math.cos(cutAngle))/worldScale, (float)(explosionCenterY+200*Math.sin(cutAngle))/worldScale,0 );
			
			affectedByLaser = new Vector<Body>();
			entryPoint = new Vector<Vector2>();
			world.rayCast(new RayCast(), new Vector2(laserSegment.a.x,laserSegment.a.y), new Vector2(laserSegment.b.x, laserSegment.b.y));
			world.rayCast(new RayCast(), new Vector2(laserSegment.b.x, laserSegment.b.y),new Vector2(laserSegment.a.x,laserSegment.a.y));
			//affectedByLaser=new Vector.<b2Body>();
			//entryPoint=new Vector.<b2Vec2>();
			//world.RayCast(laserFired,laserSegment.p1,laserSegment.p2);
			//world.RayCast(laserFired,laserSegment.p2,laserSegment.p1);
		}
	}
	
	private class RayCast implements RayCastCallback{

		@Override
		public float reportRayFixture(Fixture fixture, Vector2 point,
				Vector2 normal, float fraction) {
			Body affectedBody = brickBody;
			if (explodingBodies.indexOf(affectedBody)!=-1) {
				PolygonShape affectedPolygon = (PolygonShape) fixtureDef.shape;
				int fixtureIndex = affectedByLaser.indexOf(affectedBody);
				if (fixtureIndex==-1) {
					affectedByLaser.add(affectedBody);
					entryPoint.add(point);
				}
				else {
					Vector2 rayCenter=new Vector2((point.x+entryPoint.get(fixtureIndex).x)/2,(point.y+entryPoint.get(fixtureIndex).y)/2);
					float rayAngle = (float) Math.atan2(entryPoint.get(fixtureIndex).y-point.y, entryPoint.get(fixtureIndex).x-point.x);
					Vector<Vector2> polyVertices = new Vector<Vector2>();
					for(int i = 0; i < affectedPolygon.getVertexCount(); i++){
						//polyVertices = new Vector<Vector2>(); 
						polyVertices.add(i, new Vector2());
						affectedPolygon.getVertex(i, polyVertices.get(i));
					}
					//Vector<Vector2>polyVertices = affectedPolygon.
					Vector<Vector2> newPolyVertices1=new Vector<Vector2>();
					Vector<Vector2> newPolyVertices2=new Vector<Vector2>();
					int currentPoly = 0;
					boolean cutPlaced1 = false;
					boolean cutPlaced2 = false;
					for (int i = 0; i < polyVertices.size(); i++) {
						Vector2 worldPoint = affectedBody.getWorldPoint(polyVertices.get(i));
						float cutAngle = (float) (Math.atan2(worldPoint.y-rayCenter.y,worldPoint.x-rayCenter.x)-rayAngle);
						if (cutAngle<Math.PI*-1) {
							cutAngle+=2*Math.PI;
						}
						if (cutAngle>0&&cutAngle<=Math.PI) {
							if (currentPoly==2) {
								cutPlaced1=true;
								newPolyVertices1.add(point);
								newPolyVertices1.add(entryPoint.get(fixtureIndex));
							}
							newPolyVertices1.add(worldPoint);
							currentPoly=1;
						}
						else {
							if (currentPoly==1) {
								cutPlaced2=true;
								newPolyVertices2.add(entryPoint.get(fixtureIndex));
								newPolyVertices2.add(point);
							}
							newPolyVertices2.add(worldPoint);
							currentPoly=2;

						}
					}
					if (! cutPlaced1) {
						newPolyVertices1.add(point);
						newPolyVertices1.add(entryPoint.get(fixtureIndex));
					}
					if (! cutPlaced2) {
						newPolyVertices2.add(entryPoint.get(fixtureIndex));
						newPolyVertices2.add(point);
					}
					createSlice(newPolyVertices1,newPolyVertices1.size());
					createSlice(newPolyVertices2,newPolyVertices2.size());
					world.destroyBody(affectedBody);
				}
			}
			return 1;
		}

	}
	
	private void createSlice(Vector<Vector2> vertices, int numVertices) {
		System.out.println(numVertices);
		for(int i = 0; i < vertices.size(); i++){
			System.out.println(vertices.get(i).x + "," + vertices.get(i).y);
		}
		
		// TODO Auto-generated method stub
		//System.out.println(getArea(vertices,vertices.size()));
		if (getArea(vertices,vertices.size())>=0.05) {
			Vector2 centre = findCentroid(vertices,vertices.size());
			for (int i = 0; i < numVertices; i++) {
				vertices.get(i).sub(centre);
			}
			
			BodyDef sliceBody= new BodyDef();
			sliceBody.position.x = centre.x;
			sliceBody.position.y = centre.y;
			sliceBody.type = BodyType.DynamicBody;
			PolygonShape slicePoly = new PolygonShape();
			
			//pro
			Vector2[] vers = (Vector2[]) vertices.toArray();
			
		//	System.out.println(vers.length);
		//	for(int i = 0; i < vers.length; i++){
		//		System.out.println(vers[i].x + "  " + vers[i].y);
		//	}
			slicePoly.set(vers);
			
			FixtureDef sliceFixture = new FixtureDef();
			sliceFixture.shape = slicePoly;
			sliceFixture.density = 1;
			Body worldSlice = world.createBody(sliceBody);
			worldSlice.createFixture(sliceFixture);
			for (int i=0; i < numVertices; i++) {
				vertices.get(i).add(centre);
			}
			float distX = (centre.x*worldScale-explosionCenterX);
			if (distX<0) {
				if (distX<-explosionRadius) {
					distX=0;
				}
				else {
					distX=-50-distX;
				}
			}
			else {
				if (distX>explosionRadius) {
					distX=0;
				}
				else {
					distX=50-distX;
				}
			}
			float distY = (centre.y*worldScale-explosionCenterY);
			if (distY<0) {
				if (distY<-explosionRadius) {
					distY=0;
				}
				else {
					distY=-50-distY;
				}
			}
			else {
				if (distY>explosionRadius) {
					distY=0;
				}
				else {
					distY=50-distY;
				}
			}
			distX*=0.25;
			distY*=0.25;
			worldSlice.setLinearVelocity(new Vector2(distX,distY));
			explodingBodies.add(worldSlice);
		}
	}
	private float getArea(Vector<Vector2> vs, int count){
		float area = 0.0f;
		float p1X = 0.0f;
		float p1Y = 0.0f;
		float inv3 = 1.0f/3.0f;
		for (int i = 0; i < count; ++i)  {
			Vector2 p2 = vs.get(i);
			//pro
			Vector2 p3 = i+1<count?vs.get(i+1):vs.get(0);
			float e1X = p2.x-p1X;
			float e1Y = p2.y-p1Y;
			float e2X = p3.x-p1X;
			float e2Y = p3.y-p1Y;
			float D = (e1X * e2Y - e1Y * e2X);
			float triangleArea = (float) (0.5f*D);
			area+=triangleArea;
			//System.out.println(triangleArea);
		}
		return area;
	}
	private Vector2 findCentroid(Vector<Vector2> vs, int count) {
		Vector2 c = new Vector2();
		float area = 0.0f;
		float p1X = 0.0f;
		float p1Y = 0.0f;
		float inv3 = 1.0f/3.0f;
		for (int i = 0; i < count; ++i) {
			Vector2 p2 = vs.get(i);
			//pro
			Vector2 p3 = i+1<count?vs.get(i+1):vs.get(0);
			float e1X = p2.x-p1X;
			float e1Y = p2.y-p1Y;
			float e2X = p3.x-p1X;
			float e2Y = p3.y-p1Y;
			float D = (e1X * e2Y - e1Y * e2X);
			float triangleArea = (float) (0.5*D);
			area+=triangleArea;
			c.x += triangleArea * inv3 * (p1X + p2.x + p3.x);
			c.y += triangleArea * inv3 * (p1Y + p2.y + p3.y);
		}
		c.x*=1.0/area;
		c.y*=1.0/area;
		return c;
	}
	
	public void finalized(){
		shape.dispose();
		brickSprite.getTexture().dispose();
	}

}
