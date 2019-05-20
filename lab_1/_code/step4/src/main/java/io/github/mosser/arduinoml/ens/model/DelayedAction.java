package io.github.mosser.arduinoml.ens.model;

import io.github.mosser.arduinoml.ens.generator.Visitor;

/**
 * An action which is only executed after a certain amount of time has elapsed since its last execution
 */
public class DelayedAction extends Action {

    private Variable timer;
    private int delay;
    private Action action;

    public void setTimer(Variable timer) {
        this.timer = timer;
    }

    public Variable getTimer() {
        return timer;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getDelay() {
        return delay;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Action getAction() {
        return action;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
