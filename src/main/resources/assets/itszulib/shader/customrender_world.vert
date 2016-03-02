#version 440 core

in vec4 in_Position;
in vec3 in_Normal;
in vec2 in_TextureCoord;

out vec4 pass_LightColor;
out vec2 pass_TextureCoord;

void main() {
    gl_Position = gl_ModelViewProjectionMatrix * in_Position;
    pass_LightColor = vec4(1, 0, 0, 1);
    pass_TextureCoord = in_TextureCoord;
}
