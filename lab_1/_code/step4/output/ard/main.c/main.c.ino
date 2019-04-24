// C code generated from an object model
// Application name: led

#include <avr/io.h>
#include <util/delay.h>
#include <Arduino.h>
#include <fsm.h>

void setup(){
  pinMode(12, OUTPUT); // LED [Actuator]
  led_state_machine = &led_state_on;
}

void led_state_on(int event) {
  digitalWrite(12,HIGH);
  _delay_ms(1000);
  if(event == NULL_EVENT) {
    led_state_machine = &led_state_off;
  }
}
void led_state_off(int event) {
  digitalWrite(12,LOW);
  _delay_ms(1000);
  if(event == NULL_EVENT) {
    led_state_machine = &led_state_on;
  }
}
int main(void) {
  setup();
  while(1) {
    led_state_machine(NULL_EVENT);
  }
  return 0;
}
