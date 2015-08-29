/*  This is a plugin example. Plugins are written in JavaScript.
    Plugins placed in this folder will automatically show up in
    the taskbar (Windows)/menubar (Mac)/nowhere (Linux) (help wanted). */


/*  The following variables along with all of their methods
    are available to you in this scirpt. Hint: leds.length
    is the number of LED segments the user has set.*/

/*//////// AVAILABLE VARIABLES ////////////
// Java Class       JavaScript var name  //
// ________________ ____________________ //
// int[NUM_LEDS][3] leds                 //
// FFT              FFT                  //
// BeatDetect       BeatDetect           //
/////////////////////////////////////////*/

/*  Below are the JavaDocs for these classes:
      FFT:
        http://code.compartmental.net/minim/javadoc/ddf/minim/analysis/FFT.html
      BeatDetect:
        http://code.compartmental.net/minim/javadoc/ddf/minim/analysis/BeatDetect.html
*/

/*  The update function will automatically be called
    30 times per second. After update finishes, the
    leds[][] array will be sent to the Arduino. */


// This example will pulse when a kick drum sound is detected from the
// audio input. (Soundflower (2ch) by default) Each kick will be a random color.


// Two beats will not be detected within 250ms of eachother. Default is 10ms
BeatDetect.setSensitivity(250);

function update()
{
    if ( BeatDetect.isKick() )
    {
        var rgb = HSVtoRGB( Math.random(), 1, 1 );
        setLEDs(rgb.r, rgb.g, rgb.b);
    }
}

function setLED(index, r, g, b)
{
	leds[index][0] = r;
	leds[index][1] = g;
	leds[index][2] = b;
}

function setLEDs(r, g, b)
{
	for (var i = 0; i < leds.length; i++)
	{
		setLED(i, r, g, b);
	}	
}

// Taken from here: http://stackoverflow.com/questions/17242144/javascript-convert-hsb-hsv-color-to-rgb-accurately
function HSVtoRGB(h, s, v) {
    var r, g, b, i, f, p, q, t;
    if (arguments.length === 1) {
        s = h.s, v = h.v, h = h.h;
    }
    i = Math.floor(h * 6);
    f = h * 6 - i;
    p = v * (1 - s);
    q = v * (1 - f * s);
    t = v * (1 - (1 - f) * s);
    switch (i % 6) {
        case 0: r = v, g = t, b = p; break;
        case 1: r = q, g = v, b = p; break;
        case 2: r = p, g = v, b = t; break;
        case 3: r = p, g = q, b = v; break;
        case 4: r = t, g = p, b = v; break;
        case 5: r = v, g = p, b = q; break;
    }
    return {
        r: Math.round(r * 255),
        g: Math.round(g * 255),
        b: Math.round(b * 255)
    };
}
