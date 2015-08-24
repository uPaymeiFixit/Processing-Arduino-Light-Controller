import processing.core.PApplet;
import processing.serial.Serial;

// Singleton class
public class SerialHandler
{
	public static final byte NAME = 0;
	public static final byte INDEX = 1;
	public static final byte AUTO = 2;

	private Serial serial_port;
	private PApplet applet;
	private byte MODE;
	private String name;
	private int index;


	private SerialHandler()
	{
		MODE = AUTO;
		this.applet = new PApplet();
		detectArduino();
	}

	private static SerialHandler firstInstance = null;
	public static SerialHandler getInstance()
	{
		if ( firstInstance == null )
		{
			synchronized ( SerialHandler.class )
			{
				if ( firstInstance == null )
				{
					firstInstance = new SerialHandler();
				}
			}
		}

		return firstInstance;
	}

	public void setBaudRate( int baud_rate )
	{
		Settings.saveBAUD_RATE( baud_rate );
		refresh();
	}

	public void refresh()
	{
		if ( MODE == INDEX )
		{
			setSerial( index );
		}
		else if ( MODE == NAME )
		{
			setSerial( name );
		}
		else if ( MODE == AUTO )
		{
			detectArduino();
		}
	}

	// Returns a list of all serial devices
	public static String[] list()
	{
		return Serial.list();
	}

	public Serial getSerial()
	{
		return serial_port;
	}

	public Serial getSerial( int index )
	{
		return new Serial( applet, Serial.list()[index], Settings.BAUD_RATE );
	}

	public Serial getSerial( String name )
	{
		return new Serial( applet, name, Settings.BAUD_RATE );
	}

	public void setSerial( int index )
	{
		MODE = INDEX;
		this.index = index;
		// Close the serial port before we change it.
		stop();
		serial_port = getSerial( index );
	}

	public void setSerial( String name )
	{
		MODE = NAME;
		this.name = name;
		// Close the serial port before we change it.
		stop();
		serial_port = getSerial( name );
	}

	// Connects to each serial device and waits for a beacon
	public void detectArduino()
	{
		MODE = AUTO;
		// Close the serial port before we change it.
		stop();
		Serial serial = null;

	    // Run through each Serial device
	    for (int i = 0; i < Serial.list().length; i++)
	    {
	        try
	        {
	            System.out.println( "Trying serial port " + Serial.list()[i] );
				// This throws the exceptin if it can't run
	            serial = new Serial( applet, Serial.list()[i], Settings.BAUD_RATE );

	            System.out.println( "    Connected.\n    Listening for Arduin" +
				 					"o beacon..." );
	            // We will wait for the device to send us information
				try
				{
				    Thread.sleep( Settings.BEACON_PERIOD+1 );
				}
				catch ( InterruptedException e )
				{
				    e.printStackTrace();
				    Thread.currentThread().interrupt();
				}
	            // If the device sends us a matching byte, we found it
	            if ( serial.read() == Settings.BEACON_KEY )
	            {
	                System.out.println( "    \nArduino found on " +
										Serial.list()[i] + '\n' );
	                break;
	            }
	            System.out.println( "    We didn't receive the Arduino beacon.\n" );
				serial.stop();
	        }
	        catch ( RuntimeException e )
	        {
	            System.out.println( "    Could not connect. Maybe it was busy.\n" );
	        }
			serial = null;
	    }

		serial_port = serial;

		if ( serial_port == null )
		{
			// If we've gotten this far, there are no more devices left
			// TODO:- add a message saying maybe Arduino is Open
			// TODO:- in the readme say to quit Arduino Studio
			Message.showWarning( "We couldn't find the Arduino!\nAre you sure it's pl" +
						 "ugged in?\nAre any programs such as the Arduino IDE" +
						 " using it?\nMaybe try changing the baud rate. \nIf " +
						 "you still need help take a look at the following li" +
						 "nk:\nhttps://github.com/processing/processing/wiki/" +
						 "Serial-Issues", "NO_ARDUINO" );
		}
	}

	public void sendLEDs( int[][] leds )
	{
		// This happens when we are setting the baud rate
		if ( serial_port != null )
		{
			for ( int i = 0; i < leds.length; i++ )
	        {
	            serial_port.write( leds[i][0] );
	            serial_port.write( leds[i][1] );
	            serial_port.write( leds[i][2] );
	        }
		}
	}

	public String toString()
	{
		String output = "";
		for ( int i = 0; i < Serial.list().length; i++ )
		{
			output += i + ": " + Serial.list()[i] + "\n";
		}
		return output;
	}

	public void stop()
	{
		// Close the serial port before we change it.
		if ( serial_port != null )
		{
			serial_port.stop();
			serial_port = null;
		}
	}
}
