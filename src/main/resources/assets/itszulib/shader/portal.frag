#version 120

uniform sampler2D bgl_RenderedTexture;

void main() {
    vec4 color = texture2D(bgl_RenderedTexture, gl_FragCoord.st/256);
    gl_FragColor = vec4(color.r / 10, color.g /10, color.b/10, color.a);
}