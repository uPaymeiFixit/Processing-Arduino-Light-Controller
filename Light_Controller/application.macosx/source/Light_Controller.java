import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import ddf.minim.AudioInput; 
import ddf.minim.Minim; 
import ddf.minim.analysis.BeatDetect; 
import ddf.minim.analysis.FFT; 
import processing.serial.Serial; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Light_Controller extends PApplet {






// This is needed because Processing throws a fit if it's not here.


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
private static final boolean MIXER_BUG = true;

// In order for there to be no dock icon in Mac, I had to add these lines to
// /Applications/Processing.app/Contents/Java/modes/java/application/Info.plist.tmpl
//      <key>LSUIElement</key>
//          <true/>

Minim minim;          // Needs global for stop() and setup()
AudioInput in;        // Needs global for stop() and setup()
SerialHandler serial; // Needs global for draw() and setup()
PluginHandler plugin; // Needs global for draw() and setup()

public void setup()
{
    Settings.init();
    FileHandler.startupChecks();

    // Set up minim / audio input
    minim = new Minim( this );
    if(!MIXER_BUG) in = SelectInput.getInstance().setInput( "Soundflower (2ch)" );
    if (MIXER_BUG) in = minim.getLineIn( Minim.STEREO,
                                         Settings.BUFFER_SIZE );

    // Set up BeatDetect
    BeatDetect beat = new BeatDetect( in.bufferSize(), in.sampleRate() );
    new BeatListener( beat, in );

    // Set up FFT
    FFT fft = new FFT( in.bufferSize(), in.sampleRate() );
    new FFTListener( fft, in );

    // Import a custom pattern plugin
    plugin = new PluginHandler( beat, fft );

    // In Java this is the code we would use for the working_directory, but
    // in Processing this does not work
    //   String working_directory = MyClassName.class.getResource("").getPath();
    // So we have to use this instead:
    // TODO: This folder isn't here when you export the program.
    String working_directory = sketchPath("");

    // Load the system tray
    new SystemTrayHandler( plugin, working_directory, in );

    // Set up Serial
    serial = new SerialHandler();

    // Set frame rate of draw()
    frameRate( Settings.FRAME_RATE );

}

public void draw()
{
    // We will let the plugin update the leds array
    if ( plugin.update() )
    {
        // TODO: Should this be here?
        // beat.detect(in.mix);

        // After that we will send the array to the Arduino
        // The format of the expected data is as follows:
        //[red0, green0, blue0, red1, green1, blue1, (...), redN, greenN, blueN]
        serial.sendLEDs( plugin.leds );
    }
}

public void stop() {
    // always close Minim audio classes when you are done with them
    in.close();
    minim.stop();
    // Close serial port so others can use it
    serial.stop();
    super.stop();
}

public boolean displayable() {
    return Settings.VISIBLE;
}

// For debug stuff only.
public void keyPressed()
{
  switch( key )
  {
    case '2':
      SelectInput.getInstance().refresh();
      in.close();
      in = SelectInput.getInstance().setInput( "Soundflower (2ch)" );
      System.out.println("\nREFRESHED AND RELOADED");
      break;

    default: break;
  }
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Light_Controller" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
