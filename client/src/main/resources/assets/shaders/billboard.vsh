#version 330 core

layout (location = 0) in vec3 aPos;

out vec3 pos;
out vec2 uv;

uniform mat4 modelView;
uniform mat4 proj;
uniform vec2 size;
uniform vec2 align;
uniform vec4 uvRange;

void main() {
    gl_Position = proj * modelView * vec4((aPos.xy - align) * size, aPos.z, 1.0);
    pos = vec3(aPos.xy - align, 0.0);
    uv = mix(uvRange.xy, uvRange.zw, aPos.xy);
}