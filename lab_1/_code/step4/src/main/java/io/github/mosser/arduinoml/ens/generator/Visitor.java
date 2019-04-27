package io.github.mosser.arduinoml.ens.generator;

import io.github.mosser.arduinoml.ens.model.*;

public abstract class Visitor<T> {

	public abstract void visit(App app);
	public abstract void visit(State state);
	public abstract void visit(Variable variable);
	public abstract void visit(VariableGreater variableGreater);
	public abstract void visit(ActuatorAction action);
	public abstract void visit(ConditionalAction conditionalAction);
	public abstract void visit(DelayedAction delayedAction);
	public abstract void visit(VariableAction variableAction);
	public abstract void visit(Actuator actuator);
	public abstract void visit(Sensor sensor);
	public abstract void visit(Transition transition);

	/***********************
	 ** Helper mechanisms **
	 ***********************/

	T code;
	T headers;

	public T getCode() {
		return code;
	}
	public T getHeaders() {
		return headers;
	}
}

