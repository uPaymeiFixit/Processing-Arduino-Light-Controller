import ddf.minim.AudioListener;

// This class adds a listener to Minim's BeatDetect
// class so that BeatDetect.detect() is automatically 
// called whenever a new audio buffer arrives.
class BeatListener implements AudioListener
{
    private BeatDetect beat;
    private AudioInput source;

    BeatListener(BeatDetect beat, AudioInput source)
    {
        this.source = source;
        this.source.addListener(this);
        this.beat = beat;
    }

    void samples(float[] samples)
    {
        beat.detect(source.mix);
    }

    void samples(float[] samplesL, float[] samplesR)
    {
        beat.detect(source.mix);
    }
}