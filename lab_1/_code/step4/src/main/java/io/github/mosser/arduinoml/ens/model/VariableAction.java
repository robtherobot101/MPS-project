package io.github.mosser.arduinoml.ens.model;

import io.github.mosser.arduinoml.ens.generator.Visitor;

public class VariableAction implements Actionable {

    private Variable variable;
    private VACTIONTYPE action_type;
    private String new_value;

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

    public VACTIONTYPE getAction_type() {
        return action_type;
    }

    public void setAction_type(VACTIONTYPE action_type) {
        this.action_type = action_type;
    }

    public String getNew_value() {
        return new_value;
    }

    public void setNew_value(String new_value) {
        this.new_value = new_value;
    }
}
