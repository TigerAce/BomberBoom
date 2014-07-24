package com.game.bomberboom.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class MeshHelper {
	private Mesh mesh;
	private ShaderProgram meshShader;
	Texture lastTexture = null;

	public MeshHelper() {
		createShader();
		lastTexture = new Texture(Gdx.files.internal("img/texture/brick1.jpg"));
		lastTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}

	public void createMesh(float[] vertices) {
		mesh = new Mesh(true, vertices.length, 0, 
				new VertexAttribute(Usage.Position, 2, ShaderProgram.POSITION_ATTRIBUTE), 
				new VertexAttribute(Usage.Color, 4, ShaderProgram.COLOR_ATTRIBUTE), 
				new VertexAttribute(Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE + "0"));
		mesh.setVertices(vertices);
		//mesh.setIndices(new short[]{0, 1, 2, 2, 3, 1});
	}

	public void drawMesh() {
		// this should be called in render()
		if (mesh == null)
			throw new IllegalStateException("drawMesh called before a mesh has been created.");

		Gdx.gl.glEnable(GL20.GL_TEXTURE_2D);
		meshShader.begin();
		lastTexture.bind(0);
		meshShader.setUniformi("u_texture", 0);
		mesh.render(meshShader, GL20.GL_TRIANGLE_FAN);
		meshShader.end();
	}

	private void createShader() {
		// this shader tells opengl where to put things
		String vertexShader = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
				+ "attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
				+ "attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
				+ "uniform mat4 u_projTrans;\n" //
				+ "varying vec4 v_color;\n" //
				+ "varying vec2 v_texCoords;\n" //
				+ "\n" //
				+ "void main()\n" //
				+ "{\n" //
				+ "   v_color = " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
				+ "   v_color.a = v_color.a * (256.0/255.0);\n" //
				+ "   v_texCoords = " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" //
				+ "   gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
				+ "   gl_Position = " + ShaderProgram.POSITION_ATTRIBUTE+";  \n" //
				+ "}\n";

		// this one tells it what goes in between the points (i.e
		// colour/texture)
		String fragmentShader = "#ifdef GL_ES\n" //
				+ "#define LOWP lowp\n" //
				+ "precision mediump float;\n" //
				+ "#else\n" //
				+ "#define LOWP \n" //
				+ "#endif\n" //
				+ "varying LOWP vec4 v_color;\n" //
				+ "varying vec2 v_texCoords;\n" //
				+ "uniform sampler2D u_texture;\n" //
				+ "void main()\n"//
				+ "{\n" //
				+ "  gl_FragColor = v_color * texture2D(u_texture, v_texCoords);\n" //
				+ "}";

		// make an actual shader from our strings
		meshShader = new ShaderProgram(vertexShader, fragmentShader);

		// check there's no shader compile errors
		if (meshShader.isCompiled() == false)
			throw new IllegalStateException(meshShader.getLog());
	}
	
	public void dispose() {
		mesh.dispose();
		meshShader.dispose();
	}

}
