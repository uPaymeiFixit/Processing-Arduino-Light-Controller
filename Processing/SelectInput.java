import ddf.minim.AudioInput;
import ddf.minim.Minim;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;

// This class takes the same parameters as Minim's getLineIn method
// and tries all of the inputs to see if they are usable.
public class SelectInput
{
    private Minim minim;
    private Mixer mixer;

    private int channel;
    private int buffer_size;

    private Mixer.Info[] mixer_info;
    private Mixer.Info[] usable_inputs;
    private int usable_inputs_size = 0;

    public SelectInput(int channel, int buffer_size)
    {
        this.channel = channel;
        this.buffer_size = buffer_size;

        AudioInput in;
        minim = new Minim(this);
        mixer_info = AudioSystem.getMixerInfo();
        Mixer.Info[] usable_inputs_temp = new Mixer.Info[mixer_info.length];

        for(int i = 0; i < mixer_info.length; i++)
        {
            boolean passed = true;
            try
            {
                // Set the i-th input and try getting data from it.
                mixer = AudioSystem.getMixer(mixer_info[i]);
                minim.setInputMixer(mixer);
                in = minim.getLineIn(channel, buffer_size);

                // (we try to pull some data from the input,
                //    this is what crashes invalid inputs)
                in.left.get(0);
            }
            catch (NullPointerException e)
            {
                // If it crashes, we cannot use it.
                passed = false;
            }
            // But if it doesn't, then we can.
            if (passed) usable_inputs_temp[usable_inputs_size++] = mixer_info[i];
        }

        usable_inputs = new Mixer.Info[usable_inputs_size];
        System.arraycopy(usable_inputs_temp, 0, usable_inputs, 0, usable_inputs_size);
    }

    // Returns an array of the inputs that can actually have sound data
    //  pulled from them.
    public Mixer.Info[] getInputs()
    {
        return usable_inputs;
    }

    // Returns an AudioInput device based on the input you selected from
    //  the above method.
    public AudioInput setInput(Mixer.Info mixer_info_element)
    {
        mixer = AudioSystem.getMixer(mixer_info_element);
        minim.setInputMixer(mixer);
        return minim.getLineIn(channel, buffer_size);
    }
}
