package io.github.mosser.arduinoml.ens.model;

import io.github.mosser.arduinoml.ens.generator.Visitor;


public class ConditionalAction implements Actionable {

    private Variable variable;
    private Actionable[] actions;

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

    public Variable getVariable() {
        return variable;
    }

    public void setVariable(Variable value) {
        this.variable = value;
    }

    public void setActions(Actionable[] actions) {
        this.actions = actions;
    }

    public Actionable[] getActions() {
        return actions;
    }
}
