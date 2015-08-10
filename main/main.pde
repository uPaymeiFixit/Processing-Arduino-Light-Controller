import ddf.minim.Minim;
import ddf.minim.AudioInput;
import ddf.minim.analysis.FFT;
import ddf.minim.analysis.BeatDetect;
import processing.serial.Serial;
import java.awt.Color;

static final boolean VISIBLE = false;
static final int BUFFER_SIZE = 2048;
static final int BAUD_RATE = 115200;
static final int FRAME_RATE = 10; 
static final int SERIAL_INDEX = 3;

Minim minim;
AudioInput in;
BeatDetect beat;
FFT fft;
Serial myPort;
Pattern pattern;

void setup()
{
    // Set up minim / audio input
    minim = new Minim(this);
    in = minim.getLineIn(Minim.STEREO, BUFFER_SIZE); // TODO Implement selection

    // Set up BeatDetect
    beat = new BeatDetect(in.bufferSize(), in.sampleRate());
    new BeatListener(beat, in);

    // Set up FFT
    fft = new FFT(in.bufferSize(), in.sampleRate());
    new FFTListener(fft, in);

    // Set up Serial
    myPort = new Serial(this, Serial.list()[SERIAL_INDEX], BAUD_RATE); // TODO Implement selection

    // Set frame rate of draw()
    frameRate(FRAME_RATE);

    // Import your custom pattern plugin
    pattern = new Pattern(beat, fft);
}

void draw()
{
    pattern.update();
    for(int i = 0; i < pattern.leds.length; i++)
    {
        myPort.write(pattern.leds[i].getRed());
        myPort.write(pattern.leds[i].getGreen());
        myPort.write(pattern.leds[i].getBlue());
    }
}

public void stop() {
    // always close Minim audio classes when you are done with them
    in.close();
    minim.stop();
    // Close serial port so others can use it
    myPort.stop();
    super.stop();
}

boolean displayable() { 
    return VISIBLE; 
}