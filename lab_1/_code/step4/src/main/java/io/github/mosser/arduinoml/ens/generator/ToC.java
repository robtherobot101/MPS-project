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

	    // Libraries to include
		c("// C code generated from an object model");
		c(String.format("// Application name: %s\n", app.getName()));
		c("#include <avr/io.h>");
		c("#include <util/delay.h>");
		c("#include <Arduino.h>");
		c("#include <fsm.h>");
		c("");

		// Generate a list of state machines
		app.recurse();

		// Give the pins meaningful names
        for(Actuator a: app.getActuators()){
            c(String.format("int %s = %d;", a.getName(), a.getPin()));
        }

        // Assign pins as inputs/outputs
		c("void setup(){");
		for(Actuator a: app.getActuators()){
			a.accept(this);
		}

		// Initialise the enum which represents events
		StringBuilder eventsEnum = new StringBuilder("enum events {");
		for (Event event : app.getEvents()) {
			eventsEnum.append(event.getName()).append(", ");
		}
		eventsEnum.append("};");
		h(eventsEnum.toString());



        if (!app.getInitialStates().isEmpty()) {

            // App is a Non-Deterministic Finite State Machine with multiple initial states ( think one initial state with lambda transitions )
            for (App machine: app.getMachines()) {
                // Function pointer to store current state
                h(String.format("void (*%s_state_machine)(int event);", machine.getName()));
                // Set the pointer to the initial state of the machine
                c(String.format("  %s_state_machine = &%s;", machine.getName(), machine.getInitial().getName()));
            }
            h(String.format("void (*%s_state_machine)(int event);", app.getName()));
            c(String.format("  %s_state_machine = &%s;", app.getName(), app.getInitial().getName()));

        }

		c("}\n");


        // Function which sends the given event to all state machines
        c("void do_event(int event)");
        c("{");
        for (String stateMachine: app.getNames()) {
            // Execute the machine with the given event
            c(String.format("  %s_state_machine(event);", stateMachine));
        }
        c(String.format("  %s_state_machine(event);", app.getName()));
        c("}\n");

        // Write out function definitions for all states
		for(State state: app.getStates()){
			state.accept(this);
		}

        if (!app.getInitialStates().isEmpty()) {
            // Defines the main loop of the program
			c("void loop(void) {");
			// The default event is the 'null' event
            c(String.format("  do_event(%s);", app.getNull_event().getName()));
			c("}");
		}
	}

	@Override
	public void visit(Variable variable) {
	    // Choose a signature
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
		// Write out the initialisation
		c(String.format("  static %s %s = %s;", typeString, variable.getName(), variable.getInitialValue()));
	}

	@Override
	public void visit(VariableGreaterTrigger variableGreaterTrigger) {
	    // Check whether the threshold is exceeded
		c(String.format("if(%s > %d) {", variableGreaterTrigger.getVariable().getName(), variableGreaterTrigger.getThreshold()));
		// Fire the event
		variableGreaterTrigger.getEvent().accept(this);
		c("}");

	}

	@Override
	public void visit(Actuator actuator) {
	    // Set a pin to input/output
	 	c(String.format("  pinMode(%s, %s); // %s [Actuator]", actuator.getName(), actuator.getMode(), actuator.getName()));
	}

	@Override
	public void visit(State state) {
	    // Define signature of state's function
		h(String.format("void %s(int event);", state.getName()));
		// Write out the implementation of the function
		c(String.format("void %s(int event) {",state.getName()));

		// Write out variable definitions for each of the state's variables
		for (Variable variable : state.getVariables()) {
			variable.accept(this);
		}

		// Write out the code to execute each action of the state
		for(Action action: state.getActions()) {
			action.accept(this);
		}

		// Write out the code to transition to different states
		for(Transition transition:state.getTransitions()) {
			transition.accept(this);
		}

        // Code to fire events
		for (Trigger trigger : state.getTriggers()) {
			trigger.accept(this);
		}
		c("}");
	}


	@Override
	public void visit(ActuatorAction action) {
	    // Write to a pin
		c(String.format("  digitalWrite(%s,%s);",action.getActuator().getName(),action.getValue()));
	}

	@Override
	public void visit(ConditionalAction conditionalAction) {
	    // Create a switch which executes the right action for the value of the variable
		c(String.format("  switch(%s) { \n", conditionalAction.getVariable().getName()));
		for (int i = 0; i < conditionalAction.getActions().length; i++) {
		    // If we are in case i, we execute the action in the array at index i
			c(String.format("    case %d: ", i));
			conditionalAction.getActions()[i].accept(this);
			c("    break;");
		}
		c("  }");
	}

	@Override
	public void visit(DelayedAction delayedAction) {
	    // Check whether the required amount of time has elapsed
		c(String.format("if (millis() - %s > %dUL) {", delayedAction.getTimer().getName(), delayedAction.getDelay()));
		c(String.format("%s = millis();", delayedAction.getTimer().getName()));
		// Execute the action
		visit(delayedAction.getAction());
		c("}");
	}

	@Override
	public void visit(VariableAction variableAction) {
	    // Perform the given action on the given variable
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

	@Override
	public void visit(Sensor sensor) {
	    // Check whether the actuator is returning the required value
	    if (sensor.getValue() == SIGNAL.HIGH) {
            c(String.format("  if (digitalRead(%s)) {",sensor.getActuator().getName()));
        }
	    else {
            c(String.format("  if (!digitalRead(%s)) {",sensor.getActuator().getName()));
        }
        // If it is, fire an event
	    sensor.getEvent().accept(this);
		c("  }");

	}

	@Override
	public void visit(Transition transition) {
	    // If the required event has been fired
        c(String.format("  if(event == %s) {", transition.getEventTrigger().getName()));
        // Execute an action if required
        if (transition.getAction() != null) {
        	transition.getAction().accept(this);
		}
		// Change state
        c(String.format("    %s_state_machine = &%s;", transition.getName(), transition.getTarget().getName()));
        // When a machine transitions, execution of the previous state should stop, so we return
        c("return;");
        c("  }");
    }

	@Override
	public void visit(Event event) {
	    
		c(String.format("do_event(%s);", event.getName()));
	}


}
