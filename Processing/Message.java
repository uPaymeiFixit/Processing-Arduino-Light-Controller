import javax.swing.JOptionPane;
import java.io.StringWriter;
import java.io.PrintWriter;

public class Message
{
	// Icons
	public static final int WARNING     = JOptionPane.WARNING_MESSAGE;
	public static final int ERROR       = JOptionPane.ERROR_MESSAGE;
	public static final int QUESTION    = JOptionPane.QUESTION_MESSAGE;
	public static final int PLAIN       = JOptionPane.PLAIN_MESSAGE;
	public static final int INFORMATION = JOptionPane.INFORMATION_MESSAGE;

	private static final String default_title = "Light Controller Message";

	public Message( String message )
	{
		this( message, default_title );
	}

	public Message( String message, String title )
	{
		this( message, title, WARNING );
	}

	public Message( String message, int icon )
	{
		this( message, default_title, icon );
	}

	public Message( String message, Exception e )
	{
		StringWriter sw = new StringWriter();
		e.printStackTrace( new PrintWriter( sw ) );
		JOptionPane.showMessageDialog( null, message + "\n" + sw.toString(),
		 							   default_title, ERROR );
	}

	public Message( String message, String title, int icon )
	{
		JOptionPane.showMessageDialog( null, message, title, icon );
	}
}
