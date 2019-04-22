#include <avr/io.h>
#include <util/delay.h>
#include <Arduino.h>

//Blinks the led one cycle over two.
//Now ports can be accessed by digitalWrite/digitalRead

// declaration of global variables
int led = 12;    // Synchronized with the shield led
int button = 10;
boolean led_on;  // it is C++ you have access to boolean vars.
boolean button_clicked;

//Global variables for 7 segment
int a = 1;  //For displaying segment "a"
int b = 2;  //For displaying segment "b"
int c = 3;  //For displaying segment "c"
int d = 4;  //For displaying segment "d"
int e = 5;  //For displaying segment "e"
int f = 6;  //For displaying segment "f"
int g = 7;  //For displaying segment "g"


void setup() {               
  //Setup for LED on pin 13
  pinMode(led, OUTPUT);  
  led_on=true;
  //do not forget to setup the 7 seg and the button
  pinMode(button, INPUT);
  button_clicked = false;

  //pinMode(a, OUTPUT);  // A
  // and global state variables
}

void change_state_led(){
  if (led_on){
    digitalWrite(led, LOW);
  } else
    digitalWrite(led, HIGH);
  led_on = !led_on;
}

void turnOff() //turn off the 7seg (CC) 
{
  int i;
  for (i = a; i <= g; i++){ // this could be prettier
    digitalWrite(i,HIGH); // change into HIGH for common anode
  }
}


void displayDigit(int digit)
{
  turnOff();
  //Conditions for displaying segment a
  if(digit!=1 && digit != 4)
    digitalWrite(a,LOW); // change into HIGH for common cathode
  // TODO : complete
}

int main(void)
{
  setup();
  change_state_led();
  while(1)
    { 
      //displayDigit(0); // uncomment to test
      //digitalWrite(a,LOW);
	  button_clicked = digitalRead(button)
	  if (button_clicked == true) {
		  change_state_led;
		  _delay_ms(1000);
	  }
	  
      
    }
  return 0;
}
