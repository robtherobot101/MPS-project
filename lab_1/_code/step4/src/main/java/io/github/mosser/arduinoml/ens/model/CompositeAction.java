package io.github.mosser.arduinoml.ens.model;

import io.github.mosser.arduinoml.ens.generator.Visitor;

public class CompositeAction extends Action {

    private SIGNAL[] values;
    private Actuator[] actuators;


    public SIGNAL[] getValues() {
        return values;
    }

    public void setValues(SIGNAL[] values) {
        this.values = values;
    }

    public Actuator[] getActuators() {
        return actuators;
    }

    public void setActuators(Actuator[] actuators) {
        this.actuators = actuators;
    }

    @Override
    public void accept(Visitor visitor) {
        for (Actuator actuator : actuators) {
            visitor.visit(actuator);
        }
    }
}
