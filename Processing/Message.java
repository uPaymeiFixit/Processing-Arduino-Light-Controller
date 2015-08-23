import javax.swing.JOptionPane;
import java.io.StringWriter;
import java.io.PrintWriter;

import javax.swing.JLabel;
import javax.swing.JEditorPane;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.HyperlinkEvent;
import java.io.IOException;
import java.awt.Desktop;
import java.net.URISyntaxException;
import java.awt.Font;

public class Message
{
	// Icons
	public static final int WARNING     = JOptionPane.WARNING_MESSAGE;
	public static final int ERROR       = JOptionPane.ERROR_MESSAGE;
	public static final int QUESTION    = JOptionPane.QUESTION_MESSAGE;
	public static final int PLAIN       = JOptionPane.PLAIN_MESSAGE;
	public static final int INFORMATION = JOptionPane.INFORMATION_MESSAGE;

	// WARNING - usually recoverable
	// ERROR - usually non-recoverable
	// QUESTION - asking for input
	// INFORMATION - giving information
	// PLAIN - no use yet

	public Message() {}

	public Message( String message )
	{
		showInfo( message );
	}

	public static int getPositiveInt( String message, String filled_in )
	{
		boolean first_fail = false;
		while ( true )
		{
			String input = JOptionPane.showInputDialog( message, filled_in );
			try
			{
				int number = Integer.parseInt( input );
				if ( number < 0 ) throw new NumberFormatException();
				return number;
			}
			catch ( NumberFormatException e )
			{
				if ( !first_fail )
				{
					first_fail = true;
					message = "<b>ERROR: Input must be a positive integer.</b" +
					">\n\n" + message;
				}
			}
		}
	}

	// Displays an informational dialog with the specified message. The message
	// can contain HTML including links and will be wrapped in an html and body
	// tag.
	public static void showInfo( String message )
	{
		String issue_link = "https://github.com/uPaymeiFixit/Processing-Ardui" +
							"no-Light-Controller/issues/new?title=Unexpected%" +
							"20Message&body=%23%23%23%23+What+" +
							"error+message+did+you+get%3F%0D" +
							"%0D%0A%0D%0A%23%23%23%23+Can+you+reproduce+this+" +
							"error%3F+If+so%2C+how%3F%0D%0A%0D%0A%0D%0A%23%23" +
							"%23%23+Any+other+information%3F";

		String html = message+"<br><br>If you think this may be a " +
					  "bug, report it <a href=\""+issue_link+"\">here</a> or " +
					  "contact J@Gibbs.tk";

		showDialog( html, "Light Controller Message", INFORMATION );
	}

	// Shows a warning with the given message, error name, and exception trace
	public static void showWarning( String message, String error_name, Exception e )
	{
		StringWriter sw = new StringWriter();
		e.printStackTrace( new PrintWriter( sw ) );
		showWarning( message + '\n' + sw.toString(), error_name );
	}

	// Shows a warning with the given message and error name
	public static void showWarning( String message, String error_name )
	{
		String issue_link = "https://github.com/uPaymeiFixit/Processing-Ardui" +
							"no-Light-Controller/issues/new?title=Unexpected%" +
							"20"+error_name+"%20Warning&body=%23%23%23%23+What+" +
							"error+message+did+you+get%3F%0D"+error_name +
							"%0D%0A%0D%0A%23%23%23%23+Can+you+reproduce+this+" +
							"error%3F+If+so%2C+how%3F%0D%0A%0D%0A%0D%0A%23%23" +
							"%23%23+Any+other+information%3F";

		String html = message+"<br><br>If you think this may be a " +
					  "bug, report it <a href=\""+issue_link+"\">here</a> or " +
					  "contact J@Gibbs.tk";

		showDialog( html, "Light Controller Warning: " + error_name, WARNING );
	}

	// Shows a warning with the given message, error name, and exception trace
	public static void showError( String message, String error_name, Exception e )
	{
		StringWriter sw = new StringWriter();
		e.printStackTrace( new PrintWriter( sw ) );
		showError( message + '\n' + sw.toString(), error_name );
	}

	// Shows an error dialog with the given message and error name
	public static void showError( String message, String error_name )
	{
		String issue_link = "https://github.com/uPaymeiFixit/Processing-Ardui" +
							"no-Light-Controller/issues/new?title=Unexpected%" +
							"20"+error_name+"%20Error&body=%23%23%23%23+What+" +
							"error+message+did+you+get%3F%0D"+error_name+"%0A" +
							"%0D%0A%0D%0A%23%23%23%23+Can+you+reproduce+this+" +
							"error%3F+If+so%2C+how%3F%0D%0A%0D%0A%0D%0A%23%23" +
							"%23%23+Any+other+information%3F";

		String html = message+"<br><br>If you think this may be a " +
					  "bug, report it <a href=\""+issue_link+"\">here</a> or " +
					  "contact J@Gibbs.tk";

		showDialog( html, "Light Controller Error: " + error_name, ERROR );
	}

	// Shows a dialog with the given HTML, and handles clickable links
	public static void showDialog( String html, String title, int icon )
	{
		// for copying style
		JLabel label = new JLabel();
		Font font = label.getFont();

		// create some css from the label's font
		StringBuffer style = new StringBuffer("font-family:" + font.getFamily() + ";");
		style.append("font-weight:" + (font.isBold() ? "bold" : "normal") + ";");
		style.append("font-size:" + font.getSize() + "pt;");

		JEditorPane ep = new JEditorPane( "text/html", "<html><body style=\"" + style + "\">" + html + "</body></html>" );
		ep.addHyperlinkListener( new HyperlinkListener()
		{
			@Override
			public void hyperlinkUpdate( HyperlinkEvent e )
			{
				if ( e.getEventType().equals( HyperlinkEvent.EventType.ACTIVATED ) )
				{
					try
					{
						Desktop.getDesktop().browse(e.getURL().toURI());
					}
					catch ( URISyntaxException e2 ) { System.out.println(e); }
					catch ( IOException e2 ) { System.out.println(e); }
				}
			}
		});
		ep.setEditable( false );
		ep.setBackground( label.getBackground() );
		JOptionPane.showMessageDialog( null, ep, title, icon );
	}
}
