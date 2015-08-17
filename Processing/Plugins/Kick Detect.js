/*	This is a plugin example. Plugins are written in JavaScript.
	Plugins placed in this folder will automatically show up in
	the taskbar (Windows)/menubar (Mac)/nowhere (Linux) (help wanted). */


/* 	The following variables along with all of their methods
	are available to you in this scirpt. Hint: leds.length
	is the number of LED segments the user has set.*/

/*//////// AVAILABLE VARIABLES ////////////
// Java Class 		JavaScript var name  //
// ________________ ____________________ //
// int[NUM_LEDS][3] leds                 //
// FFT 				FFT                  //
// BeatDetect 		BeatDetect           //
/////////////////////////////////////////*/

/*	Below are the JavaDocs for these classes:
	  FFT:
		http://code.compartmental.net/minim/javadoc/ddf/minim/analysis/FFT.html
	  BeatDetect:
		http://code.compartmental.net/minim/javadoc/ddf/minim/analysis/BeatDetect.html
*/

/* 	The update function will automatically be called
	30 times per second. After update finishes, the
	leds[][] array will be sent to the Arduino. */


// This example will pulse when a kick drum sound is detected from the
// 	audio input. (Soundflower (2ch) by default)
var brightness = 0;
function update()
{
	if ( BeatDetect.isKick() )
	{
		brightness = 255;
	}
	else
		brightness *= 0.65;

	for(var i = 0; i < leds.length; i++)
	{
		leds[i][0] = brightness;
		leds[i][1] = brightness;
		leds[i][2] = brightness;
	}
}
