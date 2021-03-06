import ddf.minim.analysis.FFT;
import ddf.minim.AudioInput;
import ddf.minim.AudioListener;

// This class adds a listener to Minim's FFT class so that FFT.forward()
// is automatically called whenever a new audio buffer arrives.
public class FFTListener implements AudioListener
{
    private FFT fft;
    private AudioInput source;

    public FFTListener( FFT fft, AudioInput source )
    {
        this.source = source;
        this.source.addListener( this );
        this.fft = fft;
    }

    // Whenever the AudioInput has buffers, this will be called
    public void samples( float[] samples )
    {
        fft.forward( source.mix );
    }

    // or this one. Depending on the channel.
    public void samples( float[] samples_left, float[] samples_right )
    {
        fft.forward( source.mix );
    }
}
