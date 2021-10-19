
#include <SoftwareSerial.h>

SoftwareSerial SIM868(3, 2); //RX, TX
void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  SIM868.begin(9600);
  Serial.println("all ok ");

}

void loop() {
  // put your main code here, to run repeatedly:
  updateSerial();
}

void updateSerial()
{
  while (Serial.available()) 
  {
    SIM868.write(Serial.read());//Forward what Serial received to Software Serial Port
  }
  while(SIM868.available()) 
  {
    Serial.write(SIM868.read());//Forward what Software Serial received to Serial Port
  }
}
