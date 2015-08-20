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
import java.lang.NumberFormatException;
import javax.swing.JOptionPane;

public class SystemTrayHandler
{
	// TODO: See Processing.pde for explanation
	private static final boolean MIXER_BUG = true;
	private static final boolean FEAT_CHIPSET = false;

	private PluginHandler plugin;
	private AudioInput in;
	private String plugins_directory;

	public SystemTrayHandler(PluginHandler plugin, String working_directory, AudioInput in)
	{
		this.plugin = plugin;
		this.in = in;
		this.plugins_directory = working_directory + "Plugins/";

		// Okay, this one was stupid hacky. I couldn't figure out how to export
		// images with processing, but I noticed that in
		// Processing.app/Contents/Java/ there was a file named jssc.txt.
		// I'm almost 100% certain that Processing isn't using this. So I found
		// the file in /Applications/Processing.app/Contents/Java/modes/java/libraries/serial/library/jssc.txt
		// I took my icon.gif and renamed it to jssc.txt, and it appears to work.
		String icon = SystemTrayHandler.class.getResource("").getPath() + "jssc.txt";

		if ( SystemTray.isSupported() )
		{
			if ( !( new File( icon ) ).exists() )
			{
				String local_icon = working_directory + "icon.gif";
				if ( !( new File( local_icon ) ).exists() )
				{
					new Message( "Could not find icon.gif to put in the Syste" +
					 			 "Tray. We looked in " + icon + " and " +
								 local_icon, Message.ERROR );
					System.exit(1);
				}
				icon = local_icon;
			}

			final PopupMenu popup = new PopupMenu();
			final Image image = Toolkit.getDefaultToolkit()
									.getImage( icon );
			final TrayIcon trayIcon = new TrayIcon( image, "Light Controller" );
			final SystemTray tray = SystemTray.getSystemTray();

			addPluginsMenu( popup );

			popup.addSeparator();

			if(!MIXER_BUG) addAudioMenu( popup );
			addSettingsMenu( popup );

			popup.addSeparator();

			addExitItem( popup );

			trayIcon.setPopupMenu( popup );

			try
			{
				tray.add( trayIcon );
			}
			catch ( AWTException e )
			{
				new Message( "There was a problem creating an icon on the sys" +
							 "tem tray. This program will now exit.",
							 Message.ERROR );
				e.printStackTrace();
				System.exit(1);
			}

		}
		else
		{
			new Message( "Light Controller is not suppored on this machine du" +
			 			 "e to system tray being unsupported.", Message.ERROR );
			System.out.println( "Tray is not supported" );
			System.exit(1);
		}
	}

	void addPluginsMenu( PopupMenu popup )
	{
		Menu plugins_menu = new Menu( "Plugins" );
		popup.add( plugins_menu );
			addPlugins( plugins_menu );
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
					Desktop.getDesktop().open(
							new File( Settings.getInstance().PLUGINS_PATH ) );
				}
				catch ( IOException e )
				{
					new Message( "Could not automatically open the plugins fo" +
								 "lder. Are you sure it exists? It should be " +
								 "at Documents/Light_Controller/Plugins/",
								 Message.WARNING );
					e.printStackTrace();
				}
			}
		});
		menu.add( open_plugins_folder );

		menu.addSeparator();

		// Set up a group for all of the radio items to go in
		RadioMenuItemGroup plugin_group = new RadioMenuItemGroup();

		// Recursively search for and add plugins
		searchForPlugins( menu,
				new File( Settings.getInstance().PLUGINS_PATH ), plugin_group );
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
							else if ( radio.getRadioMenuItemGroup()
											.getSelectedRadioMenuItem() == null )
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

	void addAudioMenu( PopupMenu popup )
	{
		Menu audio_input_menu = new Menu( "Audio Input" );
		popup.add( audio_input_menu );
			addAudioInputs( audio_input_menu );
	}

	void addAudioInputs( Menu menu )
	{
		SelectInput select_input = SelectInput.getInstance();
		RadioMenuItemGroup input_group = new RadioMenuItemGroup();
		for ( int i = 0; i < select_input.getInputs().length; i++ )
		{
			boolean state = false;
			if ( select_input.getInputs()[i].getName().equals( "Soundflower (2ch)" ) )
			{
				state = true;
			}

			final int index = i;
			RadioMenuItem audio_option = new RadioMenuItem(
					select_input.getInputs()[i].getName(), input_group, state );
			audio_option.addItemListener( new ItemListener()
			{
				@Override
				public void itemStateChanged( ItemEvent e )
				{
					if ( e.getStateChange() == ItemEvent.SELECTED )
					{
						new Message( "Switching inputs to " + SelectInput
									 .getInstance().getInputs()[index].getName() +
									 "\nThis is an experimental feature." );
						SelectInput.getInstance().refresh();
						in.close();
						in = SelectInput.getInstance().setInput(
								SelectInput.getInstance().getInputs()[index] );
					}
				}
			});

			menu.add( audio_option );
		}
	}

	void addSettingsMenu( PopupMenu popup )
	{
		Menu settings = new Menu( "Settings " );
		popup.add( settings );
			addSettings( settings );
	}

	void addSettings( Menu menu )
	{
		MenuItem set_led_count = new MenuItem( "Set LED Count..." );
		set_led_count.addActionListener( new ActionListener()
		{
			@Override
			public void actionPerformed( ActionEvent e )
			{
				String input = JOptionPane.showInputDialog(
						"You will need to change NUM_LEDS in Arduion.ion for " +
						"this to work.", "Number of controllable LED segmetns" );
				try
				{
					int number = Integer.parseInt( input );
					if ( number < 1 )
					{
						throw new NumberFormatException();
					}

					Settings.getInstance().saveInt( "NUM_LEDS", number );
					plugin.instantiateLEDs( number );
				}
				catch ( NumberFormatException e2 )
				{
					new Message( "You must enter an integer which is above 0",
								 Message.WARNING );
				}
			}
		});
		menu.add( set_led_count );

		Menu set_chipset = new Menu( "LED Chipset" );
		if(FEAT_CHIPSET) menu.add( set_chipset );
		if(FEAT_CHIPSET) addChipsets( set_chipset );

		MenuItem set_baud = new MenuItem( "Set Baud Rate..." );
		set_baud.addActionListener( new ActionListener()
		{
			@Override
			public void actionPerformed( ActionEvent e )
			{
				String input = JOptionPane.showInputDialog(
						"You will need to change BAUD_RATE in Arduion.ino\n" +
						"for this to work. The current baud rate is " +
						Settings.getInstance().BAUD_RATE, "Serial baud rate" );
				try
				{
					int number = Integer.parseInt( input );
					if ( number < 1 )
					{
						throw new NumberFormatException();
					}

					Settings.getInstance().saveInt( "BAUD_RATE", number );
					SerialHandler.getInstance().setBaudRate( number );
				}
				catch ( NumberFormatException e2 )
				{
					new Message( "You must enter an integer which is above 0",
								 Message.WARNING );
				}
			}
		});
		menu.add( set_baud );
	}

	// TODO: Make chipsets work
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

	void addExitItem( PopupMenu popup )
	{
		MenuItem exit = new MenuItem( "Exit" );
		exit.addActionListener( new ActionListener()
		{
			@Override
			public void actionPerformed( ActionEvent e )
			{
				// Exits the program
				// plugin.resetLeds();
				// TODO: Decide whether or not LEDS should turn off when
				// program exits.
				// 		Pros: You can set a color and then quit the program
				// 		Cons: For LEDS to turn off, user has to unload plugins
				System.exit(0);
			}
		});
		popup.add( exit );
	}

}
