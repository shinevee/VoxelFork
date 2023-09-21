#version 330 core

#define PI 3.14159265359

in vec3 pos;
in vec3 color;
in vec2 uv;

out vec4 fColor;

uniform sampler2D tex;
uniform bool hasTex = true;
// Sky
uniform sampler2D skyTex;
uniform float skyWidth;
uniform float skyHeight;
// Fog
uniform vec3 camPos;
uniform float distHor;
uniform float distVer;
uniform float fade;

float doFog(float fog) {
    fog = max(fog * 2.0 - 1.0, 0.0);
    fog *= fog;
    return mix(fog, 1.0, fade);
}

void main() {
    if (texture(tex, uv).a < 0.5) discard;
    float fog = clamp(max(distance(pos.xz, camPos.xz) / distHor, distance(pos.y, camPos.y) / distVer), 0.0, 1.0);
    fog = clamp(doFog(fog), 0.0, 1.0);

    vec4 worldColor = vec4(color, 1.0);
    if (hasTex) worldColor *= texture(tex, uv);
    vec4 skyColor = texture(skyTex, gl_FragCoord.xy / vec2(skyWidth, skyHeight));

    fColor = mix(worldColor, skyColor, fog);
}
