#include <avr/io.h>
#include <util/delay.h>
#include <Arduino.h>
#include <fsm.h>

int led = 12;
int button = 10;



int a = 1;  //For displaying segment "a"
int b = 2;  //For displaying segment "b"
int c = 3;  //For displaying segment "c"
int d = 4;  //For displaying segment "d"
int e = 5;  //For displaying segment "e"
int f = 6;  //For displaying segment "f"
int g = 7;  //For displaying segment "g"

void setup()
{
  pinMode(8, LOW); // setting up the board
  pinMode(9, HIGH);
  pinMode(led, OUTPUT);
    pinMode(a, OUTPUT);
      pinMode(b, OUTPUT);
        pinMode(c, OUTPUT);
          pinMode(d, OUTPUT);
            pinMode(e, OUTPUT);
              pinMode(f, OUTPUT);
                pinMode(g, OUTPUT);

}

int main(void)
{
  setup();
  int intial_state = 0;
  while (1){
    led_on();
    button_not_pressed(intial_state); // initial state
  }
  return 0;
}

void led_on()
{
  digitalWrite(led, HIGH);
  _delay_ms(1000);
  button_pressed(1);
}

void led_off()
{
  digitalWrite(led, LOW);
  _delay_ms(1000);
  button_pressed(2);
}

void button_pressed(int input)
{
  int state = digitalRead(button);
  if (input == 0) {
    if (state == 1) {
       button_not_pressed(0);
    } else {
       state_zero(state);
    }
  } else if (input == 1) {
    if (state == 1) {
      led_on();
    } else {
      led_off();
    }
  } else if (input == 3) {
    if (state == 1) {
      led_off();
    } else {
      led_on();
    }
  }
}


void button_not_pressed(int state)
{
  int input = digitalRead(button);
  if (input == 1) { // if button is NOT pushed
    switch(state) {
      case 1:
        state_one(0);
        break;
      case 2:
        state_two(0);
        break;
      case 3:
        state_three(0);
        break;
      case 4:
        state_four(0);
        break;
      case 5:
        state_five(0);
        break;
      case 6:
        state_six(0);
        break;
      case 7:
        state_seven(0);
        break;
      case 8:
        state_eight(0);
        break;
      case 9:
        state_nine(0);
        break;
      default:
        state_zero(0);
    }
  } else {
    button_pressed(0);
    if (
  }
}

void state_zero(int input)
{
  digitalWrite(a, HIGH);
  digitalWrite(b, HIGH);
  digitalWrite(c, HIGH);
  digitalWrite(d, HIGH);
  digitalWrite(e, HIGH);
  digitalWrite(f, HIGH);
  digitalWrite(g, LOW);
  _delay_ms(1000);
  if (input == false) {
      button_not_pressed(1);
  } else {
      button_pressed();
  }
}

void state_one(int input)
{
    digitalWrite(a, LOW);
    digitalWrite(b, HIGH);
    digitalWrite(c, HIGH);
    digitalWrite(d, LOW);
    digitalWrite(e, LOW);
    digitalWrite(f, LOW);
    digitalWrite(g, LOW);
    _delay_ms(1000);
    button_not_pressed(2);
}

void state_two(int input)
{
    digitalWrite(a, HIGH);
    digitalWrite(b, HIGH);
    digitalWrite(c, LOW);
    digitalWrite(d, HIGH);
    digitalWrite(e, HIGH);
    digitalWrite(f, LOW);
    digitalWrite(g, HIGH);
    _delay_ms(1000);
    button_not_pressed(3);
}

void state_three(int input)
{
    digitalWrite(a, HIGH);
    digitalWrite(b, HIGH);
    digitalWrite(c, HIGH);
    digitalWrite(d, HIGH);
    digitalWrite(e, LOW);
    digitalWrite(f, LOW);
    digitalWrite(g, HIGH);
    _delay_ms(1000);
    button_not_pressed(4);
}

void state_four(int input)
{
    digitalWrite(a, LOW);
    digitalWrite(b, HIGH);
    digitalWrite(c, HIGH);
    digitalWrite(d, LOW);
    digitalWrite(e, LOW);
    digitalWrite(f, HIGH);
    digitalWrite(g, HIGH);
    _delay_ms(1000);
    button_not_pressed(5);
}

void state_five(int input)
{
    digitalWrite(a, HIGH);
    digitalWrite(b, LOW);
    digitalWrite(c, HIGH);
    digitalWrite(d, HIGH);
    digitalWrite(e, LOW);
    digitalWrite(f, HIGH);
    digitalWrite(g, HIGH);
    _delay_ms(1000);
    button_not_pressed(6);
}

void state_six(int input)
{
    digitalWrite(a, HIGH);
    digitalWrite(b, LOW);
    digitalWrite(c, HIGH);
    digitalWrite(d, HIGH);
    digitalWrite(e, HIGH);
    digitalWrite(f, HIGH);
    digitalWrite(g, HIGH);
    _delay_ms(1000);
    button_not_pressed(7);
}

void state_seven(int input)
{
    digitalWrite(a, HIGH);
    digitalWrite(b, HIGH);
    digitalWrite(c, HIGH);
    digitalWrite(d, LOW);
    digitalWrite(e, LOW);
    digitalWrite(f, LOW);
    digitalWrite(g, LOW);
    _delay_ms(1000);
    button_not_pressed(8);
}

void state_eight(int input)
{


    digitalWrite(a, HIGH);
    digitalWrite(b, HIGH);
    digitalWrite(c, HIGH);
    digitalWrite(d, HIGH);
    digitalWrite(e, HIGH);
    digitalWrite(f, HIGH);
    digitalWrite(g, HIGH);
    _delay_ms(1000);
    button_not_pressed(9);
}

void state_nine(int input)
{
  digitalWrite(a, HIGH);
  digitalWrite(b, HIGH);
  digitalWrite(c, HIGH);
  digitalWrite(d, LOW);
  digitalWrite(e, LOW);
  digitalWrite(f, HIGH);
  digitalWrite(g, HIGH);
  _delay_ms(1000);
  button_not_pressed(0);
}
