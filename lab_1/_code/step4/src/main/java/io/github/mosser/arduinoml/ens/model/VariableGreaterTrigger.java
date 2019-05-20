package io.github.mosser.arduinoml.ens.model;

import io.github.mosser.arduinoml.ens.generator.Visitable;
import io.github.mosser.arduinoml.ens.generator.Visitor;

/**
 * Fires an event when the given variable exceeds the given threshold
 */
public class VariableGreaterTrigger extends Trigger {

    private Variable variable;
    private int threshold;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public Variable getVariable() {
        return variable;
    }

    public void setVariable(Variable variable) {
        this.variable = variable;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }
}
