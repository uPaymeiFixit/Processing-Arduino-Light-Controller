import ddf.minim.analysis.BeatDetect;
import ddf.minim.analysis.FFT;
import java.io.FileNotFoundException;
import java.io.FileReader;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class PluginHandler
{
    // There is a lot of mixing bytes and ints because Processing 2.2.1 crashes
    // when trying to convert ints from JavaScript to bytes in Java.
    public int[][] leds;
    private Invocable invocable_engine;
    private BeatDetect beat;
    private FFT fft;
    public String loaded_file;

    public PluginHandler( BeatDetect beat, FFT fft )
    {
        this.beat = beat;
        this.fft = fft;

        instantiateLEDs( Settings.getInstance().NUM_LEDS );
    }

    public void instantiateLEDs( int num_leds )
    {
        if ( leds != null )
        {
            // Unload the script
            unload();
        }

        // Initialize the leds array
        leds = new int[num_leds][3];

        if ( loaded_file != null )
        {
            // Reload the script
            load( loaded_file );
        }
    }

    public boolean update()
    {
        if ( invocable_engine != null )
        {
            try
            {
                // invoke the global function named "update"
                invocable_engine.invokeFunction( "update" );
            }
            catch ( ScriptException e )
            {
                new Message( "It appears as though there may be an error in y" +
                             "our script. Here is the stack trace:\n", e );
                e.printStackTrace();
            }
            catch ( NoSuchMethodException e )
            {
                new Message( "We can't find the \"update()\" function in your" +
                             "script. Are you sure it's there?", Message.ERROR );
                e.printStackTrace();
            }

            return true;
        }
        return false;
    }

    public void resetLeds()
    {
        for ( int i = 0; i < leds.length; i++ )
        {
            leds[i][0] = Settings.getInstance().RESTING_RED;
            leds[i][1] = Settings.getInstance().RESTING_GREEN;
            leds[i][2] = Settings.getInstance().RESTING_BLUE;
        }
    }

    public void unload()
    {
        invocable_engine = null;
        resetLeds();
    }

    public void load( String file_location )
    {
        // This keeps previous scripts from interfering
        unload();

        // create a script engine manager
        ScriptEngineManager factory = new ScriptEngineManager();

        // create a script engine
        ScriptEngine engine = factory.getEngineByName( "JavaScript" );

        // expose leds array, beat, and fft as variables to script to be used
        engine.put( "leds", leds );
        engine.put( "FFT", fft );
        engine.put( "BeatDetect", beat );

        try
        {
            // evaluate JavaScript code from given file
            engine.eval( new FileReader( file_location ) );
        }
        catch (ScriptException e)
        {
            new Message( "It appears as though there may be an error in y" +
                         "our script. Here is the stack trace:\n", e );
            e.printStackTrace();
            return;
        }
        catch (FileNotFoundException e)
        {
            new Message( "We couldn't load the plugin! Are you sure the file " +
                         " is still there?" );
            e.printStackTrace();
            return;
        }

        // cast the engine to an invocable object for use later
        invocable_engine = (Invocable) engine;

        loaded_file = file_location;
    }

}
