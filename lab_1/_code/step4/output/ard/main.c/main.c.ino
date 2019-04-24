// C code generated from an object model
// Application name: led

#include <avr/io.h>
#include <util/delay.h>
#include <Arduino.h>
#include <fsm.h>

int led = 12;
int button = 10;
void setup(){
  pinMode(led, OUTPUT); // led [Actuator]
  pinMode(button, INPUT); // button [Actuator]
  led_state_machine = &led_state_on;
  button_state_machine = &button_state_up;
}

void do_event(int event)
{
  led_state_machine(event);
  button_state_machine(event);
}

void led_state_off(int event) {
  digitalWrite(led,LOW);
  if(event == BUTTON_PRESSED) {
    led_state_machine = &led_state_on;
  }
}
void led_state_on(int event) {
  digitalWrite(led,HIGH);
  if(event == BUTTON_PRESSED) {
    led_state_machine = &led_state_off;
  }
}
void button_state_up(int event) {
  if(event == BUTTON_PRESSED) {
    button_state_machine = &button_state_down;
  }
  else if (!digitalRead(button)) {
    do_event(BUTTON_PRESSED);
  }
}
void button_state_down(int event) {
  if(event == BUTTON_RELEASED) {
    button_state_machine = &button_state_up;
  }
  else if (digitalRead(button)) {
    do_event(BUTTON_RELEASED);
  }
}
int main(void) {
  setup();
  while(1) {
    do_event(NULL_EVENT);
  }
  return 0;
}
