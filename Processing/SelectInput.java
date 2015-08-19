import ddf.minim.AudioInput;
import ddf.minim.Minim;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;

// Singleton class
// This class takes the same parameters as Minim's getLineIn method
// and tries all of the inputs to see if they are usable.
public class SelectInput
{
    private Minim minim;
    private Mixer mixer;

    private static final  int CHANNEL = Minim.STEREO;
    private static final int BUFFER_SIZE = Settings.getInstance().BUFFER_SIZE;

    private Mixer.Info[] mixer_info;
    private Mixer.Info[] usable_inputs;
    private int usable_inputs_size;

    private static SelectInput firstInstance = null;

    private SelectInput()
    {
        minim = new Minim( this );
        refresh();
    }

    public static SelectInput getInstance()
	{
		if ( firstInstance == null )
		{
			synchronized ( SelectInput.class )
			{
				if ( firstInstance == null )
				{
					firstInstance = new SelectInput();
				}
			}
		}

		return firstInstance;
	}

    public void refresh()
    {
        usable_inputs_size = 0;
        mixer_info = AudioSystem.getMixerInfo();
        Mixer.Info[] usable_inputs_temp = new Mixer.Info[mixer_info.length];

        for( int i = mixer_info.length; i-- > 0; )
        {
            // Set the i-th input and try getting data from it.
            mixer = AudioSystem.getMixer( mixer_info[i] );
            minim.setInputMixer( mixer );
            // If it's null, we cannot use it
            if ( minim.getLineIn( CHANNEL, BUFFER_SIZE ) != null )
            {
                // Otherwise we can
                usable_inputs_temp[usable_inputs_size++] = mixer_info[i];
            }
        }

        usable_inputs = new Mixer.Info[usable_inputs_size];
        System.arraycopy( usable_inputs_temp, 0, usable_inputs, 0, usable_inputs_size );
    }

    // Returns an array of the inputs that can actually have sound data
    //  pulled from them.
    public Mixer.Info[] getInputs()
    {
        return usable_inputs;
    }

    public String toString()
    {
        String output = "[";
		for ( int i = 0; i < usable_inputs.length; i++ )
			output += usable_inputs[i].getName() + ", ";
		return output.substring( 0, output.length() - 2 )+"]";

    }

    // Returns an AudioInput device based on the mixers
    public AudioInput setInput( Mixer.Info mixer_info_element )
    {
        mixer = AudioSystem.getMixer( mixer_info_element );
        minim.setInputMixer( mixer );
        return minim.getLineIn( CHANNEL, BUFFER_SIZE );
    }

    // Returns an AudioInput based on the String name
    public AudioInput setInput( String name )
    {
        for ( int i = 0; i < usable_inputs.length; i++ )
        {
            if ( usable_inputs[i].getName().equals( name ) )
            {
                return setInput( usable_inputs[i] );
            }
        }

        return setInput();
    }

    // Returns an AudioInput based on the specified index
    public AudioInput setInput( int index )
    {
        return setInput( usable_inputs[index] );
    }

    // Returns the first (usually default) audio input
    public AudioInput setInput()
    {
        return setInput(0);
    }
}
