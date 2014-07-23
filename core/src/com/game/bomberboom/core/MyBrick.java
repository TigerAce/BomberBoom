package com.game.bomberboom.core;


import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
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
import com.badlogic.gdx.utils.Array;
import com.game.bomberboom.model.Barrier;

public class MyBrick extends Barrier{


	
	/**
	 * box2d parameters
	 */
	private Body brickBody;
	private FixtureDef brickFixtureDef;
	private BodyDef brickDef;
	private Shape brickShape;
	private World world;
		
	//sprite
	private Sprite brickSprite;
	
	
	public static int brickNum = 0;
	private Vector<Body> affectedByLaser;
	private HashMap<Integer, Vector2> enterPointMap;
//	private Vector<Vector2> enterPointsVec;
	private Vector<Body> explodingBodies;
	float explosionX;
	float explosionY;
	float worldScale = 40f;
	private float explosionRadius = 50;

	public MyBrick(float x, float y, float size, World world) {
		super(x, y, size);
		this.world = world;
		
		explodingBodies = new Vector<Body>();
		enterPointMap = new HashMap<Integer, Vector2>();
		
		this.brickFixtureDef = new FixtureDef();
		this.brickDef = new BodyDef();
		
		
		// default brick
		
		brickDef.type = BodyType.DynamicBody;
		brickDef.position.set(x, y);
	
		//shape
		brickShape = new PolygonShape();
		((PolygonShape) brickShape).setAsBox(this.size/2, this.size/2);
		
		//fixture
		
		brickFixtureDef.density = 60.0f;
		brickFixtureDef.friction = 0.6f;
		brickFixtureDef.restitution = 0;
		
		//sprite
		brickSprite = new Sprite(new Texture(Gdx.files.internal("img/texture/brick3.jpg")));
		brickSprite.setSize(size, size);
		brickSprite.setOrigin(brickSprite.getWidth()/2, brickSprite.getHeight()/2);
	}

	public Body getBrickBody() {
		return brickBody;
	}

	public void setBrickBody(Body brickBody) {
		this.brickBody = brickBody;
	}

	public FixtureDef getFixtureDef() {
		return brickFixtureDef;
	}

	public void setFixtureDef(FixtureDef fixtureDef) {
		this.brickFixtureDef = fixtureDef;
	}

	public BodyDef getBrickDef() {
		return brickDef;
	}

	public void setBrickDef(BodyDef brickDef) {
		this.brickDef = brickDef;
	}

	public Shape getShape() {
		return brickShape;
	}

	public void setShape(Shape shape) {
		this.brickShape = shape;
	}

	public Sprite getBrickSprite() {
		return brickSprite;
	}

	public void setBrickSprite(Sprite brickSprite) {
		this.brickSprite = brickSprite;
	}

