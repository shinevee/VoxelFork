package io.bluestaggo.voxelthing.renderer.shader;

import io.bluestaggo.voxelthing.renderer.shader.modules.FogInfo;
import io.bluestaggo.voxelthing.renderer.shader.uniform.*;

import java.io.IOException;

public class CloudShader extends Shader {
    public final UniformMatrix4fv viewProj;
    public final Uniform3f offset;
    public final Uniform4f uvRange;
    public final Uniform1f cloudHeight;

    public final Uniform1i tex;
    public final FogInfo fogInfo;

    public CloudShader() throws IOException {
        super("/assets/shaders/cloud");
        use();

        viewProj = getUniformMatrix4fv("viewProj");
        offset = getUniform3f("offset");
        (uvRange = getUniform4f("uvRange")).set(0.0f, 0.0f, 0.25f, 0.25f);
        cloudHeight = getUniform1f("cloudHeight");

        (tex = getUniform1i("tex")).set(0);
        fogInfo = new FogInfo(this);

        stop();
    }
}
