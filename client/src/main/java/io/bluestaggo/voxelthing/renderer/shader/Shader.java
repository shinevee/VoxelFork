package io.bluestaggo.voxelthing.renderer.shader;

import io.bluestaggo.voxelthing.renderer.shader.uniform.*;

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
		String vertexSource = readShaderSource(vertexPath);
		String fragmentSource = readShaderSource(fragmentPath);

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

	private static String readShaderSource(String path) throws IOException {
		try (InputStream shaderStream = Shader.class.getResourceAsStream(path)) {
			if (shaderStream == null) {
				throw new IOException("Failed to open shader \"" + path + "\"!");
			}

			BufferedReader reader = new BufferedReader(new InputStreamReader(shaderStream));
			StringBuilder stringBuilder = new StringBuilder();
			String currentLine;
			while ((currentLine = reader.readLine()) != null) {
				if (currentLine.startsWith("#include")) {
					String includePath = currentLine.substring(currentLine.indexOf('"') + 1, currentLine.lastIndexOf('"'));
					if (!includePath.startsWith("/")) {
						includePath = "/" + includePath;
					}

					stringBuilder.append(readShaderSource(includePath));
				} else {
					stringBuilder.append(currentLine);
				}
				stringBuilder.append('\n');
			}
			return stringBuilder.toString();
		}
	}

	public void unload() {
		glDeleteProgram(handle);
	}

	public void use() {
		glUseProgram(handle);
	}

	public Uniform1b getUniform1b(String name) {
		return new Uniform1b(handle, name);
	}

	public Uniform1i getUniform1i(String name) {
		return new Uniform1i(handle, name);
	}

	public Uniform1f getUniform1f(String name) {
		return new Uniform1f(handle, name);
	}

	public Uniform2f getUniform2f(String name) {
		return new Uniform2f(handle, name);
	}

	public Uniform3f getUniform3f(String name) {
		return new Uniform3f(handle, name);
	}

	public Uniform4f getUniform4f(String name) {
		return new Uniform4f(handle, name);
	}

	public UniformMatrix4fv getUniformMatrix4fv(String name) {
		return new UniformMatrix4fv(handle, name);
	}

	public static void stop() {
		glUseProgram(0);
	}
}
