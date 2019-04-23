package io.github.mosser.arduinoml.ens.model;

import com.sun.tools.javac.util.Pair;
import io.github.mosser.arduinoml.ens.generator.Visitable;
import io.github.mosser.arduinoml.ens.generator.Visitor;

import java.util.List;

public class ConditionalAction implements Visitable {

    private Variable variable;
    private CompositeAction[] actions;

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

    public void setActions(CompositeAction[] actions) {
        this.actions = actions;
    }

    public CompositeAction[] getActions() {
        return actions;
    }
}
