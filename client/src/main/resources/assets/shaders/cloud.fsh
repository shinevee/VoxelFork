#version 330 core

in vec2 pos;
in vec2 uv;

out vec4 fColor;
uniform sampler2D tex;
uniform float fogMultiplier;

void main() {
    float fog = 1.0-clamp(distance(pos, vec2(0.5,0.5)) * fogMultiplier, 0.0, 1.0);
    if(fog < 0.25) {
        fog = fog * 4;
    } else {
        fog = 1.0;
    }
    vec4 cloudColor = texture(tex, uv);
    fColor = vec4(cloudColor.xyz,cloudColor.a * fog);
}