//EVENTS

// App will handle this
enum events { null_event, button_pressed, button_released, count_overflow };
​
​
void (*led_state_machine)(int event); //LED state machine
void led_state_off(int event);
void led_state_on(int event);
​
//void sevenseg_state_machine(event_type event);
//void sevenseg_state_increment(event_type event);
//void sevenseg_state_reset(event_type event);
​
void (*button_state_machine)(int event); // Button state machine
void button_state_up(int event);
void button_state_down(int event);



#include <avr/io.h>
#include <util/delay.h>
#include <Arduino.h>
#include <fsm.h>

int led = 12;
int button = 10;

int count = 0;


void setup() {
  pinMode(led, OUTPUT);
  pinMode(button, INPUT);

  led_state_machine = &led_state_off;
  button_state_machine = &button_state_up;
  //sevenseg_state_machine(int event) = led_state_up;

}

void do_event(int event)
{
  led_state_machine(event);
  button_state_machine(event);
  //sevenseg_state_machine(event);
}

/////////////////////////////
//////////STATES/////////////
/////////////////////////////

//LED
void led_state_off(int event)
{
  digitalWrite(led, LOW); // action
  if (event == button_pressed) { // event
    led_state_machine = &led_state_on; // transition
  }
}

void led_state_on(int event)
{
  digitalWrite(led, HIGH);
  if (event == button_pressed) {
    led_state_machine = &led_state_off;
  }
}

//BUTTON
void button_state_up(int event)
{
  if (event == button_pressed){ // event
    button_state_machine = &button_state_down; // transition
  }
  else if (!digitalRead(button)) { // button pressed - sensor
    do_event(button_pressed);
  }
}

void button_state_down(int event)
{
  if (event == button_released){
    button_state_machine = &button_state_up;
  }
  else if (digitalRead(button)) {
    do_event(button_released);
  }
}

//Notice that states have similar structure:

/*void machine_state_name(int event) {
 *   do_some_stuff();
 *   check_if_relevant_event();
 *     do_transition();
 *   check_if_we_should_transition();
 *     trigger_event();
 **/

int main(void)
{
  setup();
  while(1)
  {
    do_event(null_event);
  }
  return 0;
}