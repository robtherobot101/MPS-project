package io.github.mosser.arduinoml.ens.model;

import io.github.mosser.arduinoml.ens.generator.Visitable;
import io.github.mosser.arduinoml.ens.generator.Visitor;


public class Sensor implements Visitable {
//    private String name;
//    private int pin;
//
//    public int getPin() {
//        return pin;
//    }
//    public void setPin(int pin) {
//        this.pin = pin;
//    }
//
//    @Override
//    public void accept(Visitor visitor) {
//        visitor.visit(this);
//    }
//
//    @Override
//    public String getName() {
//        return name;
//    }
//
//    @Override
//    public void setName(String name) {
//        this.name = name;
//    }

    private SIGNAL value;
    private Actuator actuator;


    public SIGNAL getValue() {
        return value;
    }

    public void setValue(SIGNAL value) {
        this.value = value;
    }

    public Actuator getActuator() {
        return actuator;
    }

    public void setActuator(Actuator actuator) {
        this.actuator = actuator;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
