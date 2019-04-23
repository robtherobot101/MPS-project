package io.github.mosser.arduinoml.ens.model;

import io.github.mosser.arduinoml.ens.generator.Visitor;

import java.lang.reflect.Array;
import java.util.List;

public class ArrayValue extends Value{

    private String name;
    private List<? extends Value> values;

    public List<? extends Value> getValues() {
        return values;
    }

    public void setValues(List<? extends Value> values) {
        this.values = values;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toC() {
        StringBuilder sb = new StringBuilder();
        String prefix = "{";
        for (Value value : values) {
            sb.append(prefix);
            sb.append(value.toC());
            prefix = ", ";
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public int getDepth() {
        return values.get(0).getDepth() + 1;
    }
}