	@Override
	public void draw() {
		
		brickFixtureDef.shape = brickShape;
		
		brickBody = world.createBody(brickDef);
		brickBody.createFixture(this.brickFixtureDef);
		brickBody.setLinearDamping(0.5f);
		brickBody.setAngularDamping(0.5f);
		brickBody.setUserData(new MyUserData("brick", null, brickSprite));
		((MyUserData)brickBody.getUserData()).setID(brickNum);
		enterPointMap.put(brickNum, null);
		brickNum++;
		
	}
	
/*	public void explodeFragment(){
		
		explosionCenterX = this.brickBody.getWorldCenter().x;
	    explosionCenterY = this.brickBody.getWorldCenter().y;
		explodingBodies.add(this.brickBody);
		
		for (int i = 0; i < 2; i++) {
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
					float rayAngle = (float) Math.atan2((double)entryPoint.get(fixtureIndex).y-point.y, (double)entryPoint.get(fixtureIndex).x-point.x);
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
					Vector2 worldPoint;
					for (int i = 0; i < polyVertices.size(); i++) {
						worldPoint = affectedBody.getWorldPoint(polyVertices.get(i));
						float cutAngle = (float) (Math.random()*Math.PI*2);//(float) (Math.atan2(worldPoint.y-rayCenter.y,worldPoint.x-rayCenter.x)-rayAngle);
						if (cutAngle<Math.PI*-1) {
							cutAngle+=2*Math.PI;
						}
						if (cutAngle>0&&cutAngle<=Math.PI) {
							if (currentPoly==2) {
								cutPlaced1=true;
								newPolyVertices1.add(new Vector2(point));
								newPolyVertices1.add(new Vector2(entryPoint.get(fixtureIndex)));
							}
							newPolyVertices1.add(new Vector2(worldPoint));
							currentPoly=1;
						}
						else {
							if (currentPoly==1) {
								cutPlaced2=true;
								newPolyVertices2.add(new Vector2((entryPoint.get(fixtureIndex))));
								newPolyVertices2.add(new Vector2(point));
							}
							newPolyVertices2.add(new Vector2(worldPoint));
							currentPoly=2;

						}
					}
					if (! cutPlaced1) {
						newPolyVertices1.add(new Vector2(point));
						newPolyVertices1.add(new Vector2(entryPoint.get(fixtureIndex)));
					}
					if (! cutPlaced2) {
						newPolyVertices2.add(new Vector2(entryPoint.get(fixtureIndex)));
						newPolyVertices2.add(new Vector2(point));
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
			
			Vector2[] vers = new Vector2[vertices.size()];
			vertices.toArray(vers);
	
			slicePoly.set(vers);
			
			FixtureDef sliceFixture = new FixtureDef();
			sliceFixture.shape = slicePoly;
			sliceFixture.density = 1;
			Body worldSlice = world.createBody(sliceBody);
			worldSlice.createFixture(sliceFixture);
			for (int i=0; i < numVertices; i++) {
				vertices.get(i).add(new Vector2(centre));
			}
			/*float distX = (centre.x*worldScale-explosionCenterX);
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
	}*/
	public void boom() {
		float cutAngle;
		explosionX = this.brickBody.getPosition().x;
		explosionY = this.brickBody.getPosition().y;
		

			// storing the exploding bodies in a vector. I need to do it since I do not want other bodies
			// to be affected by the raycast and explode
			explodingBodies = new Vector<Body>();
			explodingBodies.add(this.brickBody);
			
			System.out.println(brickNum);
			//enterPointsVec = new Vector<Vector2>(brickNum);
			//for(int i = 0; i < enterPointsVec.size(); i++){
			//	enterPointsVec.set(i, new Vector2(0,0));
			//}
			// the explosion begins!
			for (int i = 1; i <= 5; i++) {
				// choosing a random angle
				cutAngle=(float) (Math.random()*Math.PI*2);
				// creating the two points to be used for the raycast, according to the random angle and mouse position
				// also notice how I need to add a little offset (i/10) or Box2D will crash. Probably it's not able to 
				// determine raycast on objects whose area is very very close to zero (or zero)
				Vector2 p1 = new Vector2((float)((explosionX+i/10-2000*Math.cos(cutAngle))/worldScale),
										(float)((explosionY-2000*Math.sin(cutAngle))/worldScale));
				Vector2 p2 = new Vector2((float)((explosionX+2000*Math.cos(cutAngle))/worldScale),
										(float)((explosionY+2000*Math.sin(cutAngle))/worldScale));
				world.rayCast(new Intersection(), p1, p2);
				world.rayCast(new Intersection(), p2, p1);
				
			}
		}	
	
	
    private class Intersection implements RayCastCallback{

	@Override
	public float reportRayFixture(Fixture fixture, Vector2 point,
			Vector2 normal, float fraction) {
		
	//sure -1?
		if (explodingBodies.indexOf(fixture.getBody())!=-1) {
	
			MyUserData data = ((MyUserData)(fixture.getBody().getUserData()));
			
		if (enterPointMap.get(data.getID()) != null) {
			// If this body has already had an intersection point, then it now has two intersection points, thus it must be split in two - thats where the splitObj() method comes in.
			splitObj(fixture.getBody(), enterPointMap.get(data.getID()), point.cpy());
		}
		else {
			enterPointMap.put(data.getID(),(new Vector2(point)));
		}
		
		}return 1;
		}
    }
	
