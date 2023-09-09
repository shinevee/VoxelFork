package io.bluestaggo.voxelthing.renderer.shader;

import io.bluestaggo.voxelthing.renderer.shader.uniform.ShaderUniform;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.io.IOException;

public class CloudShader extends Shader {
    public final ShaderUniform<Matrix4f> view;
    public final ShaderUniform<Matrix4f> proj;
    public final ShaderUniform<Vector3f> camPos;
    public final ShaderUniform<Vector4f> uvRange;
    public final ShaderUniform<Float> ticks;
    public final ShaderUniform<Integer> tex;
    public final ShaderUniform<Float> fogMultiplier;

    public CloudShader() throws IOException {
        super("/assets/shaders/cloud");
        use();
        view = getUniformMatrix4fv("view");
        proj = getUniformMatrix4fv("proj");
        (uvRange = getUniform4f("uvRange")).set(new Vector4f(0.0F,0.0F,64F/256F,64F/256F));
        ticks = getUniform1f("ticks");
        (tex = getUniform1i("tex")).set(0);
        camPos = getUniform3f("camPos");
        fogMultiplier = getUniform1f("fogMultiplier");
        stop();
    }
}
