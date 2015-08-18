import ddf.minim.analysis.BeatDetect;
import ddf.minim.AudioInput;
import ddf.minim.AudioListener;

// This class adds a listener to Minim's BeatDetect
// class so that BeatDetect.detect() is automatically
// called whenever a new audio buffer arrives.
public class BeatListener implements AudioListener
{
    private BeatDetect beat;
    private AudioInput source;

    public BeatListener(BeatDetect beat, AudioInput source)
    {
        this.source = source;
        this.source.addListener(this);
        this.beat = beat;
    }

    public void samples(float[] samples)
    {
        System.out.println("          mix");
        beat.detect(source.mix);
    }

    public void samples(float[] samples_left, float[] samples_right)
    {
        System.out.println("          leri");
        beat.detect(source.mix);
    }
}
