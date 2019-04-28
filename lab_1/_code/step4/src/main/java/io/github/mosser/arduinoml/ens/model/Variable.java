package io.github.mosser.arduinoml.ens.model;

import io.github.mosser.arduinoml.ens.generator.Visitable;
import io.github.mosser.arduinoml.ens.generator.Visitor;

public class Variable implements NamedElement, Visitable {

    private String name;
    private String initialValue;
    private TYPE type;

    public String getInitialValue() {
        return initialValue;
    }

    public void setInitialValue(String initialValue) {
        this.initialValue = initialValue;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }
}
