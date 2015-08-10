// This pattern will light the light up when a kick drum sound is detected.

class Pattern
{
	int NUM_LEDS = 17; // Change this to however many LED segments you have
	public Color[] leds = new Color[NUM_LEDS]; // Do not change this line

	BeatDetect beat; // Optional
	FFT fft; // Optional

	Pattern(BeatDetect beat, FFT fft)
	{
		this.beat = beat; // Optional
		this.fft = fft; // Optional
	}

	// After this function gets called, the "leds"
	// array will be sent to the arduino.
	int value = 0;
	void update()
	{
		if(beat.isKick())
			value = 255;
		else
			value *= 0.65;

		for(int i = 0; i < NUM_LEDS; i++)
			leds[i] = new Color(value, value, value);
	}
}