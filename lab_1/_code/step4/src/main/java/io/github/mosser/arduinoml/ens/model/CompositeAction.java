package io.github.mosser.arduinoml.ens.model;

import io.github.mosser.arduinoml.ens.generator.Visitable;
import io.github.mosser.arduinoml.ens.generator.Visitor;

import java.util.List;

public class CompositeAction extends Action {

    private SIGNAL[] values;
    private CompositeActuator actuator;


    public SIGNAL[] getValues() {
        return values;
    }

    public void setValues(SIGNAL[] values) {
        this.values = values;
    }

    public Actuator getActuator() {
        return actuator;
    }

    public void setActuator(CompositeActuator actuator) {
        this.actuator = actuator;
    }

    @Override
    public void accept(Visitor visitor) {
        for (int i = 0; i < values.length; i++) {
            Action action = new Action();
            action.setActuator(actuator.getActuators().get(i));
            action.setValue(values[i]);
            visitor.visit(action);
        }
    }
}
