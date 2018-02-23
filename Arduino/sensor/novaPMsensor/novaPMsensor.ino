#include <SDS011.h>
#include <LiquidCrystal.h>

float p10, p25;
int error;

SDS011 my_sds;
LiquidCrystal lcd(9, 8, 4, 5, 6, 7);

void setup() {
  my_sds.begin(11, 10);
  lcd.begin(16, 2);
  Serial.begin(9600);
}

void lcd_print(int ligne, String content) {
  lcd.setCursor(0, ligne);
  lcd.print(content);
}

void loop() {
  error = my_sds.read(&p25, &p10);
  Serial.println(error);
  if (! error) {
    lcd.clear();
    Serial.println("P2.5: " + String(p25));
    lcd_print(0, "P2.5: " + String(p25));
    Serial.println("P10: " + String(p10));
    lcd_print(1, "P10: " + String(p10));
  }
  delay(1000);
}
