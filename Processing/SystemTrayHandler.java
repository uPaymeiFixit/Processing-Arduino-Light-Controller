import ddf.minim.AudioInput;
import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.Mixer;

public class SystemTrayHandler
{
	private PluginHandler plugin;
	private AudioInput in;
	private SelectInput select_input;
	private String plugins_directory;

	public SystemTrayHandler(PluginHandler plugin,
							 String plugins_directory,
							 SelectInput select_input,
							 AudioInput in)
	{
		this.plugin = plugin;
		this.in = in;
		this.select_input = select_input;
		this.plugins_directory = plugins_directory;

		if ( SystemTray.isSupported() )
		{
			final PopupMenu popup = new PopupMenu();
			// TODO: change this to a non-local reference
			final Image image = Toolkit.getDefaultToolkit().getImage( "/Users/Josh/github/Processing-Arduino-Light-Organ/Processing/icon.gif" );
			final TrayIcon trayIcon = new TrayIcon( image, "Light Organ" );
			final SystemTray tray = SystemTray.getSystemTray();

			Menu plugins_menu = new Menu( "Plugins" );
			popup.add( plugins_menu );
				addPlugins( plugins_menu );

			popup.addSeparator();

			Menu audio_input_menu = new Menu( "Audio Input" );
			popup.add( audio_input_menu );
				addAudioInputs( audio_input_menu );

			Menu settings = new Menu( "Settings " );
			popup.add( settings );
				addSettings( settings );

			popup.addSeparator();

			MenuItem exit = new MenuItem( "Exit" );
			exit.addActionListener( new ActionListener()
			{
				@Override
			    public void actionPerformed( ActionEvent e )
			    {
					// Exits the program
					System.exit(0);
			    }
			});
			popup.add( exit );

			trayIcon.setPopupMenu( popup );

			try
			{
				tray.add( trayIcon );
			}
			catch ( AWTException e ) { e.printStackTrace(); }

		}
		else
		{
			System.out.println( "Tray is not supported" );
		}
	}

	void addPlugins( Menu menu )
	{
		MenuItem open_plugins_folder = new MenuItem( "Open Plugins Folder..." );
		open_plugins_folder.addActionListener( new ActionListener()
		{
			@Override
			public void actionPerformed( ActionEvent event )
			{
				try
				{
				// Open the Plugins folder
				Desktop.getDesktop().open( new File( plugins_directory ) );
				}
				catch ( IOException e ) { e.printStackTrace(); }
			}
		});
		menu.add( open_plugins_folder );

		menu.addSeparator();

		// Set up a group for all of the radio items to go in
		RadioMenuItemGroup plugin_group = new RadioMenuItemGroup();

		// Recursively search for and add plugins
		searchForPlugins( menu, new File( plugins_directory ), plugin_group );
	}

	// Recursively searches for files and directorys and places them in the menu
	void searchForPlugins( Menu menu, File directory, RadioMenuItemGroup group )
	{
		File[] files = directory.listFiles();
        for ( int i = 0; i < files.length; i++ )
        {
			String file = files[i].getName();
			// If the "file" name is less than 3, it cannot be a .js file.
			if ( files[i].isFile() && file.length() >= 3 )
			{
				// If the "file" is a file and it's name ends with .js, load it
	            if ( file.substring( file.length()-3).equals( ".js" ) )
	            {
	                // Gets the name of the file and takes the extension off the end
	                String file_name =  file.split( "\\.", 2 )[0];
					final String file_location = files[i].getPath();
	                // Create a radio item based on this name and set the group
	                final RadioMenuItem radio = new RadioMenuItem( file_name, group );

	                // When this item is clicked, we will load that plugin
					radio.addItemListener( new ItemListener()
		            {
		                @Override
		                public void itemStateChanged( ItemEvent e )
		                {
		                    if ( e.getStateChange() == ItemEvent.SELECTED )
							{
		                    	// Normally we would pass plugin in as a parameter
								// but then we would be referring to a non-final
								// variable inside an inner class defined in a
								// different method. Throws errors and stuff.
								plugin.load( file_location );
							}
							else if ( radio.getRadioMenuItemGroup().getSelectedRadioMenuItem() == null )
							{
								plugin.unload();
							}
		                }
		            });
	                menu.add( radio );
	            }
			}
            else if ( files[i].isDirectory() )
            {
                // Makes a menu out of the directory name
                Menu new_menu = new Menu( files[i].getName()+"..." );
                menu.add( new_menu );
                searchForPlugins( new_menu, files[i], group );
            }
        }
	}

	void addAudioInputs( Menu menu )
	{
		Mixer.Info[] m = select_input.getInputs();
		RadioMenuItemGroup input_group = new RadioMenuItemGroup();
		for (int i = 0; i < select_input.getInputs().length; i++)
		{
			final int index = i;
			RadioMenuItem audio_option = new RadioMenuItem( select_input.getInputs()[i].getName(), input_group );
			audio_option.addItemListener( new ItemListener()
			{
				@Override
				public void itemStateChanged( ItemEvent e )
				{
					if ( e.getStateChange() == ItemEvent.SELECTED )
					{
						// TODO: FIX THIS IN THE MORNING - needs to be Final
						in = select_input.setInput( select_input.getInputs()[index] );
					}
				}
			});

			menu.add( audio_option );
		}
	}

	void addSettings( Menu menu )
	{
		MenuItem set_led_count = new MenuItem( "Set LED Count..." );
		menu.add( set_led_count );

		Menu set_chipset = new Menu( "LED Chipset" );
		menu.add( set_chipset );
		addChipsets( set_chipset );
	}

	void addChipsets( Menu menu )
	{
		RadioMenuItemGroup chipset_group = new RadioMenuItemGroup();
		RadioMenuItem spi_595 = new RadioMenuItem( "595", chipset_group );
		menu.add( spi_595 );
		RadioMenuItem spi_HL1606 = new RadioMenuItem( "HL1606", chipset_group );
		menu.add( spi_HL1606 );
		RadioMenuItem spi_LPD6803 = new RadioMenuItem( "LPD6803", chipset_group );
		menu.add( spi_LPD6803 );
		RadioMenuItem spi_WS2801 = new RadioMenuItem( "WS2801", chipset_group );
		menu.add( spi_WS2801 );
		RadioMenuItem spi_TM1809 = new RadioMenuItem( "TM1809", chipset_group );
		menu.add( spi_TM1809 );
	}

}
