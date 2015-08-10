// SETUP:
//    PIN GND -> GND
//    PIN 13  -> CLK
//    PIN 11  -> DAT

#include <FastSPI_LED.h>
#define NUM_LEDS 17
#define PIN 4 // Do I need this?

struct CRGB { unsigned char g; unsigned char r; unsigned char b; };
struct CRGB *leds;

void setup()
{
  FastSPI_LED.setLeds(NUM_LEDS);
  FastSPI_LED.setChipset(CFastSPI_LED::SPI_LPD6803);

  FastSPI_LED.setPin(PIN);

  FastSPI_LED.init();
  FastSPI_LED.start();

  leds = (struct CRGB*)FastSPI_LED.getRGBData();

  Serial.begin(115200);
}

int buffer[NUM_LEDS*3];
int buffer_length = 0;

void loop()
{
  while (Serial.available() > 0)
  {
    buffer[buffer_length++] = Serial.read();
    if (buffer_length == NUM_LEDS*3)
    {
      for(int i = 0; i < NUM_LEDS-1; i++)
      {
        leds[i].r = buffer[i*3  ];
        leds[i].g = buffer[i*3+1];
        leds[i].b = buffer[i*3+2];
      }
      // I guess I don't nuderstand how leds[] is structured.
      //memcpy(leds, buffer, NUM_LEDS * 3); 
      FastSPI_LED.show();
      buffer_length = 0;
    }
  }
}



