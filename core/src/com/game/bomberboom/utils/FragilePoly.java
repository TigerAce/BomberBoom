package com.game.bomberboom.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.game.bomberboom.utils.*;;

public class FragilePoly extends FragileObject implements Disposable{
	
	private Array<Vector2> vertices;
	private Array<Vector2> textCoord;
	private short[] indices;
	private MeshHelper meshHelper;
	private int min_fragments;
	private int max_fragments;
	private Vector2 breakPoint;
	private Vector2 breakPointTextCoord;

	public FragilePoly(Array<Vector2> vertices, Array<Vector2> textCoord, short[] indices, float x, float y) {
		// TODO Auto-generated constructor stub
		this.vertices = vertices;
		this.textCoord = textCoord;
		this.indices = indices;
		float[] temp = new float[8 * this.vertices.size];
		Color color = new Color(1.f, 1.f, 1.f, 1.f);
		for (int i = 0; i < vertices.size; i++) {
			temp[i*8] = (this.vertices.get(i).x + x);
			temp[i*8+1] = (this.vertices.get(i).y + y);
			
			temp[i*8+2] = (color.r);
			temp[i*8+3] = (color.g);
			temp[i*8+4] = (color.b);
			temp[i*8+5] = (color.a);
			
			temp[i*8+6] = (this.textCoord.get(i).x);
			temp[i*8+7] = (this.textCoord.get(i).y);
		}
		
		meshHelper = new MeshHelper();
		meshHelper.createMesh(temp);
		
		breakPoint = new Vector2();
		breakPointTextCoord = new Vector2();
		float randx = MathUtils.random(0.f, 1.f);
		float randy = MathUtils.random(0.f, 1.f);
		breakPoint.x = (Interpolation.linear).apply(-0.5f, 0.5f, randx);
		breakPoint.y = (Interpolation.linear).apply(0.5f, -0.5f, randy);
		breakPointTextCoord.set(randx, randy);
	}

	@Override
	public Array<FragileObject> breakDown() {
		// TODO Auto-generated method stub
		
		Array<Vector2> tempVertices = new Array<Vector2>();
		Array<Vector2> tempTextCoord = new Array<Vector2>();
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
		return fragileArray;
	}

	/* (non-Javadoc)
	 * @see com.boomberman.core.FragileObject#draw()
	 */
	@Override
	public void draw() {
		// TODO Auto-generated method stub
		meshHelper.drawMesh();
	}

	/* (non-Javadoc)
	 * @see com.boomberman.core.FragileObject#update()
	 */
	@Override
	public void update() {
		// TODO Auto-generated method stub
		super.update();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		meshHelper.dispose();
	}

}
