import ddf.minim.AudioInput;
import ddf.minim.Minim;
import ddf.minim.analysis.BeatDetect;
import ddf.minim.analysis.FFT;
import javax.sound.sampled.Mixer;
import processing.serial.Serial;

// Sets the GUI to show/not show
static final boolean VISIBLE = false;

// Size of the audio input buffer
static final int BUFFER_SIZE = 2048;

// Speed of draw() function (units: per second)
static final int FRAME_RATE = 30;

// Time in between beacons
static final int BEACON_PERIOD = 500;

// Unique byte that the desktop program should recognize
static final byte BEACON_KEY = 42;

// Serial baud rate. Should match desktop program.
static final int BAUD_RATE = 115200;

Minim minim;
AudioInput in;
SelectInput si;
BeatDetect beat;
FFT fft;
Serial serial_port;
PluginHandler plugin;

void setup()
{
    // Set up minim / audio input
    minim = new Minim(this);
    autodetectInput();

    // Set up BeatDetect
    beat = new BeatDetect(in.bufferSize(), in.sampleRate());
    new BeatListener(beat, in);

    // Set up FFT
    fft = new FFT(in.bufferSize(), in.sampleRate());
    new FFTListener(fft, in);

    // Set up Serial
    autodetectSerial();

    // Set frame rate of draw()
    frameRate(FRAME_RATE);

    // In Java this is the code we would use for the working_directory, but
    // in Processing this does not work
    //   String working_directory = MyClassName.class.getResource("").getPath();
    // So we have to use this instead:
    String sketch_location = sketchPath("")+"Plugins/";

    String pluginName = "Rainbow";

    // Import a custom pattern plugin
    plugin = new PluginHandler(sketch_location, pluginName, beat, fft);
}

// Trys to find and set the Soundflower (2ch) input
void autodetectInput()
{
    si = new SelectInput(Minim.STEREO, BUFFER_SIZE);
    Mixer.Info[] m = si.getInputs();

    // First we will set a default index, in case we don't find it.
    int current_input = 0;

    // Next we will search for the input with the matching name.
    for (int i = 0; i < si.getInputs().length; i++)
        if(si.getInputs()[i].getName().equals("Soundflower (2ch)"))
            current_input = i;

    // Now we set the input. If soundflower was found it should be set.
    in = si.setInput(si.getInputs()[current_input]);
}

// Finds the correct serial device to connect to
void autodetectSerial()
{
    // Run through each Serial device
    for(int i = 0; i < Serial.list().length; i++)
    {
        boolean passed = true;
        try
        {
            serial_port = new Serial(this, Serial.list()[i], BAUD_RATE);
        }
        catch (Exception e)
        {
            passed = false;
        }
        // If we were able to establish a connection without exception...
        if(passed)
        {
            // We will wait for the device to send us information
            delay(BEACON_PERIOD+1);
            // If the device sends us a matching byte, we found it
            if (serial_port.read() == BEACON_KEY)
                return;
        }
    }
}

void draw()
{
    // We will let the plugin update the leds array
    plugin.update();

    // After that we will send the array to the Arduino
    // The format of the expected data is as follows:
    //    [red0, green0, blue0, red1, green1, blue1, (...), redn, greenn, bluen]
    for(int i = 0; i < plugin.leds.length; i++)
    {
        serial_port.write(plugin.leds[i][0]);
        serial_port.write(plugin.leds[i][1]);
        serial_port.write(plugin.leds[i][2]);
    }
}

public void stop() {
    // always close Minim audio classes when you are done with them
    in.close();
    minim.stop();
    // Close serial port so others can use it
    serial_port.stop();
    super.stop();
}

boolean displayable() {
    return VISIBLE;
}