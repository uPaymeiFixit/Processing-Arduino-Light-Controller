// SETUP:
//    PIN GND -> GND
//    PIN 13  -> CLK
//    PIN 11  -> DAT

#include <FastSPI_LED.h>

// Change this to the number of LED segments you have
#define NUM_LEDS 17

// Time in between beacons
#define BEACON_PERIOD 500

// Unique byte that the desktop program should recognize
#define BEACON_KEY 42

// Serial baud rate. Should match desktop program.
#define BAUD_RATE 115200

struct CRGB { unsigned char g; unsigned char r; unsigned char b; };
struct CRGB *leds;

void setup()
{
  FastSPI_LED.setLeds(NUM_LEDS);

  // Set the chipset.
  // Supported chipsets are
  //    SPI_595   
  //    SPI_HL1606
  //    SPI_LPD6803 (This is what I am using.)
  //    SPI_WS2801
  //    SPI_TM1809 
  FastSPI_LED.setChipset(CFastSPI_LED::SPI_LPD6803);

  FastSPI_LED.init();
  FastSPI_LED.start();

  leds = (struct CRGB*)FastSPI_LED.getRGBData();

  Serial.begin(BAUD_RATE);
}

int buffer[NUM_LEDS*3];
int buffer_length = 0;
int lastBeacon = 0;
void loop()
{
  // If BEACON_PERIODms has passed, send beacon to let the 
  // program know that we are an arduino running the program.
  if (millis() > lastBeacon+BEACON_PERIOD)
  {
    lastBeacon = millis();
    Serial.write(BEACON_KEY);
  }

  // NUM_LEDS*3 bytes are expected to be sent all at the "same time".
  while (Serial.available() > 0)
  {
    // So as soon as we receive each byte, we need to store
    // it in our own buffer as to not overflow the Serial buffer.
    buffer[buffer_length++] = Serial.read();

    // After we have filled up the buffer, we will set the led strip
    // up with that data. It should be formatted as follows:
    //    [red0, green0, blue0, red1, green1, blue1, (...), redn, greenn, bluen]
    if (buffer_length == NUM_LEDS*3)
    {
      for(int i = 0; i < NUM_LEDS-1; i++)
      {
        leds[i].r = buffer[i*3  ];
        leds[i].g = buffer[i*3+1];
        leds[i].b = buffer[i*3+2];
      }
      FastSPI_LED.show();
      buffer_length = 0;
    }
  }
}