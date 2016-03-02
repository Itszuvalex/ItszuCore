#version 440 core

uniform sampler2D texture_diffuse;

in vec4 pass_LightColor;
in vec2 pass_TextureCoord;

out vec4 out_Color;

void main() {
    out_Color = mix(texture(texture_diffuse, pass_TextureCoord), pass_LightColor, pass_LightColor[3]);
}
