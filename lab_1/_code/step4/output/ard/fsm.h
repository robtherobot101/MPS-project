enum events { NULL_EVENT, BUTTON_PRESSED, BUTTON_RELEASED, COUNT_OVERFLOW };
void (*led_state_machine)(int event);
void state_led_state_on(int event);
void state_led_state_off(int event);