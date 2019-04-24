// C code generated from an object model
// Application name: Led!

#include <avr/io.h>
#include <util/delay.h>
#include <Arduino.h>
#include <fsm.h>

void setup(){
  pinMode(13, null); // LED [Actuator]
  Led!_state_machine = &_state_Led!;
}

  digitalWrite(13,HIGH);
  _delay_ms(1000);
}
  digitalWrite(13,LOW);
  _delay_ms(1000);
}
int main(void) {
  setup();
  while(1) {
    Led!_state_machine(null_event);
  }
  return 0;
}
