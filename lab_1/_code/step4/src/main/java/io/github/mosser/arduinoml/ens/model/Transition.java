package io.github.mosser.arduinoml.ens.model;

import io.github.mosser.arduinoml.ens.generator.Visitable;
import io.github.mosser.arduinoml.ens.generator.Visitor;

public class Transition implements NamedElement, Visitable {

    private String name; // might not need this
    private Event eventTrigger;
    private State target;
    private Action action;

    @Override public String getName() {
        return name;
    }
    @Override public void setName(String name) {
        this.name = name;
    }
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }


    public Event getEventTrigger() { return eventTrigger; }

    public void setEventTrigger(Event eventTrigger) { this.eventTrigger = eventTrigger; }

    public State getTarget() { return target; }

    public void setTarget(State target) { this.target = target; }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }
}