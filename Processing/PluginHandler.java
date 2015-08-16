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

    public PluginHandler(String sketch_location, String plugin_name, BeatDetect beat, FFT fft)
    {
        final int NUM_LEDS = 17; // TODO User definable

        loadPlugin(sketch_location, plugin_name, beat, fft, NUM_LEDS);
    }

    public void update()
    {
        try
        {
            // invoke the global function named "update"
            invocable_engine.invokeFunction("update");
        }
        catch (ScriptException e)       {e.printStackTrace();}
        catch (NoSuchMethodException e) {e.printStackTrace();}
    }

    private void loadPlugin(String sketch_location, String plugin_name, BeatDetect beat, FFT fft, int num_leds)
    {
        // create a script engine manager
        ScriptEngineManager factory = new ScriptEngineManager();

        // create a script engine
        ScriptEngine engine = factory.getEngineByName("JavaScript");

        // Initialize our array
        leds = new byte[num_leds][3];

        // zero-out the array in case the plugin dev does something stupid
        for(int i = 0; i < num_leds; i++)
        {
            leds[i][0] = 0;
            leds[i][1] = 0;
            leds[i][2] = 0;
        }

        // expose leds array, beat, and fft as variables to script to be used
        engine.put("leds", leds);
        engine.put("FFT", fft);
        engine.put("BeatDetect", beat);

        try
        {
            String file_location = sketch_location + plugin_name + ".js";
            // evaluate JavaScript code from given file
            engine.eval(new FileReader(file_location));
        }
        catch (ScriptException e)       {e.printStackTrace();}
        catch (FileNotFoundException e) {e.printStackTrace();}

        // cast the engine to an invocable object for use later
        invocable_engine = (Invocable) engine;
    }

}
