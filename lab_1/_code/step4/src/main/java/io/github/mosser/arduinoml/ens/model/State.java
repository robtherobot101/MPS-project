package io.github.mosser.arduinoml.ens.model;

import io.github.mosser.arduinoml.ens.generator.Visitable;
import io.github.mosser.arduinoml.ens.generator.Visitor;

import java.util.ArrayList;
import java.util.List;

public class State implements NamedElement, Visitable {

	private String name;
	private List<Action> actions = new ArrayList<>();
	private List<Transition> transitions = new ArrayList<Transition>();
	private List<Variable> variables = new ArrayList<>();
	private List<Trigger> triggers = new ArrayList<>();

	@Override public String getName() {
		return name;
	}
	@Override public void setName(String name) {
		this.name = name;
	}

	public List<Action> getActions() {
		return actions;
	}
	public void setActions(List<Action> actions) {
		this.actions = actions;
	}


    public List<Transition> getTransitions() { return transitions; }

    public void setTransitions(List<Transition> transitions) { this.transitions = transitions; }

    @Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	public List<Variable> getVariables() {
		return variables;
	}

	public void setVariables(List<Variable> variables) {
		this.variables = variables;
	}

	public List<Trigger> getTriggers() {
		return triggers;
	}

	public void setTriggers(List<Trigger> triggers) {
		this.triggers = triggers;
	}
}
