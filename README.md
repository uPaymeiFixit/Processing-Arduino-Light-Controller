# Processing Arduino Light Organ
This project allows you to take control of an LED strip from your computer and use it as a light organ (light visualizer) for the music you are listening to. Additionally you can easily program your own patterns.

![Screen Shot 2019-06-29 at 11 24 54 PM](https://user-images.githubusercontent.com/1683528/60393149-87421300-9ac5-11e9-9c3b-8f425087cdde.png)

This project takes advantage of:

 - [Processing](https://processing.org/) - Which the desktop program was written in.

 - [Minim](http://code.compartmental.net/tools/minim/) - An audio processing library for Processing.

 - FastSPI\_LED - A library for Arduino which allows control over the LED strip. (FastSPI\_LED has since [moved](https://github.com/FastLED/FastLED) and dropped support for the LPD6803 chipset.)




![Screen Shot 2019-06-29 at 11 25 38 PM](https://user-images.githubusercontent.com/1683528/60393151-87421300-9ac5-11e9-949d-917eb8651689.png)
![Screen Shot 2019-06-29 at 11 26 07 PM](https://user-images.githubusercontent.com/1683528/60393152-87421300-9ac5-11e9-93cb-0d270bde8694.png)


# Setup
### Light Strip
This project should work out of the box with any LED strip driven by the LPD6803 chipset. [This](http://amzn.com/B00DPVLTP6) is the one I use. 

### Power Source
You will need something to power the LED strip. I have the LED strip wired directly to the power supply on my computer. But if you are uncomfortable with this, you can use a power brick like [this](http://amzn.com/B003TUMDWG).

### Arduino
You will also need an Arduino. The cheap knockoffs will usually work just as well as the legitimate Arduino. I am using a legitimate Arduino Uno for this project.

### Wiring
The LED strip should have four terminals labeled something along the lines of Cout (**Clock**), Dout (**Data**), + (**Positive**), and - (**Negative**).

 - The **clock** terminal gets wired to **pin 13** on the Arduino Uno. (Note: it may be different for other Arduinos)

 - The **data** terminal gets wired to **pin 11** on the Arduino Uno. (See above note.)

 - The **positive** terminal gets wired to the **positive of the power source** you are using. (If you are using the a power brick like the one linked above, you can plug it in and ignore this step)

 - The **negative** terminal gets wired to a **GND pin** on the Arduino 
AND the **negative of the power source** you are using. (If you are using a power brick like the one linked above, you can plug it in and ignore the LAST part of this step)

If you see the message below, the software was unable to find an arduino on an unused serial port.
![Screen Shot 2019-06-29 at 11 25 18 PM](https://user-images.githubusercontent.com/1683528/60393150-87421300-9ac5-11e9-9762-d2bd6424c794.png)

### Software
You will need to download [Processing](https://processing.org/download/) (v2.2.1 recommended) and [Arduino](https://www.arduino.cc/en/Main/Software). 

After you have done this, open Processing/Processing.pde, go to Sketch -> Import Library... -> Add Library... -> search for "minim" -> click Install

Next, open Arduino/Arduino.ino, and flash it to the Arduino.

**IMPORTANT:** After the sketch is finished uploading to the Arduino, make sure you quit the Arduino IDE.

Open and run Light_Organ.pde

Done! It should be working now. If it's not, you can mess around with some of the code such as NUM\_LEDS or BAUD_RATE in Arduino.ino and Light_Controller's settings.

Additionally, you can make your own patterns by adding one to your Documents/Light Controller/Plugins/ folder where you will find examples to help you out

If your plugin analyzes sound, it will by default analyze the sound from your default input device. In Mac, you can reroute your audio output into an input using [Soundflower](https://rogueamoeba.com/freebies/soundflower/).

If you find a bug, or would like a new feature, please report it [here](https://github.com/uPaymeiFixit/Processing-Arduino-Light-Controller/issues/new?title=Unexpected%20Error&body=%23%23%23%23+What+error+message+did+you+get%3F%0D%0D%0A%0D%0A%23%23%23%23+Can+you+reproduce+this+error%3F+If+so%2C+how%3F%0D%0A%0D%0A%0D%0A%23%23%23%23+Any+other+information%3F)
