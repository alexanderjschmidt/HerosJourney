#ifdef GL_ES
	#define LOWP lowp
	precision mediump float;
#else
	#define LOWP 
#endif

varying LOWP vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;

//values used for shading algorithm...
uniform int lightNums;
uniform vec2 Resolution;      //resolution of screen
uniform vec3 LightPos[16];        //light position, normalized
uniform vec4 LightColor[16];      //light RGBA -- alpha is intensity
uniform vec4 AmbientColor;    //ambient RGBA -- alpha is intensity 
uniform vec3 Falloff[16];         //attenuation coefficients


void main()
{
	//RGBA of our diffuse color
	vec4 DiffuseColor = texture2D(u_texture, v_texCoords);
	
	vec3 Sum = vec3(0.0);
	for(int i = 0; i < lightNums; i++){
	    //Delta pos
		vec3 LightDir = vec3(LightPos[i].xy - (gl_FragCoord.xy / Resolution.xy), LightPos[i].z);
	
		//Correct for aspect ratio
		LightDir.x *= Resolution.x / Resolution.y;
	
		//determine magnitude
		float D = length(LightDir);
		
		vec3 Diffuse = (LightColor[i].rgb * LightColor[i].a);
		
		vec3 Ambient = AmbientColor.rgb * AmbientColor.a;
		
		float Attenuation = 1.0 / ( Falloff[i].x + (Falloff[i].y*D) + (Falloff[i].z*D*D) );
		
		vec3 Intensity = Ambient + Diffuse * Attenuation;
		vec3 FinalColor = DiffuseColor.rgb * Intensity;
	    Sum += FinalColor;
	    } 
	    
	gl_FragColor = v_color * vec4(Sum, DiffuseColor.a);
}
