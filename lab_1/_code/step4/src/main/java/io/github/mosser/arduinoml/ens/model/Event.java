package io.github.mosser.arduinoml.ens.model;

import io.github.mosser.arduinoml.ens.generator.Visitable;
import io.github.mosser.arduinoml.ens.generator.Visitor;

/**
 * Represents some event which can be fired and reacted to by different state machines
 */
public class Event implements Visitable {

    private String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (!name.endsWith("_event")) {
            name = name + "_event";
        }
        this.name = name.toUpperCase();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
