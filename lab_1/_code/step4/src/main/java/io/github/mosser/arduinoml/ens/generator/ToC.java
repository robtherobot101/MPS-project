package io.github.mosser.arduinoml.ens.generator;

import io.github.mosser.arduinoml.ens.model.*;

public class ToC extends Visitor<StringBuffer> {

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

		c("void setup(){");

		for(Actuator a: app.getActuators()){
			a.accept(this);
		}
		c("}\n");

		for(State state: app.getStates()){
			h(String.format("void state_%s();", state.getName()));
			state.accept(this);
		}

		if (app.getInitial() != null) {
			c("int main(void) {");
			c("  setup();");
			c(String.format("  state_%s();", app.getInitial().getName()));
			c("  return 0;");
			c("}");
		}
	}

	@Override
	public void visit(Variable variable) {
		c(String.format("int %s = %d;", variable.getName(), variable.getValue()));
	}

	@Override
	public void visit(Actuator actuator) {
		c(String.format("  pinMode(%d, OUTPUT); // %s [Actuator]", actuator.getPin(), actuator.getName()));
	}

	@Override
	public void visit(State state) {
		c(String.format("void state_%s() {",state.getName()));
		for(Action action: state.getActions()) {
			action.accept(this);
		}
		c("  _delay_ms(1000);");
		c(String.format("  state_%s();", state.getNext().getName()));
		c("}");
	}


	@Override
	public void visit(Action action) {
		c(String.format("  digitalWrite(%d,%s);",action.getActuator().getPin(),action.getValue()));
	}

	@Override
	public void visit(ConditionalAction conditionalAction) {
		c(String.format("switch(%s) { \n", conditionalAction.getVariable().getName()));
		for (int i = 0; i < conditionalAction.getActions().length; i++) {
			c(String.format("case %d: ", i));
			visit(conditionalAction.getActions()[i]);
			c("break;");
		}
		c("}");
	}

	@Override
	public void visit(Sensor sensor) {
		c(String.format("  digitalWrite(%d,%s);",sensor.getState().getName()));
	}

}
