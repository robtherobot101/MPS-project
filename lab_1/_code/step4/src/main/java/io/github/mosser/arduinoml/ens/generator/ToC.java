package io.github.mosser.arduinoml.ens.generator;

import io.github.mosser.arduinoml.ens.model.*;

public class ToC extends Visitor<StringBuffer> {

    // What is this for? I thought remembering states is against the rules of the game...
	private final static String CURRENT_STATE = "current_state";

	public ToC() {
		this.code = new StringBuffer();
		this.headers = new StringBuffer();
	}

	private void c(String s) {
		code.append(String.format("%s\n",s));
	}

	private void h(String s) {
		headers.append(String.format("%s\n",s));
	}

	@Override
	public void visit(App app) {
		c("// C code generated from an object model");
		c(String.format("// Application name: %s\n", app.getName()));
		c("#include <avr/io.h>");
		c("#include <util/delay.h>");
		c("#include <Arduino.h>");
		c("#include <fsm.h>");
		c("");
        for(Actuator a: app.getActuators()){
            c(String.format("int %s = %d;", a.getName(), a.getPin()));
        }

		c("void setup(){");
		for(Actuator a: app.getActuators()){
			a.accept(this);
		}

		// TODO: This probably shouldn't be hardcoded, must abstract this somehow
		h("enum events { NULL_EVENT, BUTTON_PRESSED, BUTTON_RELEASED, COUNT_OVERFLOW };");



        if (!app.getInitialStates().isEmpty()) {

            // App is a Non-Deterministic Finite State Machine with multiple initial states ( think one initial state with lambda transitions )
            for (State state: app.getInitialStates()) {
                //c(String.format("  %s_state_machine = &%s;", app.getName(), state.getName()));
                //c(String.format("  %s_state_machine = &%s;", state.getName(), state.getName()));

                // add initial states as state machines to header file
                //h(String.format("void (*%s_state_machine)(int event);", app.getName()));
                h(String.format("void (*%s_state_machine)(int event);", state.getName()));
            }

            //TODO: Remove hard coded state machines once I figure out how to name them properly
            c(String.format("  led_state_machine = &led_state_on;"));
            c(String.format("  button_state_machine = &button_state_up;"));

        }
        
		c("}\n");


        // Run the state machines

        c("void do_event(int event)");
        c("{");
        for (State state: app.getInitialStates()) {
            // c(String.format("    %s_state_machine(NULL_EVENT);", app.getName()));
            //c(String.format("    %s_state_machine(NULL_EVENT);", state.getName()));
        }
        //TODO: Remove hard coded state machines once I figure out how to name them properly
        c(String.format("  led_state_machine(event);"));
        c(String.format("  button_state_machine(event);"));
        c("}\n");



		for(State state: app.getStates()){
			h(String.format("void state_%s(int event);", state.getName()));
			state.accept(this);
		}

        if (!app.getInitialStates().isEmpty()) {
			c("int main(void) {");
			c("  setup();");
            c("  while(1) {");
            c("    do_event(NULL_EVENT);");

            c("  }");
			c("  return 0;");
			c("}");
		}
	}

	@Override
	public void visit(Actuator actuator) {

	 	c(String.format("  pinMode(%s, %s); // %s [Actuator]", actuator.getName(), actuator.getMode(), actuator.getName()));
	}


	@Override
	public void visit(State state) {
	    // Note the name of the sate must be prefixed with the name of the statemachine (the app) e.g. 'led_state_on'
		c(String.format("void %s(int event) {",state.getName()));
		for(Action action: state.getActions()) {
			action.accept(this);
		}
		// this delay is an action itself
		//c("  _delay_ms(1000);");
		// I think this is where we rewrite the code to include events

        // failing if statement so transactions can be stacked
        //c("  if (1 == 2) {}");
        for(Transition transition:state.getTransitions()) {
            transition.accept(this);
        }
		//c(String.format("  state_%s();", state.getNext().getName()));
		c("}");
	}


	@Override
	public void visit(Action action) {
	    // Command for action
		c(String.format("  digitalWrite(%s,%s);",action.getActuator().getName(),action.getValue()));
	}

	// Our sensor code
	@Override
	public void visit(Sensor sensor) {
	    if (sensor.getValue() == SIGNAL.HIGH) {
            c(String.format("  else if (!digitalRead(%s)) {",sensor.getActuator().getName()));
            c("    do_event(BUTTON_PRESSED);");
            c("  }");
        } else {
            c(String.format("  else if (digitalRead(%s)) {",sensor.getActuator().getName()));
            c("    do_event(BUTTON_RELEASED);");
            c("  }");
        }

	}

	// Our transition code
	@Override
	public void visit(Transition transition) {
        c(String.format("  if(event == %s) {", transition.getEvent()));
        c(String.format("    %s_state_machine = &%s;", transition.getName(), transition.getTarget().getName()));
        c("  }");
	    if (transition.getTrigger() != null) {
            transition.getTrigger().accept(this);
        }
    }


}
