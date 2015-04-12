#version 120

uniform sampler2D bgl_RenderedTexture;

void main() {
    vec4 fragcoord = gl_FragCoord;
    gl_FragColor = texture2D(bgl_RenderedTexture, vec2(fragcoord.x, fragcoord.y));
}