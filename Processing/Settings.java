import java.io.File;
import java.util.prefs.Preferences;
import javax.swing.JFileChooser;

// Singleton class: We don't need this more than once
public class Settings
{
	private String PACKAGE_NAME = "tk.gibbs.ColorOrgan";
	private Preferences prefs;


	// These preferences can be found in
	// ~/Library/Preferences/com.apple.java.util.prefs.plist

	// Sets the GUI to show/not show
	public boolean VISIBLE = false;

	// Size of the audio input buffer
	public int BUFFER_SIZE = 2048;

	// Speed of draw() function (units: per second)
	public int FRAME_RATE = 30;

	// Serial baud rate. Should match desktop program.
	public int BAUD_RATE = 115200;

	// Number of controllable LED segments on the strip (note, this needs to
	// match NUM_LEDS in Arduino.ino);
	public int NUM_LEDS = 17;

	// Default colors for when the script starts up or scripts are unloaded
	public byte RESTING_RED = 0;
	public byte RESTING_GREEN = 0;
	public byte RESTING_BLUE = 0;

	// Time in between beacons (units: milliseconds)
	public int BEACON_PERIOD = 500;

	// Unique byte that the desktop program should recognize
	public byte BEACON_KEY = 42;

	// Default path for the plugins (Documens/Light_Controller/Plugins)
	public String PLUGINS_PATH = "";


	// Created to keep users from instantiation
	// Only Settings will be able to instantiate this class
	private Settings()
	{
		// This will define a node in which the preferences can be stored
		prefs = Preferences.userRoot().node( PACKAGE_NAME );

		VISIBLE = prefs.getBoolean( "VISIBLE", VISIBLE );
		prefs.putBoolean( "VISIBLE", VISIBLE );
		BUFFER_SIZE = prefs.getInt( "BUFFER_SIZE", BUFFER_SIZE );
		prefs.putInt( "BUFFER_SIZE", BUFFER_SIZE );
		FRAME_RATE = prefs.getInt( "FRAME_RATE", FRAME_RATE );
		prefs.putInt( "FRAME_RATE", FRAME_RATE );
		BAUD_RATE = prefs.getInt( "BAUD_RATE", BAUD_RATE );
		prefs.putInt( "BAUD_RATE", BAUD_RATE );
		NUM_LEDS = prefs.getInt( "NUM_LEDS", NUM_LEDS );
		prefs.putInt( "NUM_LEDS", NUM_LEDS );
		RESTING_RED = (byte) prefs.getInt( "RESTING_RED", RESTING_RED );
		prefs.putInt( "RESTING_RED", RESTING_RED );
		RESTING_GREEN = (byte) prefs.getInt( "RESTING_GREEN", RESTING_GREEN );
		prefs.putInt( "RESTING_GREEN", RESTING_GREEN );
		RESTING_BLUE = (byte) prefs.getInt( "RESTING_BLUE ", RESTING_BLUE );
		prefs.putInt( "RESTING_BLUE ", RESTING_BLUE );
		BEACON_PERIOD = prefs.getInt( "BEACON_PERIOD", BEACON_PERIOD );
		prefs.putInt( "BEACON_PERIOD", BEACON_PERIOD );
		BEACON_KEY = (byte) prefs.getInt( "BEACON_KEY", BEACON_KEY );
		prefs.putInt( "BEACON_KEY", BEACON_KEY );
		PLUGINS_PATH = prefs.get( "PLUGINS_PATH", PLUGINS_PATH );
		initializePluginsFolder();
		prefs.put( "PLUGINS_PATH", PLUGINS_PATH );

		printSettings();
	}

	private void initializePluginsFolder()
	{
		// If the plugins path is not set, this is a first run
		if ( PLUGINS_PATH.equals("") )
		{
			System.out.println( "This is the first time this program has run." +
			  					"\nWriting files now." );

			// This method doesn't work perfectly. In unix it will return
			// only the user's home directory. So we add /Documents/ to it.
			// Also has a lot of potential for failure in Windows since
			// Windows seems to have an ever-shifting idea of what the
			// Documents folder should be.
			String os = System.getProperty( "os.name" ).toLowerCase();
			String docs = new JFileChooser().getFileSystemView()
											.getDefaultDirectory().toString();
			if ( !( os.indexOf( "win" ) >= 0 ) )
			{
				docs += "/Documents/";
			}

			File main = FileHandler.getFile( docs + "Light_Controller/" );
			if ( !main.exists() )
			{
				// Without the application folder, there's no point in running
				System.exit(1);
			}

			FileHandler.writeDemos( main.getPath() + "/Plugins/" );
			PLUGINS_PATH = main.getPath() + "/Plugins/";
		}
	}

	private static Settings firstInstance = null;
	public static Settings getInstance()
	{
		if ( firstInstance == null )
		{
			synchronized ( Settings.class )
			{
				if ( firstInstance == null )
				{
					firstInstance = new Settings();
				}
			}
		}
		return firstInstance;
	}

	private void printSettings()
	{
		System.out.println( "VISIBLE: " + VISIBLE );
		System.out.println( "BUFFER_SIZE: " + BUFFER_SIZE );
		System.out.println( "FRAME_RATE: " + FRAME_RATE );
		System.out.println( "BAUD_RATE: " + BAUD_RATE );
		System.out.println( "NUM_LEDS: " + NUM_LEDS );
		System.out.println( "RESTING_RED: " + RESTING_RED );
		System.out.println( "RESTING_GREEN: " + RESTING_GREEN );
		System.out.println( "RESTING_BLUE: " + RESTING_BLUE );
		System.out.println( "BEACON_PERIOD: " + BEACON_PERIOD );
		System.out.println( "BEACON_KEY: " + BEACON_KEY );
		System.out.println( "PLUGINS_PATH: " + PLUGINS_PATH );
		System.out.println("");
	}

	public void save( String key, String value )
	{
		prefs.put( key, value );
	}

	public void saveBoolean( String key, boolean value )
	{
		prefs.putBoolean( key, value );
	}

	public void saveByteArray( String key, byte[] value )
	{
		prefs.putByteArray( key, value );
	}

	public void saveDouble( String key, double value )
	{
		prefs.putDouble( key, value );
	}

	public void saveFloat( String key, float value )
	{
		prefs.putFloat( key, value );
	}

	public void saveInt( String key, int value )
	{
		prefs.putInt( key, value );
	}

	public void saveLong( String key, long value )
	{
		prefs.putLong( key, value );
	}

}
