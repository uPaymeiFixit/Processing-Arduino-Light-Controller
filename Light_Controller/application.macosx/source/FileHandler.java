import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import javax.swing.JFileChooser;

public class FileHandler
{
	private FileHandler() {}

	public static void startupChecks()
	{

		// If the plugins path is not set, we will set it
		if ( Settings.PLUGINS_PATH.equals("") )
		{
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

			Settings.savePLUGINS_PATH( docs + "Light Controller" + File.separator + "Plugins" + File.separator );
		}


		// Initialize files if they don't exist
		File plugins = new File ( Settings.PLUGINS_PATH );

		// We want to check if this directory has been created
		if ( !plugins.exists() )
		{
			// This will attempt to create the directory
			plugins = getFile( Settings.PLUGINS_PATH );

			// Lets check again if it exists
			if ( !plugins.exists() )
			{
				// Something went wrong and we can't load the path
				System.exit(1);
			}

			writeDemos( Settings.PLUGINS_PATH );
		}

	}


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
				Message.showWarning(
"We couldn't create the demo " + demo_name + " It's okay!<br />"+
"You can grab all of the demos <a href=\"https://github.com/uPaymeiFixit/"+
"Processing-Arduino-Light-Organ/tree/master/Processing/Plugins\">here</a> " +
"and place<br />them in your Documents/Light Controller/Plugins/ folder.<br /><br />"+
"Below is the stack trace for this error.<br />",
							"FileNotFoundException_CANT_MAKE_DEMO_FILES", e );
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
		// Convert string path to file
		File file = new File( folder_name );

		// If the file doesn't exist
		if ( !file.exists() )
		{
			// Check if the parent exists, because we can't create a directory
			// inside a null directory
			if ( !file.getParentFile().exists() )
			{
				// Recursively make directories up the chain until we hit one
				// that already exists
				getFile( file.getParent() );
			}
			try
			{
				file.mkdir();
			}
			catch ( SecurityException e )
			{
				Message.showWarning(
"Well this is odd... Somehow we couldn't create<br />" +
folder_name +
"<br />The program may or may not crash now.<br />" +
"Below you will find the stack trace for this error.<br />",
										"SecurityException_CREATE_FOLDER", e );
			}
		}
		return file;
	}

}