	private void splitObj(Body sliceBody,Vector2 A, Vector2 B) {
		
		
		Array<Fixture> origFixture = sliceBody.getFixtureList();
		Fixture f = origFixture.get(0);
		PolygonShape poly = (PolygonShape) f.getShape();
		
		Vector<Vector2> verticesVec = new Vector<Vector2>();
		Vector2 tmp = new Vector2();
		int numVertices = poly.getVertexCount();
		for (int i = 0; i < numVertices; i++) {
		    // fill tmp with the vertex
		    poly.getVertex(i, tmp);
		    verticesVec.add(new Vector2(tmp));
		}
	
		Vector<Vector2> shape1Vertices = new Vector<Vector2>();
		Vector<Vector2> shape2Vertices = new Vector<Vector2>();
		
		//skiped
		MyUserData userData = (MyUserData) sliceBody.getUserData();
		int origUserDataId = userData.getID();
		//var origUserData:userData=sliceBody.GetUserData(),origUserDataId:int=origUserData.id,d:Number;
		
		PolygonShape polyShape = new PolygonShape();
		Body body;
		// First, I destroy the original body and remove its Sprite representation from the childlist.
		world.destroyBody(sliceBody);
		//skiped
		//removeChild(origUserData);
		// The world.RayCast() method returns points in world coordinates, so I use the b2Body.GetLocalPoint() to convert them to local coordinates.;
		A=sliceBody.getLocalPoint(A);
		B=sliceBody.getLocalPoint(B);
		// I use shape1Vertices and shape2Vertices to store the vertices of the two new shapes that are about to be created. 
		// Since both point A and B are vertices of the two new shapes, I add them to both vectors.
		shape1Vertices.add(new Vector2(A));
		shape1Vertices.add(new Vector2(B));
		shape2Vertices.add(new Vector2(A));
		shape2Vertices.add(new Vector2(B));
		// I iterate over all vertices of the original body. ;
		// I use the function det() ("det" stands for "determinant") to see on which side of AB each point is standing on. The parameters it needs are the coordinates of 3 points:
		// - if it returns a value >0, then the three points are in clockwise order (the point is under AB)
		// - if it returns a value =0, then the three points lie on the same line (the point is on AB)
		// - if it returns a value <0, then the three points are in counter-clockwise order (the point is above AB). 
		for (int i = 0; i < numVertices; i++) {
			float d = det(A.x,A.y,B.x,B.y,verticesVec.get(i).x,verticesVec.get(i).y);
			if (d > 0) {
				shape1Vertices.add(new Vector2(verticesVec.get(i)));
			}
			else {
				shape2Vertices.add(new Vector2(verticesVec.get(i)));
			}
		}
		// In order to be able to create the two new shapes, I need to have the vertices arranged in clockwise order.
		// I call my custom method, arrangeClockwise(), which takes as a parameter a vector, representing the coordinates of the shape's vertices and returns a new vector, with the same points arranged clockwise.
		shape1Vertices=arrangeClockwise(shape1Vertices);
		shape2Vertices=arrangeClockwise(shape2Vertices);
		// setting the properties of the two newly created shapes
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.x = sliceBody.getPosition().x;
		bodyDef.position.y = sliceBody.getPosition().y;
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density=f.getDensity();
		fixtureDef.friction=f.getFriction();
		fixtureDef.restitution=f.getRestitution();
		// creating the first shape, if big enough
		if (getArea(shape1Vertices,shape1Vertices.size())>=0.05) {
			
			//double check
			Vector2[] vers = new Vector2[shape1Vertices.size()];
			shape1Vertices.toArray(vers);
			polyShape.set(vers);
			
			fixtureDef.shape=polyShape;
			//bodyDef.userData=new userData(origUserDataId,shape1Vertices,origUserData.texture);
			//addChild(bodyDef.userData);
			enterPointMap.put(origUserDataId, null);
			body = world.createBody(bodyDef);
			
			body.setAngularVelocity(sliceBody.getAngle());
			body.createFixture(fixtureDef);
			// setting a velocity for the debris
			body.setLinearVelocity(setExplosionVelocity(body));
			// the shape will be also part of the explosion and can explode too
			explodingBodies.add(body);
		}
		// creating the second shape, if big enough
		if (getArea(shape2Vertices,shape2Vertices.size())>=0.05) {
			
		
			Vector2[] vers = new Vector2[shape2Vertices.size()];
			shape2Vertices.toArray(vers);
			polyShape.set(vers);
			
			fixtureDef.shape=polyShape;
			//bodyDef.userData=new userData(numEnterPoints,shape2Vertices,origUserData.texture);
			//addChild(bodyDef.userData);
			/**
			 * what is going on?
			 */
			//enterPointMap.put(null, null);
			MyBrick.brickNum++;
			body=world.createBody(bodyDef);
			body.setAngularVelocity(sliceBody.getAngle());
			body.createFixture(fixtureDef);
			// setting a velocity for the debris
			body.setLinearVelocity(setExplosionVelocity(body));
			// the shape will be also part of the explosion and can explode too
			explodingBodies.add(body);
		}
	}

