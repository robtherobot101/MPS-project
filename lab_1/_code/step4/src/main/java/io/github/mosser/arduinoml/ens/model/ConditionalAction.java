package io.github.mosser.arduinoml.ens.model;

import io.github.mosser.arduinoml.ens.generator.Visitor;


public class ConditionalAction extends Action {

    private Variable variable;
    private Action[] actions;

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

    public void setActions(Action[] actions) {
        this.actions = actions;
    }

    public Action[] getActions() {
        return actions;
    }
}
