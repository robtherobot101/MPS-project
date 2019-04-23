package io.github.mosser.arduinoml.ens.model;

import io.github.mosser.arduinoml.ens.generator.Visitor;

import java.util.List;

public class CompositeActuator extends Actuator {

    private List<Actuator> actuators;
    private String name;

    public List<Actuator> getActuators() {
        return actuators;
    }

    public void setActuators(List<Actuator> actuators) {
        this.actuators = actuators;
    }

    @Override
    public void accept(Visitor visitor) {
        for (Actuator actuator : actuators) {
            visitor.visit(actuator);
        }
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }


}
