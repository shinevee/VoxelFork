#version 330 core

layout (location = 0) in vec3 aPos;

out vec2 pos;
out vec2 uv;

uniform mat4 view;
uniform mat4 proj;
uniform vec3 camPos;
uniform vec4 uvRange;
uniform float ticks;

void main() {
    vec3 finalPos = (camPos * vec3(1.0,0.0,1.0)) - vec3(512,0,512);
    vec2 uvOffset = (camPos.xz / 4096) + vec2(ticks / 4096,0);
    gl_Position = proj * view * vec4(((aPos * 1024) + finalPos) + vec3(0,128,0), 1.0);
    pos = aPos.xz;
    uv = mix(uvRange.xy + uvOffset, uvRange.zw + uvOffset, aPos.xz);
}