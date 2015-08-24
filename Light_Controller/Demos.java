public class Demos
{
	private Demos() {}

	// To convert these js fles to strings:
	// 1. Replace " with \\"

	// Some compilers don't like you splitting lines without closing a string.
	// to take care of that:
	// 2. Replace \n with \\n"+\n"

	// 3. Paste the contents in between two double quotes. ("")

	// Note: You need to use a find and replace that supports regex. I recommend
	// Sublime Text for this. Atom doesn't work for some reason.

	public static final String kick_detect_color =
	"/*	This is a plugin example. Plugins are written in JavaScript.\n"+
"	Plugins placed in this folder will automatically show up in\n"+
"	the taskbar (Windows)/menubar (Mac)/nowhere (Linux) (help wanted). */\n"+
"\n"+
"\n"+
"/* 	The following variables along with all of their methods\n"+
"	are available to you in this scirpt. Hint: leds.length\n"+
"	is the number of LED segments the user has set.*/\n"+
"\n"+
"/*//////// AVAILABLE VARIABLES ////////////\n"+
"// Java Class 		JavaScript var name  //\n"+
"// ________________ ____________________ //\n"+
"// int[NUM_LEDS][3] leds                 //\n"+
"// FFT 				FFT                  //\n"+
"// BeatDetect 		BeatDetect           //\n"+
"/////////////////////////////////////////*/\n"+
"\n"+
"/*	Below are the JavaDocs for these classes:\n"+
"	  FFT:\n"+
"		http://code.compartmental.net/minim/javadoc/ddf/minim/analysis/FFT.html\n"+
"	  BeatDetect:\n"+
"		http://code.compartmental.net/minim/javadoc/ddf/minim/analysis/BeatDetect.html\n"+
"*/\n"+
"\n"+
"/* 	The update function will automatically be called\n"+
"	30 times per second. After update finishes, the\n"+
"	leds[][] array will be sent to the Arduino. */\n"+
"\n"+
"\n"+
"// This example will pulse when a kick drum sound is detected from the\n"+
"// audio input. (Soundflower (2ch) by default) Each kick will be a random color.\n"+
"\n"+
"\n"+
"// Two beats will not be detected within 250ms of eachother. Default is 10ms\n"+
"BeatDetect.setSensitivity(250);\n"+
"\n"+
"function update()\n"+
"{\n"+
"	if ( BeatDetect.isKick() )\n"+
"	{\n"+
"		var rgb = HSVtoRGB( Math.random(), 1, 1 );\n"+
"		for( var i = 0; i < leds.length; i++ )\n"+
"		{\n"+
"			leds[i][0] = rgb.r;\n"+
"			leds[i][1] = rgb.g;\n"+
"			leds[i][2] = rgb.b;\n"+
"		}\n"+
"	}\n"+
"}\n"+
"\n"+
"// Taken from here: http://stackoverflow.com/questions/17242144/javascript-convert-hsb-hsv-color-to-rgb-accurately\n"+
"function HSVtoRGB(h, s, v) {\n"+
"    var r, g, b, i, f, p, q, t;\n"+
"    if (arguments.length === 1) {\n"+
"        s = h.s, v = h.v, h = h.h;\n"+
"    }\n"+
"    i = Math.floor(h * 6);\n"+
"    f = h * 6 - i;\n"+
"    p = v * (1 - s);\n"+
"    q = v * (1 - f * s);\n"+
"    t = v * (1 - (1 - f) * s);\n"+
"    switch (i % 6) {\n"+
"        case 0: r = v, g = t, b = p; break;\n"+
"        case 1: r = q, g = v, b = p; break;\n"+
"        case 2: r = p, g = v, b = t; break;\n"+
"        case 3: r = p, g = q, b = v; break;\n"+
"        case 4: r = t, g = p, b = v; break;\n"+
"        case 5: r = v, g = p, b = q; break;\n"+
"    }\n"+
"    return {\n"+
"        r: Math.round(r * 255),\n"+
"        g: Math.round(g * 255),\n"+
"        b: Math.round(b * 255)\n"+
"    };\n"+
"}\n"+
"";

	public static final String kick_detect =
	"/*	This is a plugin example. Plugins are written in JavaScript.\n"+
"	Plugins placed in this folder will automatically show up in\n"+
"	the taskbar (Windows)/menubar (Mac)/nowhere (Linux) (help wanted). */\n"+
"\n"+
"\n"+
"/* 	The following variables along with all of their methods\n"+
"	are available to you in this scirpt. Hint: leds.length\n"+
"	is the number of LED segments the user has set.*/\n"+
"\n"+
"/*//////// AVAILABLE VARIABLES ////////////\n"+
"// Java Class 		JavaScript var name  //\n"+
"// ________________ ____________________ //\n"+
"// int[NUM_LEDS][3] leds                 //\n"+
"// FFT 				FFT                  //\n"+
"// BeatDetect 		BeatDetect           //\n"+
"/////////////////////////////////////////*/\n"+
"\n"+
"/*	Below are the JavaDocs for these classes:\n"+
"	  FFT:\n"+
"		http://code.compartmental.net/minim/javadoc/ddf/minim/analysis/FFT.html\n"+
"	  BeatDetect:\n"+
"		http://code.compartmental.net/minim/javadoc/ddf/minim/analysis/BeatDetect.html\n"+
"*/\n"+
"\n"+
"/* 	The update function will automatically be called\n"+
"	30 times per second. After update finishes, the\n"+
"	leds[][] array will be sent to the Arduino. */\n"+
"\n"+
"\n"+
"// This example will pulse when a kick drum sound is detected from the\n"+
"// 	audio input. (Soundflower (2ch) by default)\n"+
"var brightness = 0;\n"+
"function update()\n"+
"{\n"+
"	if ( BeatDetect.isKick() )\n"+
"	{\n"+
"		brightness = 255;\n"+
"	}\n"+
"	else\n"+
"		brightness *= 0.65;\n"+
"\n"+
"	for(var i = 0; i < leds.length; i++)\n"+
"	{\n"+
"		leds[i][0] = brightness;\n"+
"		leds[i][1] = brightness;\n"+
"		leds[i][2] = brightness;\n"+
"	}\n"+
"}\n"+
"";

	public static final String pulse =
	"/*	This is a plugin example. Plugins are written in JavaScript.\n"+
"	Plugins placed in this folder will automatically show up in\n"+
"	the taskbar (Windows)/menubar (Mac)/nowhere (Linux) (help wanted). */\n"+
"\n"+
"\n"+
"/* 	The following variables along with all of their methods\n"+
"	are available to you in this scirpt. Hint: leds.length\n"+
"	is the number of LED segments the user has set.*/\n"+
"\n"+
"/*//////// AVAILABLE VARIABLES ////////////\n"+
"// Java Class 		JavaScript var name  //\n"+
"// ________________ ____________________ //\n"+
"// int[NUM_LEDS][3] leds                 //\n"+
"// FFT 				FFT                  //\n"+
"// BeatDetect 		BeatDetect           //\n"+
"/////////////////////////////////////////*/\n"+
"\n"+
"/*	Below are the JavaDocs for these classes:\n"+
"	  FFT:\n"+
"		http://code.compartmental.net/minim/javadoc/ddf/minim/analysis/FFT.html\n"+
"	  BeatDetect:\n"+
"		http://code.compartmental.net/minim/javadoc/ddf/minim/analysis/BeatDetect.html\n"+
"*/\n"+
"\n"+
"/* 	The update function will automatically be called\n"+
"	30 times per second. After update finishes, the\n"+
"	leds[][] array will be sent to the Arduino. */\n"+
"\n"+
"\n"+
"// 	This example will pulse white\n"+
"var c = 0;\n"+
"function update()\n"+
"{\n"+
"	for(var i = 0; i < leds.length; i++)\n"+
"	{\n"+
"		leds[i][0] = Math.sin(c)*127+127;\n"+
"		leds[i][1] = leds[i][0];\n"+
"		leds[i][2] = leds[i][1];\n"+
"	}\n"+
"	c += 0.1;\n"+
"}\n"+
"";

	public static final String rainbow =
	"/*	This is a plugin example. Plugins are written in JavaScript.\n"+
"	Plugins placed in this folder will automatically show up in\n"+
"	the taskbar (Windows)/menubar (Mac)/nowhere (Linux) (help wanted). */\n"+
"\n"+
"\n"+
"/* 	The following variables along with all of their methods\n"+
"	are available to you in this scirpt. Hint: leds.length\n"+
"	is the number of LED segments the user has set.*/\n"+
"\n"+
"/*//////// AVAILABLE VARIABLES ////////////\n"+
"// Java Class 		JavaScript var name  //\n"+
"// ________________ ____________________ //\n"+
"// int[NUM_LEDS][3] leds                 //\n"+
"// FFT 				FFT                  //\n"+
"// BeatDetect 		BeatDetect           //\n"+
"/////////////////////////////////////////*/\n"+
"\n"+
"/*	Below are the JavaDocs for these classes:\n"+
"	  FFT:\n"+
"		http://code.compartmental.net/minim/javadoc/ddf/minim/analysis/FFT.html\n"+
"	  BeatDetect:\n"+
"		http://code.compartmental.net/minim/javadoc/ddf/minim/analysis/BeatDetect.html\n"+
"*/\n"+
"\n"+
"/* 	The update function will automatically be called\n"+
"	30 times per second. After update finishes, the\n"+
"	leds[][] array will be sent to the Arduino. */\n"+
"\n"+
"\n"+
"// 	This example will fade through a rainbow of colors\n"+
"var value = 0;\n"+
"function update()\n"+
"{\n"+
"	value += 0.01;\n"+
"\n"+
"	for(var i = 0; i < leds.length; i++)\n"+
"	{\n"+
"		var rgb = HSVtoRGB(value%1, 1, 1);\n"+
"		leds[i][0] = rgb.r;\n"+
"		leds[i][1] = rgb.g;\n"+
"		leds[i][2] = rgb.b;\n"+
"	}\n"+
"}\n"+
"\n"+
"// Taken from here: http://stackoverflow.com/questions/17242144/javascript-convert-hsb-hsv-color-to-rgb-accurately\n"+
"function HSVtoRGB(h, s, v) {\n"+
"    var r, g, b, i, f, p, q, t;\n"+
"    if (arguments.length === 1) {\n"+
"        s = h.s, v = h.v, h = h.h;\n"+
"    }\n"+
"    i = Math.floor(h * 6);\n"+
"    f = h * 6 - i;\n"+
"    p = v * (1 - s);\n"+
"    q = v * (1 - f * s);\n"+
"    t = v * (1 - (1 - f) * s);\n"+
"    switch (i % 6) {\n"+
"        case 0: r = v, g = t, b = p; break;\n"+
"        case 1: r = q, g = v, b = p; break;\n"+
"        case 2: r = p, g = v, b = t; break;\n"+
"        case 3: r = p, g = q, b = v; break;\n"+
"        case 4: r = t, g = p, b = v; break;\n"+
"        case 5: r = v, g = p, b = q; break;\n"+
"    }\n"+
"    return {\n"+
"        r: Math.round(r * 255),\n"+
"        g: Math.round(g * 255),\n"+
"        b: Math.round(b * 255)\n"+
"    };\n"+
"}\n"+
"";

	public static final String pink =
	"/*	This is a plugin example. Plugins are written in JavaScript.\n"+
"	Plugins placed in this folder will automatically show up in\n"+
"	the taskbar (Windows)/menubar (Mac)/nowhere (Linux) (help wanted). */\n"+
"\n"+
"\n"+
"/* 	The following variables along with all of their methods\n"+
"	are available to you in this scirpt. Hint: leds.length\n"+
"	is the number of LED segments the user has set.*/\n"+
"\n"+
"/*//////// AVAILABLE VARIABLES ////////////\n"+
"// Java Class 		JavaScript var name  //\n"+
"// ________________ ____________________ //\n"+
"// int[NUM_LEDS][3] leds                 //\n"+
"// FFT 				FFT                  //\n"+
"// BeatDetect 		BeatDetect           //\n"+
"/////////////////////////////////////////*/\n"+
"\n"+
"/*	Below are the JavaDocs for these classes:\n"+
"	  FFT:\n"+
"		http://code.compartmental.net/minim/javadoc/ddf/minim/analysis/FFT.html\n"+
"	  BeatDetect:\n"+
"		http://code.compartmental.net/minim/javadoc/ddf/minim/analysis/BeatDetect.html\n"+
"*/\n"+
"\n"+
"/* 	The update function will automatically be called\n"+
"	30 times per second. After update finishes, the\n"+
"	leds[][] array will be sent to the Arduino. */\n"+
"\n"+
"\n"+
"// This example will set the LEDs to deeppink\n"+
"\n"+
"// We're never changing the color from deeppink, so we only need to execute this\n"+
"// code once. Therefore, we will put it outside the update function so that\n"+
"// it's not run every time update is called.\n"+
"for(var i = 0; i < leds.length; i++)\n"+
"{\n"+
"	leds[i][0] = 255;\n"+
"	leds[i][1] = 20;\n"+
"	leds[i][2] = 147;\n"+
"}\n"+
"\n"+
"function update()\n"+
"{\n"+
"\n"+
"}\n"+
"";

	public static final String red =
	"/*	This is a plugin example. Plugins are written in JavaScript.\n"+
"	Plugins placed in this folder will automatically show up in\n"+
"	the taskbar (Windows)/menubar (Mac)/nowhere (Linux) (help wanted). */\n"+
"\n"+
"\n"+
"/* 	The following variables along with all of their methods\n"+
"	are available to you in this scirpt. Hint: leds.length\n"+
"	is the number of LED segments the user has set.*/\n"+
"\n"+
"/*//////// AVAILABLE VARIABLES ////////////\n"+
"// Java Class 		JavaScript var name  //\n"+
"// ________________ ____________________ //\n"+
"// int[NUM_LEDS][3] leds                 //\n"+
"// FFT 				FFT                  //\n"+
"// BeatDetect 		BeatDetect           //\n"+
"/////////////////////////////////////////*/\n"+
"\n"+
"/*	Below are the JavaDocs for these classes:\n"+
"	  FFT:\n"+
"		http://code.compartmental.net/minim/javadoc/ddf/minim/analysis/FFT.html\n"+
"	  BeatDetect:\n"+
"		http://code.compartmental.net/minim/javadoc/ddf/minim/analysis/BeatDetect.html\n"+
"*/\n"+
"\n"+
"/* 	The update function will automatically be called\n"+
"	30 times per second. After update finishes, the\n"+
"	leds[][] array will be sent to the Arduino. */\n"+
"\n"+
"\n"+
"// This example will set the LEDs to red\n"+
"\n"+
"// We're never changing the color from red, so we only need to execute this\n"+
"// code once. Therefore, we will put it outside the update function so that\n"+
"// it's not run every time update is called.\n"+
"for(var i = 0; i < leds.length; i++)\n"+
"{\n"+
"	leds[i][0] = 255;\n"+
"}\n"+
"\n"+
"function update()\n"+
"{\n"+
"\n"+
"}\n"+
"";


}
