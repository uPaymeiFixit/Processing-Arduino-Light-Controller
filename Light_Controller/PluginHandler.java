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

    public PluginHandler( BeatDetect beat, FFT fft )
    {
        this.beat = beat;
        this.fft = fft;

        instantiateLEDs( Settings.NUM_LEDS );
    }

    public void instantiateLEDs( int num_leds )
    {
        Settings.saveNUM_LEDS( num_leds );

        // Unload the script
        unload();

        // Initialize the leds array
        leds = new int[num_leds][3];

        // Reload the script
        if ( !Settings.ACTIVE_PLUGIN.equals("") )
        {
            System.out.println(Settings.ACTIVE_PLUGIN);
            load( Settings.ACTIVE_PLUGIN );
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
                Message.showWarning(
"It appears as though there may be an error in your script.<br />" +
"Here is the stack trace:<br />",
                                        "ScriptException_DURING_UPDATE", e );
                unload();
                Settings.saveACTIVE_PLUGIN("");
                // TODO: We cannot call this, so after we have unloaded the
                // plugins from here, it will still say that this plugin is
                // checked.
                // SystemTrayHandler.refreshPlugins();
            }
            catch ( NoSuchMethodException e )
            {
                Message.showWarning(
"We can't find the \"update()\" function in yourscript.<br />" +
"Are you sure it's there?<br />" +
"Here is the stack trace:<br />",
                                                "NoSuchMethodException", e );
                unload();
                Settings.saveACTIVE_PLUGIN("");
                // TODO: We cannot call this, so after we have unloaded the
                // plugins from here, it will still say that this plugin is
                // checked.
                // SystemTrayHandler.refreshPlugins();
            }

            return true;
        }
        return false;
    }

    public void resetLeds()
    {
        if ( leds != null )
        {
            for ( int i = 0; i < leds.length; i++ )
            {
                leds[i][0] = 0;
                leds[i][1] = 0;
                leds[i][2] = 0;
            }

            // the main update loop won't send this since this class's update method
            // returs false, so we have to manually send the black leds.
            SerialHandler.sendLEDs( leds );
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
            Message.showWarning(
"It appears as though there may be an error in your script.<br />"+
"Here is the stack trace:<br />",
                                            "ScriptException_DURING_INIT", e );
            return;
        }
        catch (FileNotFoundException e)
        {
            Message.showWarning(
"We couldn't load the plugin! Are you sure the file is still there?<br />"+
"Below is the stack trace for this error.\n",
                                    "FileNotFoundException_SCRIPT_FILE", e );
            return;
        }

        // cast the engine to an invocable object for use later
        invocable_engine = (Invocable) engine;

        Settings.saveACTIVE_PLUGIN( file_location );
    }

}
