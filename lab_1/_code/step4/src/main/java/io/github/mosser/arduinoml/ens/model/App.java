package io.github.mosser.arduinoml.ens.model;

import io.github.mosser.arduinoml.ens.generator.*;

import java.util.ArrayList;
import java.util.List;

public class App implements NamedElement, Visitable {

	private String name;
	private List<Variable> variables = new ArrayList<>();
	private List<Actuator> actuators = new ArrayList<>();
	private List<String> machines = new ArrayList<>();
	private List<State> states = new ArrayList<>();
	private List<State> initialStates = new ArrayList<>();
	private List<Event> events = new ArrayList<>();
	private Event null_event;
	private State initial;

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	public List<Variable> getVariables() {
		return variables;
	}

	public void setVariables(List<Variable> variables) {
		this.variables = variables;
	}

	public List<Actuator> getActuators() {
		return actuators;
	}

	public void setBricks(List<Actuator> actuators) {
		this.actuators = actuators;
	}

	public List<State> getStates() {
		return states;
	}

	public void setStates(List<State> states) {
		this.states = states;
	}

	public State getInitial() {
		return initial;
	}

	public void setInitial(State initial) {
		this.initial = initial;
	}

	public List<State> getInitialStates() { return initialStates; }

	public void setInitialStates(List<State> initialStates) { this.initialStates = initialStates; }

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

    public Event getNull_event() {
        return null_event;
    }

    public void setNull_event(Event null_event) {
        this.null_event = null_event;
    }

	public List<Event> getEvents() {
		return events;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}

	public List<String> getMachines() {
		return machines;
	}

	public void setMachines(List<String> machines) {
		this.machines = machines;
	}
}
