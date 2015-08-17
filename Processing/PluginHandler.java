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
    private String plugins_directory;

    public PluginHandler(String plugins_directory, BeatDetect beat, FFT fft)
    {
        final int NUM_LEDS = 17; // TODO: User definable

        this.plugins_directory = plugins_directory;
        this.beat = beat;
        this.fft = fft;

        // Initialize the leds array
        leds = new byte[NUM_LEDS][3];

        // Zero-out the array in case the plugin dev does something stupid
        for(int i = 0; i < NUM_LEDS; i++)
        {
            leds[i][0] = 0;
            leds[i][1] = 0;
            leds[i][2] = 0;
        }
    }

    public void update()
    {
        if ( invocable_engine != null )
        {
            try
            {
                // invoke the global function named "update"
                invocable_engine.invokeFunction("update");
            }
            catch (ScriptException e)       {e.printStackTrace();}
            catch (NoSuchMethodException e) {e.printStackTrace();}
        }
    }

    public void load(String plugin_name)
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
            String file_location = plugins_directory + plugin_name + ".js";
            // evaluate JavaScript code from given file
            engine.eval(new FileReader(file_location));
        }
        catch (ScriptException e)       {e.printStackTrace();}
        catch (FileNotFoundException e) {e.printStackTrace();}

        // cast the engine to an invocable object for use later
        invocable_engine = (Invocable) engine;
    }

}