	private Vector2 setExplosionVelocity(Body b){
		float distX = b.getWorldCenter().x*worldScale-explosionX;
		if (distX<0) {
			if (distX<-explosionRadius) {
				distX=0;
			}
			else {
				distX=- explosionRadius-distX;
			}
		}
		else {
			if (distX>explosionRadius) {
				distX=0;
			}
			else {
				distX=explosionRadius-distX;
			}
		}
		float distY = b.getWorldCenter().y*worldScale-explosionY;
		if (distY<0) {
			if (distY<-explosionRadius) {
				distY=0;
			}
			else {
				distY=- explosionRadius-distY;
			}
		}
		else {
			if (distY>explosionRadius) {
				distY=0;
			}
			else {
				distY=explosionRadius-distY;
			}
		}
		distX*=0.25;
		distY*=0.25;
		return new Vector2(distX,distY);
	}
	private Vector<Vector2> arrangeClockwise(Vector<Vector2> vec){
		// The algorithm is simple: 
		// First, it arranges all given points in ascending order, according to their x-coordinate.
		// Secondly, it takes the leftmost and rightmost points (lets call them C and D), and creates tempVec, where the points arranged in clockwise order will be stored.
		// Then, it iterates over the vertices vector, and uses the det() method I talked about earlier. It starts putting the points above CD from the beginning of the vector, and the points below CD from the end of the vector. 
		// That was it!
		/**
		 * double check
		 */
		System.out.println(vec.size());
		int n = vec.size();
		float d;
		int i1 = 1;
		int i2 = n - 1;
	
		
		Vector<Vector2> tempVec = new Vector<Vector2>();
		for(int i = 0; i < vec.size(); i++){
			tempVec.add(new Vector2());
		}
		Vector2 C;
		Vector2 D;
		
		Collections.sort(vec, new CompVector());
		
		//new vector???
		tempVec.get(0).set(new Vector2(vec.get(0)));
		//tempVec.get(0) = new Vector2(vec.get(0));
		
		C = new Vector2(vec.get(0));
		D = new Vector2(vec.get(n - 1));
		for (int i = 1; i < n-1; i++) {
			d=det(C.x,C.y,D.x,D.y,vec.get(i).x,vec.get(i).y);
			if (d<0) {
				tempVec.get(i1++).set(new Vector2(vec.get(i)));
			}
			else {
				tempVec.get(i2--).set(new Vector2(vec.get(i)));
	
			}
		}
		tempVec.get(i1).set(new Vector2(vec.get(n - 1)));
		return tempVec;
	}
	
	public class CompVector implements Comparator<Vector2> {
	    @Override
	    public int compare(Vector2 a, Vector2 b) {
	    	if (a.x>b.x) {
				return 1;
			}
			else if (a.x<b.x) {
				return -1;
			}
			return 0;
	    }
	}
	
	private float det(float x1, float y1, float x2, float y2, float x3, float y3) {
		// This is a function which finds the determinant of a 3x3 matrix.
		// If you studied matrices, you'd know that it returns a positive number if three given points are in clockwise order, negative if they are in anti-clockwise order and zero if they lie on the same line.
		// Another useful thing about determinants is that their absolute value is two times the face of the triangle, formed by the three given points.
		return x1*y2+x2*y3+x3*y1-y1*x2-y2*x3-y3*x1;
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
	
	protected void finalized(){
		brickShape.dispose();
		brickSprite.getTexture().dispose();
	}

}
