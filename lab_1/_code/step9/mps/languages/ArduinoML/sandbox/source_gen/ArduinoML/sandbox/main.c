#include <avr/io.h>
#include <util/delay.h>
#include <Arduino.h>

/** Generating code for application led2 **/

// Declaring states function headers 
void state_on2();
void state_off2();

// Declaring available actuators 
int theLed2 = 13;

// Declaring available sensors 


// Declaring states
void state_on2()
{
  digitalWrite(theLed2, HIGH);
  _delay_ms(1000);
  state_off2();
}

void state_off2()
{
  digitalWrite(theLed2, LOW);
  _delay_ms(1000);
  state_on2();
}


void setup()
{
  pinMode(theLed2, OUTPUT);

}

int main(void)
{
  setup();
  state_on2();
  return 0;
}