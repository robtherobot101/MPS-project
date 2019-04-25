package io.github.mosser.arduinoml.ens.model;

public class Event {

    private String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (!name.endsWith("_event")) {
            name = name + "_event";
        }
        this.name = name.toUpperCase();
    }
}
