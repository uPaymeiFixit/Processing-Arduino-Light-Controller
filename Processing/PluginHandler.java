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
    private int NUM_LEDS = 17; // TODO: User definabel

    public PluginHandler(BeatDetect beat, FFT fft)
    {
        this.beat = beat;
        this.fft = fft;

        // Initialize the leds array
        leds = new byte[NUM_LEDS][3];

        // Zero-out the array in case the plugin dev does something stupid
        resetLeds();
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
        for(int i = 0; i < NUM_LEDS; i++)
        {
            leds[i][0] = 0;
            leds[i][1] = 0;
            leds[i][2] = 0;
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
    }

}
