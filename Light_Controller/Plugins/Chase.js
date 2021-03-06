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


// 	This example will pulse white
var current_led = 0;
function update()
{
	// Turn the previous LED off
	setLED(fixIndex(current_led-1), 0,0,0);

	// Create a tail trailing off
	var tail_length = 5;
	for (var i = 0; i < tail_length; i++)
	{
		var value = 255/(tail_length-i)
		setLED(fixIndex(current_led+i), value, value, value);
	}
	current_led++;
}


function setLED(index, r, g, b)
{
	leds[index][0] = r;
	leds[index][1] = g;
	leds[index][2] = b;
}


function fixIndex(index)
{
	if (index < 0)
	{
		return (leds.length-1)-((-index-1) % leds.length);
	}
	return index % leds.length;
}