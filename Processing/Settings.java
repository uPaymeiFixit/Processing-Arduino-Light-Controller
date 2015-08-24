import java.util.prefs.Preferences;

// Singleton class: We don't need this more than once
public class Settings
{
	// Created to keep users from instantiation
	private Settings() {}

	private static String PACKAGE_NAME = "tk.gibbs.ColorOrgan";
	private static Preferences prefs;


	// These preferences can be found in
	// ~/Library/Preferences/com.apple.java.util.prefs.plist


	// This should only be true if this is the first time this program has run
	public static boolean FIRST_RUN = true;

	// Sets the GUI to show/not show
	// In order for this to work, we have to compile using Processing 2.2.1
	// newer versions seem to ignore this.
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

	// This is the location of the last active plugin
	public static String ACTIVE_PLUGIN = "";


	public static void init()
	{
		// This will define a node in which the preferences can be stored
		prefs = Preferences.userRoot().node( PACKAGE_NAME );

		// This will try to read constants from the preferences file, and return
		// defaults if it could not find them.
		FIRST_RUN     =        prefs.getBoolean( "FIRST_RUN    ", FIRST_RUN     );
		VISIBLE       =        prefs.getBoolean( "VISIBLE      ", VISIBLE       );
		BUFFER_SIZE   =        prefs.getInt    ( "BUFFER_SIZE  ", BUFFER_SIZE   );
		FRAME_RATE    =        prefs.getInt    ( "FRAME_RATE   ", FRAME_RATE    );
		BAUD_RATE     =        prefs.getInt    ( "BAUD_RATE    ", BAUD_RATE     );
		NUM_LEDS      =        prefs.getInt    ( "NUM_LEDS     ", NUM_LEDS      );
		BEACON_PERIOD =        prefs.getInt    ( "BEACON_PERIOD", BEACON_PERIOD );
		BEACON_KEY    = (byte) prefs.getInt    ( "BEACON_KEY   ", BEACON_KEY    );
		PLUGINS_PATH  =        prefs.get       ( "PLUGINS_PATH ", PLUGINS_PATH  );
		ACTIVE_PLUGIN =        prefs.get       ( "ACTIVE_PLUGIN", ACTIVE_PLUGIN );

		// This will save all of the constants to the preferences file (just in
		// case they were not there to begin with).
		// saveFIRST_RUN    ( FIRST_RUN     );
		saveVISIBLE      ( VISIBLE       );
		saveBUFFER_SIZE  ( BUFFER_SIZE   );
		saveFRAME_RATE   ( FRAME_RATE    );
		saveBAUD_RATE    ( BAUD_RATE     );
		saveNUM_LEDS     ( NUM_LEDS      );
		saveBEACON_PERIOD( BEACON_PERIOD );
		saveBEACON_KEY   ( BEACON_KEY    );
		// savePLUGINS_PATH ( PLUGINS_PATH  );
		saveACTIVE_PLUGIN( ACTIVE_PLUGIN );


		printSettings();

		if ( FIRST_RUN )
		{
			Settings.saveFIRST_RUN( false );
			new Message( "This is the first time you have run this program." );
		}
	}

	public static void printSettings()
	{
		System.out.println(
			"\n************* PREFS **************\n" +
			"FIRST_RUN:     " + FIRST_RUN     + "\n" +
			"VISIBLE:       " + VISIBLE       + "\n" +
			"BUFFER_SIZE:   " + BUFFER_SIZE   + "\n" +
			"FRAME_RATE:    " + FRAME_RATE    + "\n" +
			"BAUD_RATE:     " + BAUD_RATE     + "\n" +
			"NUM_LEDS:      " + NUM_LEDS      + "\n" +
			"BEACON_PERIOD: " + BEACON_PERIOD + "\n" +
			"BEACON_KEY:    " + BEACON_KEY    + "\n" +
			"PLUGINS_PATH:  " + PLUGINS_PATH  + "\n" +
			"ACTIVE_PLUGIN: " + ACTIVE_PLUGIN + "\n" +
			"**********************************\n"
		);
	}

	public static void saveFIRST_RUN    ( boolean _FIRST_RUN     ) { FIRST_RUN     = _FIRST_RUN    ; prefs.putBoolean( "FIRST_RUN    ", FIRST_RUN     ); }
	public static void saveVISIBLE      ( boolean _VISIBLE       ) { VISIBLE       = _VISIBLE      ; prefs.putBoolean( "VISIBLE      ", VISIBLE       ); }
	public static void saveBUFFER_SIZE  ( int     _BUFFER_SIZE   ) { BUFFER_SIZE   = _BUFFER_SIZE  ; prefs.putInt    ( "BUFFER_SIZE  ", BUFFER_SIZE   ); }
	public static void saveFRAME_RATE   ( int     _FRAME_RATE    ) { FRAME_RATE    = _FRAME_RATE   ; prefs.putInt    ( "FRAME_RATE   ", FRAME_RATE    ); }
	public static void saveBAUD_RATE    ( int     _BAUD_RATE     ) { BAUD_RATE     = _BAUD_RATE    ; prefs.putInt    ( "BAUD_RATE    ", BAUD_RATE     ); }
	public static void saveNUM_LEDS     ( int     _NUM_LEDS      ) { NUM_LEDS      = _NUM_LEDS     ; prefs.putInt    ( "NUM_LEDS     ", NUM_LEDS      ); }
	public static void saveBEACON_PERIOD( int     _BEACON_PERIOD ) { BEACON_PERIOD = _BEACON_PERIOD; prefs.putInt    ( "BEACON_PERIOD", BEACON_PERIOD ); }
	public static void saveBEACON_KEY   ( byte    _BEACON_KEY    ) { BEACON_KEY    = _BEACON_KEY   ; prefs.putInt    ( "BEACON_KEY   ", BEACON_KEY    ); }
	public static void savePLUGINS_PATH ( String  _PLUGINS_PATH  ) { PLUGINS_PATH  = _PLUGINS_PATH ; prefs.put       ( "PLUGINS_PATH ", PLUGINS_PATH  ); }
	public static void saveACTIVE_PLUGIN( String  _ACTIVE_PLUGIN ) { ACTIVE_PLUGIN = _ACTIVE_PLUGIN; prefs.put       ( "ACTIVE_PLUGIN", ACTIVE_PLUGIN ); }

}
