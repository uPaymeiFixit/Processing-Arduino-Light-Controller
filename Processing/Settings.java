import java.io.File;
import java.util.prefs.Preferences;
import javax.swing.JFileChooser;

// Singleton class: We don't need this more than once
public class Settings
{
	private static String PACKAGE_NAME = "tk.gibbs.ColorOrgan";
	private static Preferences prefs;


	// These preferences can be found in
	// ~/Library/Preferences/com.apple.java.util.prefs.plist

	// Sets the GUI to show/not show
	public static boolean VISIBLE = false;

	// Size of the audio input buffer
	public static int BUFFER_SIZE = 2048;

	// Speed of draw() function (units: per second)
	public static int FRAME_RATE = 30;

	// Serial baud rate. Should match desktop program.
	public static int BAUD_RATE = 115200;

	// Number of controllable LED segments on the strip (note, this needs to
	// match NUM_LEDS in Arduino.ino);
	public static int NUM_LEDS = 17;

	// Time in between beacons (units: milliseconds)
	public static int BEACON_PERIOD = 500;

	// Unique byte that the desktop program should recognize
	public static byte BEACON_KEY = 42;

	// Default path for the plugins (Documens/Light_Controller/Plugins)
	public static String PLUGINS_PATH = "";

	public static String LAST_PLUGIN = "";


	// Created to keep users from instantiation
	// Only Settings will be able to instantiate this class
	private Settings()
	{

		// TODO: add settings.updateBaudRate() etc and then remove anything that
		// matches "= Settings.*";
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
			// TODO: Don't overwrite files
			// TODO: Check if the directory exists every launch, if it doesn't,
			// make it but don't consider it the first run. (Sometimes users
			// will delete the directory on accident).
			new Message("This is the first time the program has run.");

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
				docs += File.separator + "Documents" + File.separator;
			}

			File main = FileHandler.getFile( docs + File.separator +
										"Light_Controller" + File.separator );
			if ( !main.exists() )
			{
				// Without the application folder, there's no point in running
				System.exit(1);
			}

			FileHandler.writeDemos( main.getPath() + File.separator +
												   "Plugins" + File.separator );
			PLUGINS_PATH = main.getPath() + File.separator +
												    "Plugins" + File.separator;
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

	private static void printSettings()
	{
		System.out.println( "VISIBLE: " + VISIBLE );
		System.out.println( "BUFFER_SIZE: " + BUFFER_SIZE );
		System.out.println( "FRAME_RATE: " + FRAME_RATE );
		System.out.println( "BAUD_RATE: " + BAUD_RATE );
		System.out.println( "NUM_LEDS: " + NUM_LEDS );
		System.out.println( "BEACON_PERIOD: " + BEACON_PERIOD );
		System.out.println( "BEACON_KEY: " + BEACON_KEY );
		System.out.println( "PLUGINS_PATH: " + PLUGINS_PATH );
		System.out.println("");
	}

	public static void save( String key, String value )
	{
		prefs.put( key, value );
	}

	public static void saveBoolean( String key, boolean value )
	{
		prefs.putBoolean( key, value );
	}

	public static void saveByteArray( String key, byte[] value )
	{
		prefs.putByteArray( key, value );
	}

	public static void saveDouble( String key, double value )
	{
		prefs.putDouble( key, value );
	}

	public static void saveFloat( String key, float value )
	{
		prefs.putFloat( key, value );
	}

	public static void saveInt( String key, int value )
	{
		prefs.putInt( key, value );
	}

	public static void saveLong( String key, long value )
	{
		prefs.putLong( key, value );
	}

}
