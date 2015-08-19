import  java.util.prefs.Preferences;

// Singleton class: We don't need this more than once
public class Settings
{
	private String PACKAGE_NAME = "tk.gibbs.ColorOrgan";
	private Preferences prefs;

	// Sets the GUI to show/not show
	public boolean VISIBLE = false;

	// Size of the audio input buffer
	public int BUFFER_SIZE = 2048;

	// Speed of draw() function (units: per second)
	public int FRAME_RATE = 30;
	public int BAUD_RATE = 115200;
	public int NUM_LEDS = 17;
	public byte RESTING_RED = 0;
	public byte RESTING_GREEN = 0;
	public byte RESTING_BLUE = 0;
	public int BEACON_PERIOD = 500;
	public byte BEACON_KEY = 42;

	// pattern location

	private static Settings firstInstance = null;

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
	}

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
