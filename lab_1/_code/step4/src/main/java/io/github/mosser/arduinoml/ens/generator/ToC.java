package io.github.mosser.arduinoml.ens.generator;

import io.github.mosser.arduinoml.ens.model.*;

import java.util.HashSet;
import java.util.Set;

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

		for (Variable v : app.getVariables()) {
			v.accept(this);
		}

        for(Actuator a: app.getActuators()){
            c(String.format("int %s = %d;", a.getName(), a.getPin()));
        }

		c("void setup(){");
		for(Actuator a: app.getActuators()){
			a.accept(this);
		}

		StringBuilder eventsEnum = new StringBuilder("enum events {");
		for (Event event : app.getEvents()) {
			eventsEnum.append(event.getName()).append(", ");
		}
		eventsEnum.append("};");
		h(eventsEnum.toString());



        if (!app.getInitialStates().isEmpty()) {

            // App is a Non-Deterministic Finite State Machine with multiple initial states ( think one initial state with lambda transitions )
            for (String machine: app.getMachines()) {
                h(String.format("void (*%s_state_machine)(int event);", machine));
            }

            //TODO: Remove hard coded state machines once I figure out how to name them properly
            //c(String.format("  led_state_machine = &led_state_on;"));
            //c(String.format("  button_state_machine = &button_state_up;"));
            c(String.format("  sevenseg_state_machine = &sevenseg_state_initialising;"));

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
        //c(String.format("  led_state_machine(event);"));
        //c(String.format("  button_state_machine(event);"));
        c(String.format("  sevenseg_state_machine(event);"));
        c("}\n");

		for(State state: app.getStates()){
			state.accept(this);
		}

        if (!app.getInitialStates().isEmpty()) {
			c("void loop(void) {");
            c(String.format("  do_event(%s);", app.getNull_event().getName()));
			c("}");
		}
	}

	@Override
	public void visit(Variable variable) {
		c(String.format("%s %s = %s;", variable.getType(), variable.getName(), variable.getInitialValue()));
	}

	@Override
	public void visit(Actuator actuator) {
	 	c(String.format("  pinMode(%s, %s); // %s [Actuator]", actuator.getName(), actuator.getMode(), actuator.getName()));
	}

	@Override
	public void visit(State state) {
		h(String.format("void %s(int event);", state.getName()));
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

	@Override
	public void visit(ConditionalAction conditionalAction) {
		c(String.format("  switch(%s) { \n", conditionalAction.getVariable().getName()));
		for (int i = 0; i < conditionalAction.getActions().length; i++) {
			c(String.format("    case %d: ", i));
			visit(conditionalAction.getActions()[i]);
			c("    break;");
		}
		c("}");
	}

	@Override
	public void visit(DelayedAction delayedAction) {
		c(String.format("if (millis() - %s > %dUL) {", delayedAction.getTimer().getName(), delayedAction.getDelay()));
		c(String.format("%s = millis();", delayedAction.getTimer().getName()));
		visit(delayedAction.getAction());
		c("}");
	}

    // Our sensor code
	@Override
	public void visit(Sensor sensor) {
	    if (sensor.getValue() == SIGNAL.HIGH) {
            c(String.format("  else if (!digitalRead(%s)) {",sensor.getActuator().getName()));
            c("    do_event(BUTTON_PRESSED_EVENT);");
            c("  }");
        } else {
            c(String.format("  else if (digitalRead(%s)) {",sensor.getActuator().getName()));
            c("    do_event(BUTTON_RELEASED_EVENT);");
            c("  }");
        }

	}

	// Our transition code
	@Override
	public void visit(Transition transition) {
        c(String.format("  if(event == %s) {", transition.getEvent().getName()));
        c(String.format("    %s_state_machine = &%s;", transition.getName(), transition.getTarget().getName()));
        c("  }");
	    if (transition.getTrigger() != null) {
            transition.getTrigger().accept(this);
        }
    }


}
