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
		c("void setup(){");
		for(Actuator a: app.getActuators()){
			a.accept(this);
		}

        if (app.getInitial() != null) {

            //TODO: Change this to iterate through the app's statemachines
            c(String.format("  %s_state_machine = &_state_%s;", app.getName(), app.getName(), app.getInitial().getName()));
        }
        
		c("}\n");

		for(State state: app.getStates()){
			h(String.format("void state_%s();", state.getName()));
			state.accept(this);
		}

		if (app.getInitial() != null) {
			c("int main(void) {");
			c("  setup();");
            c("  while(1) {");
            //TODO: Change this to iterate through the app's statemachines
            c(String.format("    %s_state_machine(null_event);", app.getName()));
            c("  }");
			c("  return 0;");
			c("}");
		}
	}

	@Override
	public void visit(Actuator actuator) {

	 	c(String.format("  pinMode(%d, %s); // %s [Actuator]", actuator.getPin(), accutator.getMode(), actuator.getName()));
	}


	@Override
	public void visit(State state) {
		h(String.format("void state_%s(int event) {",state.getName()));
		for(Action action: state.getActions()) {
			action.accept(this);
		}
		// this delay is an action itself
		c("  _delay_ms(1000);");
		// I think this is where we rewrite the code to include events

        //TODO: Modify for multiple transitions using else if block
        for(Transition transition:state.getTransitions()) {
            transition.accept(this);
        }
		//c(String.format("  state_%s();", state.getNext().getName()));
		c("}");
	}


	@Override
	public void visit(Action action) {
	    // Command for action
		c(String.format("  digitalWrite(%d,%s);",action.getActuator().getPin(),action.getValue()));
	}

	// Our sensor code
	@Override
	public void visit(Sensor sensor) {
        c(String.format("  pinMode(%d, INPUT); // %s [Sensor]", sensor.getPin(), sensor.getName()));
	}

	// Our transition code
	@Override
	public void visit(Transition transition) {
        c(String.format("  if(event == %s) {", transition.getEvent()));
        c(String.format("    %s_state_machine = &%s;", transition.getName(), transition.getTarget().getName()));
        c(String.format("  }", transition.getEvent()));
    }


}
