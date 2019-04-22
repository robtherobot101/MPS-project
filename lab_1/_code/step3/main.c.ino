#include <avr/io.h>
#include <util/delay.h>
#include <Arduino.h>
#include <fsm.h>

int led = 13;

void setup()
{
  pinMode(led, OUTPUT);
}

int main(void)
{
  setup();
  while (1){
    state_on() // initial state
    state_off()
  }
  return 0;
}

void state_on()
{
  digitalWrite(led, HIGH);
  _delay_ms(1000);
  state_off();
}

void state_off()
{
  digitalWrite(led, LOW);
  _delay_ms(1000);
  state_on();
}
