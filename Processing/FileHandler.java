import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

// Singleton class
public class FileHandler
{
	private FileHandler() {}

	public static void writeDemos( String plugins )
	{
		if ( getFile( plugins ).exists() )
		{
			writeDemo( plugins, "Kick Detect Color.js", Demos.kick_detect_color );
			writeDemo( plugins, "Kick Detect.js", Demos.kick_detect );
			writeDemo( plugins, "Pulse.js", Demos.pulse );
			writeDemo( plugins, "Rainbow.js", Demos.rainbow );

			if ( getFile( plugins + "Solid Colors/" ).exists() )
			{
				writeDemo( plugins + "Solid Colors/", "Pink.js", Demos.pink );
				writeDemo( plugins + "Solid Colors/", "Red.js", Demos.red );
			}
		}
	}

	private static boolean suppress = false;
	private static void writeDemo( String path, String demo_name, String contents )
	{
		PrintWriter writer = null;
		try
		{
			writer = new PrintWriter( path + demo_name );
			writer.print( contents );
		}
		catch ( FileNotFoundException e )
		{
			if ( !suppress )
			{
				// We're only going to show this error once, because chances are
				// if it failed once, it's going to fail over and over again.
				suppress = true;
				Message.showWarning( "We couldn't create the demo " + demo_name + "It" +
							 "'s okay! You can grab all of the demos <a href=\"ht" +
							 "tps://github.com/uPaymeiFixit/Processing-Arduin" +
							 "o-Light-Organ/tree/master/Processing/Plugins\">here</a> a" +
							 "nd place them in your Documents/Light_Controlle" +
							 "r/Plugins/ folder. Below is the stack trace for this error.\n", "FileNotFoundException_CANT_MAKE_DEMO_FILES", e );
			}
		}
		finally
		{
			if ( writer != null )
			{
				writer.close();
			}
		}
	}

	// Returns a file based on the given input, and if the pramater is a
	// directory, it will try to create it. Giving a warning if it cant.
	public static File getFile( String folder_name )
	{
		File file = new File( folder_name );

		if ( !file.exists() )
		{
			try
			{
				file.mkdir();
			}
			catch ( SecurityException e )
			{
				Message.showWarning( "Well this is odd... Somehow we couldn't create " +
							 folder_name + " The program may or may not crash" +
							 " now... Sorry about that. :( Below you will find the stack trace for this error.\n", "SecurityException_CREATE_FOLDER", e );
			}
		}
		return file;
	}

}
