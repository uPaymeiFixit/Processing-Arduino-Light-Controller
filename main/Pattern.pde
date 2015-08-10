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
	int counter = 0; 
	void update()
	{
		counter++;
		for(int i = 0; i < NUM_LEDS; i++)
			leds[i] = Color.getHSBColor((counter%360)/360.0, 1, 1);
	}
}