import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;

class SelectInput
{
  private Minim minim;
  private Mixer mixer;

  private int channel;
  private int bufferSize;

  private Mixer.Info[] mixerInfo;
  private Mixer.Info[] usableInputs;
  private int usableInputsSize = 0;

  SelectInput(int channel, int bufferSize)
  {
    this.channel = channel;
    this.bufferSize = bufferSize;

    AudioInput in;
    minim = new Minim(this);
    mixerInfo = AudioSystem.getMixerInfo();
    Mixer.Info[] usableInputs_temp = new Mixer.Info[mixerInfo.length];

    for(int i = 0; i < mixerInfo.length; i++)
    {
      boolean passed = true;
      try
      {
        // Set the i-th input and try getting data from it.
        mixer = AudioSystem.getMixer(mixerInfo[i]);
        minim.setInputMixer(mixer);
        in = minim.getLineIn(channel, bufferSize);

        // (we try to pull some data from the input,
        //  this is what crashes invalid inputs)
        in.left.get(0);
      } 
      catch (Exception e)
      {
        // If it crashes, we cannot use it.
        passed = false;
      }
      // But if it doesn't, then we can.
      if (passed) usableInputs_temp[usableInputsSize++] = mixerInfo[i];
    }

    usableInputs = new Mixer.Info[usableInputsSize];
    System.arraycopy(usableInputs_temp, 0, usableInputs, 0, usableInputsSize);
  }

  Mixer.Info[] getInputs()
  {
    return usableInputs;
  }

  AudioInput setInput(Mixer.Info mixerInfoElement)
  {
    mixer = AudioSystem.getMixer(mixerInfoElement);
    minim.setInputMixer(mixer);
    return minim.getLineIn(channel, bufferSize);
  }
}