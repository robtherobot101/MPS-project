package io.github.mosser.arduinoml.ens.model;

import io.github.mosser.arduinoml.ens.generator.Visitable;
import io.github.mosser.arduinoml.ens.generator.Visitor;

import java.util.ArrayList;
import java.util.List;

public class State implements NamedElement, Visitable {

    private String name; // might not need this
    private EVENT event;
    private Sensor trigger;
    private State target;

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

    public EVENT getEvent() { return event; }

    public void setEvent(EVENT event) { this.event = event; }

    public State getTarget() { return target; }

    public void setTarget(io.github.mosser.arduinoml.ens.model.State target) { this.target = target; }

    public Sensor getTrigger() { return trigger; }

    public void setTrigger(Sensor trigger) { this.trigger = trigger; }
}