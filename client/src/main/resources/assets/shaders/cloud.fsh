#version 330 core

in vec3 pos;
in vec2 uv;

out vec4 fColor;

uniform sampler2D tex;
// Fog
uniform vec3 camPos;
uniform float distHor;
uniform float distVer;

float doFog(float fog) {
    fog = max(fog * 2.0 - 1.0, 0.0);
    fog *= fog;
    return fog;
}

void main() {
    vec4 cloudColor = texture(tex, uv);
    if (cloudColor.a == 0) discard;

    float fog = clamp(max(distance(pos.xz, camPos.xz) / distHor, distance(pos.y, camPos.y) / distVer), 0.0, 1.0);
    fog = clamp(doFog(fog), 0.0, 1.0);
    fColor = vec4(cloudColor.xyz, cloudColor.a * (1.0 - fog));
}