package io.bluestaggo.voxelthing.renderer.shader;

import io.bluestaggo.voxelthing.renderer.shader.uniform.*;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.lwjgl.opengl.GL33C.*;

public class Shader {
	private final int handle;

	public Shader(String path) throws IOException {
		String vertexPath = path + ".vsh";
		String fragmentPath = path + ".fsh";
		String vertexSource, fragmentSource;

		// Load vertex shader code
		try (InputStream vertexStream = getClass().getResourceAsStream(vertexPath)) {
			if (vertexStream == null) {
				throw new IOException("Failed to open vertex shader \"" + vertexPath + "\"!");
			}

			BufferedReader reader = new BufferedReader(new InputStreamReader(vertexStream));
			StringBuilder stringBuilder = new StringBuilder();
			String currentLine;
			while ((currentLine = reader.readLine()) != null) {
				stringBuilder.append(currentLine);
				stringBuilder.append('\n');
			}
			vertexSource = stringBuilder.toString();
		}

		// Load fragment shader code
		try (InputStream fragmentStream = getClass().getResourceAsStream(fragmentPath)) {
			if (fragmentStream == null) {
				throw new IOException("Failed to open fragment shader \"" + fragmentPath + "\"!");
			}

			BufferedReader reader = new BufferedReader(new InputStreamReader(fragmentStream));
			StringBuilder stringBuilder = new StringBuilder();
			String currentLine;
			while ((currentLine = reader.readLine()) != null) {
				stringBuilder.append(currentLine);
				stringBuilder.append('\n');
			}
			fragmentSource = stringBuilder.toString();
		}

		// Load vertex shader
		int vertexShader = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vertexShader, vertexSource);
		glCompileShader(vertexShader);
		if (glGetShaderi(vertexShader, GL_COMPILE_STATUS) == GL_FALSE) {
			String log = glGetShaderInfoLog(vertexShader);
			throw new RuntimeException("Failed to compile shader \"" + vertexPath + "\"!\n" + log);
		}

		// Load fragment shader
		int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fragmentShader, fragmentSource);
		glCompileShader(fragmentShader);
		if (glGetShaderi(fragmentShader, GL_COMPILE_STATUS) == GL_FALSE) {
			String log = glGetShaderInfoLog(fragmentShader);
			throw new RuntimeException("Failed to compile fragment shader \"" + fragmentPath + "\"!\n" + log);
		}

		// Load program
		handle = glCreateProgram();
		glAttachShader(handle, vertexShader);
		glAttachShader(handle, fragmentShader);
		glLinkProgram(handle);
		if (glGetProgrami(handle, GL_LINK_STATUS) == GL_FALSE) {
			String log = glGetProgramInfoLog(handle);
			throw new RuntimeException("Failed to link program!\n" + log);
		}

		// Delete shaders
		glDeleteShader(vertexShader);
		glDeleteShader(fragmentShader);
	}

	public void unload() {
		glDeleteProgram(handle);
	}

	public void use() {
		glUseProgram(handle);
	}

	public ShaderUniform<Integer> getUniform1i(String name) {
		return new Uniform1i(handle, name);
	}

	public ShaderUniform<Float> getUniform1f(String name) {
		return new Uniform1f(handle, name);
	}

	public ShaderUniform<Vector2f> getUniform2f(String name) {
		return new Uniform2f(handle, name);
	}

	public ShaderUniform<Vector3f> getUniform3f(String name) {
		return new Uniform3f(handle, name);
	}

	public ShaderUniform<Vector4f> getUniform4f(String name) {
		return new Uniform4f(handle, name);
	}

	public ShaderUniform<Matrix4f> getUniformMatrix4fv(String name) {
		return new UniformMatrix4fv(handle, name);
	}

	public static void stop() {
		glUseProgram(0);
	}
}
