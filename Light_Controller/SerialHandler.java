import processing.core.PApplet;
import processing.serial.Serial;

public class SerialHandler
{
	public static final byte NAME = 0;
	public static final byte INDEX = 1;
	public static final byte AUTO = 2;

	private static Serial serial_port;
	private static PApplet applet;
	private static byte MODE;
	private static String name;
	private static int index;


	public SerialHandler()
	{
		MODE = AUTO;
		this.applet = new PApplet();
		detectArduino();
	}

	public static void setBaudRate( int baud_rate )
	{
		Settings.saveBAUD_RATE( baud_rate );
		refresh();
	}

	public static void refresh()
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

	public static Serial getSerial()
	{
		return serial_port;
	}

	public static Serial getSerial( int index )
	{
		return new Serial( applet, Serial.list()[index], Settings.BAUD_RATE );
	}

	public static Serial getSerial( String name )
	{
		return new Serial( applet, name, Settings.BAUD_RATE );
	}

	public static void setSerial( int _index )
	{
		MODE = INDEX;
		index = _index;
		// Close the serial port before we change it.
		stop();
		serial_port = getSerial( index );
	}

	public static void setSerial( String _name )
	{
		MODE = NAME;
		name = _name;
		// Close the serial port before we change it.
		stop();
		serial_port = getSerial( name );
	}

	// Connects to each serial device and waits for a beacon
	public static void detectArduino()
	{
		MODE = AUTO;
		// Close the serial port before we change it.
		stop();
		Serial serial = null;
		boolean suppress = false;

		while (true)
		{

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
								// TODO: It appears as though this is not working in Windows
								// maybe it is not able to read the data?
		            // If the device sends us a matching byte, we found it
		            if ( serial.read() == Settings.BEACON_KEY )
		            {
		                System.out.println( "    \nArduino found on " +
											Serial.list()[i] + '\n' );
		                serial_port = serial;

						// If we've told the user we would notify them when we
						// found the Arduino:
						if ( suppress )
						{
							new Message(
								"Whatever you did worked! ;)<br>"+
								"We found the Arduino on<br>"+
								Serial.list()[i] );
						}

						return;
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

			// we only want to show this error once
			if ( !suppress )
			{
				suppress = true;
				// If we've gotten this far, there are no more devices left
				Message.showWarning(
"We couldn't find the Arduino!<br /><br />"+
"We'll keep searching for it and let you know if we find it.<br />"+
"In the meantime, here are some things to try:<br />"+
"  - Are you sure it's plugged in?<br />"+
"  - Are any programs such as the Arduino IDE using it?<br />"+
"  - Try changing the baud rate in Arduino.ino and the settings<br />"+
"     to 9600<br /><br />"+
"Make sure you let at least a minute pass before trying something new<br />"+
"If you still need help take a look at <a href=\"https://github.com/"+
"processing/processing/wiki/Serial-Issues\">this</a>.",
															"NO_ARDUINO" );
			}

		}

	}

	public static void sendLEDs( int[][] leds )
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

	public static void stop()
	{
		// Close the serial port before we change it.
		if ( serial_port != null )
		{
			serial_port.stop();
			serial_port = null;
		}
	}
}
