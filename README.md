# Processing Arduino Light Organ
This project allows you to take control of an LED strip from your computer and use it as a light organ (light visualizer) for the music you are listening to. Additionally you can easily program your own patterns.

This project takes advantage of:

 - [Processing](https://processing.org/) - Which the desktop program was written in.

 - [Minim](http://code.compartmental.net/tools/minim/) - An audio processing library for Processing.

 - FastSPI\_LED - A library for Arduino which allows control over the LED strip. (FastSPI\_LED has since [moved](https://github.com/FastLED/FastLED) and dropped support for the LPD6803 chipset.)


#Setup
###Light Strip
This project should work out of the box with any LED strip driven by the LPD6803 chipset. [This](http://amzn.com/B00DPVLTP6) is the one I use. 

###Power Source
You will need something to power the LED strip. I have the LED strip wired directly to the power supply on my computer. But if you are uncomfortable with this, you can use a power brick like [this](http://amzn.com/B003TUMDWG).

###Arduino
You will also need an Arduino. The cheap knockoffs will usually work just as well as the legitimate Arduino. I am using a legitimate Arduino Uno for this project.

###Wiring
The LED strip should have four terminals labeled something along the lines of Cout (**Clock**), Dout (**Data**), + (**Positive**), and - (**Negative**).

 - The **clock** terminal gets wired to **pin 13** on the Arduino Uno. (Note: it may be different for other Arduinos)

 - The **data** terminal gets wired to **pin 11** on the Arduino Uno. (See above note.)

 - The **positive** terminal gets wired to the **positive of the power source** you are using. (If you are using the a power brick like the one linked above, you can plug it in and ignore this step)

 - The **negative** terminal gets wired to a **GND pin** on the Arduino 
AND the **negative of the power source** you are using. (If you are using a power brick like the one linked above, you can plug it in and ignore the LAST part of this step)

###Software
You will need to download [Processing](https://processing.org/download/) and [Arduino](https://www.arduino.cc/en/Main/Software). 

After you have done this, you can download [Minim 2.2.0](http://code.compartmental.net/tools/minim/), unzip it, and place it in your Processing libraries folder. (Mac: ~/Documents/Processing/libraries/)

Next, move the FastSPI\_LED folder to Arduino's libraries folder. (Mac: ~/Documents/Arduino/libraries/)

Open Arduino/Light\_Organ\_Data\_Interpreter/Light\_Organ\_Data\_Interpreter.ino, and flash it to the Arduino.

Open Processing/main/main.pde and run it.

Done! It should be working now. If it's not, you can mess around with some of the code such as NUM\_LEDS in Pattern.pde & Light\_Organ\_Data\_Interpreter.ino, or SERIAL\_INDEX in main.pde.

Additionally, you can make your own patterns by changing/adding to Patterns.pde. After update() is called (which will be done automatically by main.pde), the leds array will be sent to the Arduino. You have access to Minim's Fast Fourier Transform and Beat Detect libraries. (fft & beat in Pattern.pde) If you are unfamiliar with these, I would suggest taking a look at [the documentation](http://code.compartmental.net/minim/index_analysis.html).

If your Pattern.pde file analyzes sound, it will by default analyze the sound from your default input device. In Mac, you can reroute your audio output into an input using [Soundflower](https://rogueamoeba.com/freebies/soundflower/).

Good luck!
