import ddf.minim.AudioInput;
import ddf.minim.Minim;
import ddf.minim.analysis.BeatDetect;
import ddf.minim.analysis.FFT;
import javax.sound.sampled.Mixer;
import processing.serial.Serial;

// TODO: BUG:
// When minim.setInputMixer() is called, it seems that the AudioInput only
// generates buffers SOMETIMES. Generally, if it starts generating buffers at
// the beginning, it will continue for the rest of the operation. But if it
// doesn't, it's not going to randomly start. I've spent way too much time
// on trying to fix this, so for now I'm removing this feature (ability to
// select the input device.)
// A user-workaround for this is to set the default input to whatever you want
// it to be, start the program, and then change it back to whatever it was
// before.
static final boolean MIXER_BUG = false;

// Sets the GUI to show/not show
static final boolean VISIBLE = false;

// Size of the audio input buffer
static final int BUFFER_SIZE = 2048;

// Speed of draw() function (units: per second)
static final int FRAME_RATE = 30;


// TODO: Check to see if Leonardo doesn't support this baud rate
// TODO: maybe add a setting to set the baud rate OR arduino board

Minim minim;          // Needs global for stop() and setup()
AudioInput in;        // Needs global for stop() and setup()
SerialHandler serial; // Needs global for draw() and setup()
PluginHandler plugin; // Needs global for draw() and setup()
SelectInput select_input;
BeatDetect beat;
void setup()
{
    // Set up minim / audio input
    minim = new Minim( this );
    /*SelectInput*/ select_input = MIXER_BUG ? null : new SelectInput( Minim.STEREO, BUFFER_SIZE );
    if ( MIXER_BUG ) in = minim.getLineIn( Minim.STEREO, BUFFER_SIZE );
    else in = select_input.setInput( "Soundflower (2ch)" );

    // Set up BeatDetect
    /*BeatDetect*/ beat = new BeatDetect( in.bufferSize(), in.sampleRate() );
    // new BeatListener( beat, in );

    // Set up FFT
    FFT fft = new FFT( in.bufferSize(), in.sampleRate() );
    new FFTListener( fft, in );

    // Set up Serial
    serial = new SerialHandler( this );

    // Set frame rate of draw()
    frameRate( FRAME_RATE );

    // Import a custom pattern plugin
    plugin = new PluginHandler( beat, fft );

    // In Java this is the code we would use for the working_directory, but
    // in Processing this does not work
    //   String working_directory = MyClassName.class.getResource("").getPath();
    // So we have to use this instead:
    // TODO: This folder isn't here when you export the program.
    String working_directory = sketchPath("");

    // Load the system tray
    new SystemTrayHandler( plugin, working_directory, select_input, in );

    // TODO changeme
    plugin.load( working_directory+"Plugins/Kick Detect.js" );
}

void draw()
{
    // We will let the plugin update the leds array
    if ( plugin.update() )
    {
        // TODO: Should this be here?
        beat.detect(in.mix);
        // After that we will send the array to the Arduino
        // The format of the expected data is as follows:
        //    [red0, green0, blue0, red1, green1, blue1, (...), redn, greenn, bluen]
        serial.sendLEDs( plugin.leds );
    }
}

public void stop() {
    System.out.println("The system is halting!"); // This never gets printed
    // always close Minim audio classes when you are done with them
    in.close();
    minim.stop();
    // Close serial port so others can use it
    serial.stop();
    super.stop();
}

boolean displayable() {
    return VISIBLE;
}

void keyPressed()
{
  switch( key )
  {
    case '1':
      select_input.refresh();
      System.out.println("\nREFRESHED");
      break;

    case '2':
      select_input.refresh();
      in.close();
      in = select_input.setInput( "Soundflower (2ch)" );
      System.out.println("\nREFRESHED AND RELOADED");
      break;

    case '3':
      break;

    case '4':
      break;

    case '5':
      break;

    default: break;
  }
}
