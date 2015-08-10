import ddf.minim.AudioListener;

// This class adds a listener to Minim's FFT
// class so that FFT.forward() is automatically 
// called whenever a new audio buffer arrives.
class FFTListener implements AudioListener
{
    private FFT fft;
    private AudioInput source;

    FFTListener(FFT fft, AudioInput source)
    {
        this.source = source;
        this.source.addListener(this);
        this.fft = fft;
    }

    void samples(float[] samples)
    {
        fft.forward(source.mix);
    }

    void samples(float[] samplesL, float[] samplesR)
    {
        fft.forward(source.mix);
    }
}