#include <SDS011.h>
#include <LiquidCrystal.h>
#include <ArduinoJson.h>

#include <Firmata.h>

float p10, p25;
int error;

SDS011 my_sds;
LiquidCrystal lcd(9, 8, 4, 5, 6, 7);

StaticJsonBuffer<200> jsonBuffer;

void stringCallback(char *myString)
{
  Firmata.sendString(myString);
}


void sysexCallback(byte command, byte argc, byte *argv)
{
  Firmata.sendSysex(command, argc, argv);
}

void setup() {
  my_sds.begin(11, 10);
  lcd.begin(16, 2);

  Firmata.setFirmwareVersion(FIRMATA_FIRMWARE_MAJOR_VERSION, FIRMATA_FIRMWARE_MINOR_VERSION);
  Firmata.attach(STRING_DATA, stringCallback);
  Firmata.attach(START_SYSEX, sysexCallback);
  Firmata.begin(57600);

  while (!Serial) {
    ;
  }
}

void lcd_print(int ligne, String content) {
  lcd.setCursor(0, ligne);
  lcd.print(content);
}

void loop() {

  while (Firmata.available()) {
    Firmata.processInput();
  }

  if (!my_sds.read(&p25, &p10)) {

    {
      JsonObject& root = jsonBuffer.createObject();
      JsonObject& sensor = root.createNestedObject("sensor");
      sensor["name"] = "SDS011";
      sensor["type"] = "pm10";
      sensor["unity"] = "ug/m^3";
      root["value"] = String(p10);

      char buff[100];
      root.printTo(buff);
      Firmata.sendString(buff);
    }

    {
      JsonObject& root = jsonBuffer.createObject();
      JsonObject& sensor = root.createNestedObject("sensor");
      sensor["name"] = "SDS011";
      sensor["type"] = "pm2.5";
      sensor["unity"] = "ug/m^3";
      root["value"] = String(p25);

      char buff[100];
      root.printTo(buff);
      Firmata.sendString(buff);
    }

    jsonBuffer.clear();

    lcd.clear();
    lcd_print(0, "P2.5: " + String(p25));
    lcd_print(1, "P10: " + String(p10));
  }

  delay(1000);
}