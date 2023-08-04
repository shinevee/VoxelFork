#version 330 core

#define PI 3.14159265359

in vec3 pos;
in vec3 color;
in vec2 uv;

out vec4 fColor;

uniform sampler2D tex;
uniform sampler2D skyTex;
uniform vec3 camPos;
uniform float camFar;
uniform float width;
uniform float height;
uniform int fogAlgorithm = 3;

float doFog(float fog) {
    return fog * 2.0 - 1.0;
}

void main() {
    float fog = clamp(distance(pos, camPos) / camFar, 0.0, 1.0);
    fog = clamp(doFog(fog), 0.0, 1.0);
    fColor = mix(vec4(color, 1.0) * texture(tex, uv), texture(skyTex, gl_FragCoord.xy / vec2(width, height)), fog);
}
