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
            c(String.format("  led_state_machine = &led_state_on;"));
            c(String.format("  button_state_machine = &button_state_up;"));
            c(String.format("  sevenseg_state_machine = &sevenseg_state_initialising;"));

        }

		c("}\n");


        // Run the state machines

        c("void do_event(int event)");
        c("{");
        //TODO: Remove hard coded state machines once I figure out how to name them properly
        c(String.format("  led_state_machine(event);"));
        c(String.format("  button_state_machine(event);"));
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
		String typeString = "int";
		switch(variable.getType()) {
			case LONG:
				typeString = "unsigned long";
				break;
			case INTEGER:
				typeString = "int";
				break;
			case STRING:
				typeString = "char*[]";
		}
		c(String.format("  static %s %s = %s;", typeString, variable.getName(), variable.getInitialValue()));
	}

	@Override
	public void visit(VariableGreaterTrigger variableGreaterTrigger) {
		c(String.format("if(%s > %d) {", variableGreaterTrigger.getVariable().getName(), variableGreaterTrigger.getThreshold()));
		variableGreaterTrigger.getEvent().accept(this);
		c("}");

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

		for (Variable variable : state.getVariables()) {
			variable.accept(this);
		}

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

		for (Trigger trigger : state.getTriggers()) {
			trigger.accept(this);
		}
		c("}");
	}


	@Override
	public void visit(ActuatorAction action) {
	    // Command for action
		c(String.format("  digitalWrite(%s,%s);",action.getActuator().getName(),action.getValue()));
	}

	@Override
	public void visit(ConditionalAction conditionalAction) {
		c(String.format("  switch(%s) { \n", conditionalAction.getVariable().getName()));
		for (int i = 0; i < conditionalAction.getActions().length; i++) {
			c(String.format("    case %d: ", i));
			conditionalAction.getActions()[i].accept(this);
			c("    break;");
		}
		c("  }");
	}

	@Override
	public void visit(DelayedAction delayedAction) {
		c(String.format("if (millis() - %s > %dUL) {", delayedAction.getTimer().getName(), delayedAction.getDelay()));
		c(String.format("%s = millis();", delayedAction.getTimer().getName()));
		visit(delayedAction.getAction());
		c("}");
	}

	@Override
	public void visit(VariableAction variableAction) {
		switch (variableAction.getAction_type()) {
			case SET:
				c(String.format("%s = %s;", variableAction.getVariable().getName(), variableAction.getNew_value()));
				break;
			case INCREMENT:
				c(String.format("%s++;", variableAction.getVariable().getName()));
				break;
			case DECREMENT:
				c(String.format("%s--;", variableAction.getVariable().getName()));
				break;
		}
	}

	// Our sensor code
	@Override
	public void visit(Sensor sensor) {
	    if (sensor.getValue() == SIGNAL.HIGH) {
            c(String.format("  if (digitalRead(%s)) {",sensor.getActuator().getName()));
        }
	    else {
            c(String.format("  if (!digitalRead(%s)) {",sensor.getActuator().getName()));
        }
	    sensor.getEvent().accept(this);
		c("  }");

	}

	// Our transition code
	@Override
	public void visit(Transition transition) {
        c(String.format("  if(event == %s) {", transition.getEventTrigger().getName()));
        if (transition.getAction() != null) {
        	transition.getAction().accept(this);
		}
        c(String.format("    %s_state_machine = &%s;", transition.getName(), transition.getTarget().getName()));
        c("  }");
    }

	@Override
	public void visit(Event event) {
		c(String.format("do_event(%s);", event.getName()));
	}


}
