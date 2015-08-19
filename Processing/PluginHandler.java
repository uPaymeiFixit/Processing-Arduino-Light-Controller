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
    public byte[][] leds;
    private Invocable invocable_engine;
    private BeatDetect beat;
    private FFT fft;
    public String loaded_file;

    public PluginHandler(BeatDetect beat, FFT fft)
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
        leds = new byte[num_leds][3];

        if ( loaded_file != null )
        {
            // Reload the script
            load( loaded_file );
        }
    }

    public boolean update()
    {
        // TODO: Find a better solution than calling beat.isKick() every update
        // I haven't looked at the source of BeatDetect yet, but it seems like
        // if the class is left alone long enough, it won't work. So we'll call
        // it here. This isn't a great solution, but it works for now.
        if ( invocable_engine != null )
        {
            try
            {
                // invoke the global function named "update"
                invocable_engine.invokeFunction("update");
            }
            catch (ScriptException e)       {e.printStackTrace();}
            catch (NoSuchMethodException e) {e.printStackTrace();}

            return true;
        }
        else
        {
            return false;
        }
    }

    public void resetLeds()
    {
        for(int i = 0; i < leds.length; i++)
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


    public void load(String file_location)
    {
        // create a script engine manager
        ScriptEngineManager factory = new ScriptEngineManager();

        // create a script engine
        ScriptEngine engine = factory.getEngineByName("JavaScript");

        // expose leds array, beat, and fft as variables to script to be used
        engine.put("leds", leds);
        engine.put("FFT", fft);
        engine.put("BeatDetect", beat);
        // TODO: put a changeIcon method

        try
        {
            // evaluate JavaScript code from given file
            engine.eval(new FileReader(file_location));
        }
        catch (ScriptException e)       {e.printStackTrace();}
        catch (FileNotFoundException e) {e.printStackTrace();}

        // cast the engine to an invocable object for use later
        invocable_engine = (Invocable) engine;

        loaded_file = file_location;
    }

}
