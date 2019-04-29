package io.github.mosser.arduinoml.ens.samples;

import io.github.mosser.arduinoml.ens.generator.ToC;
import io.github.mosser.arduinoml.ens.generator.Visitor;
import io.github.mosser.arduinoml.ens.model.*;
import io.github.mosser.arduinoml.ens.model.Action;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Main {

    public static void main(String[] args) {

        // The outputs required for individual pins in order to represent each of the digits on the seven segment display
        // The first dimension represents the desired number, and the second represents the segment (a through g)
        // For example, NUMBERS[5][3] represents the required output on pin c (3) to correctly display the number 5.
        SIGNAL[][] NUMBERS = {
                {SIGNAL.HIGH, SIGNAL.HIGH, SIGNAL.HIGH, SIGNAL.HIGH, SIGNAL.HIGH, SIGNAL.HIGH, SIGNAL.LOW },    // 0
                {SIGNAL.LOW,  SIGNAL.HIGH, SIGNAL.HIGH, SIGNAL.LOW,  SIGNAL.LOW,  SIGNAL.LOW,  SIGNAL.LOW },    // 1
                {SIGNAL.HIGH, SIGNAL.HIGH, SIGNAL.LOW,  SIGNAL.HIGH, SIGNAL.HIGH, SIGNAL.LOW,  SIGNAL.HIGH},    // 2
                {SIGNAL.HIGH, SIGNAL.HIGH, SIGNAL.HIGH, SIGNAL.HIGH, SIGNAL.LOW,  SIGNAL.LOW,  SIGNAL.HIGH},    // 3
                {SIGNAL.LOW,  SIGNAL.HIGH, SIGNAL.HIGH, SIGNAL.LOW,  SIGNAL.LOW,  SIGNAL.HIGH, SIGNAL.HIGH},    // 4
                {SIGNAL.HIGH, SIGNAL.LOW,  SIGNAL.HIGH, SIGNAL.HIGH, SIGNAL.LOW,  SIGNAL.HIGH, SIGNAL.HIGH},    // 5
                {SIGNAL.HIGH, SIGNAL.LOW,  SIGNAL.HIGH, SIGNAL.HIGH, SIGNAL.HIGH, SIGNAL.HIGH, SIGNAL.HIGH},    // 6
                {SIGNAL.HIGH, SIGNAL.HIGH, SIGNAL.HIGH, SIGNAL.LOW,  SIGNAL.LOW,  SIGNAL.LOW,  SIGNAL.LOW },    // 7
                {SIGNAL.HIGH, SIGNAL.HIGH, SIGNAL.HIGH, SIGNAL.HIGH, SIGNAL.HIGH, SIGNAL.HIGH, SIGNAL.HIGH},    // 8
                {SIGNAL.HIGH, SIGNAL.HIGH, SIGNAL.HIGH, SIGNAL.HIGH, SIGNAL.LOW,  SIGNAL.HIGH, SIGNAL.HIGH},    // 9
        };

        String sevenSegStateMachineName = "sevenseg";
        String buttonStateMachineName = "button";
        String ledStateMachineName = "led";

        ////////////////////////////////
        ////////////VARIABLES///////////
        ////////////////////////////////

        Variable count = new Variable();
        count.setType(TYPE.INTEGER);
        count.setName("count");
        count.setInitialValue("0");

        Variable sevenSegLastUpdated = new Variable();
        sevenSegLastUpdated.setType(TYPE.LONG);
        sevenSegLastUpdated.setName("seven_seg_last_updated");
        sevenSegLastUpdated.setInitialValue("0");

        ////////////////////////////////
        ////////////ACTUATORS///////////
        ////////////////////////////////

        // ACTUATORS FOR SELECTING SINGLE 7-SEGMENT DIGIT
        Actuator seven_seg_prop1 = new Actuator();
        seven_seg_prop1.setName("SEVENSEG_PROP1");
        seven_seg_prop1.setPin(8);
        seven_seg_prop1.setMode(MODE.OUTPUT);

        Actuator seven_seg_prop2 = new Actuator();
        seven_seg_prop2.setName("SEVENSEG_PROP2");
        seven_seg_prop2.setPin(9);
        seven_seg_prop2.setMode(MODE.OUTPUT);

        // ACTUATORS FOR SINGLE SEGMENTS
        Actuator[] sevenSegActuators = new Actuator[7];
        for (int i = 0; i < 7; i++) {
            Actuator actuator = new Actuator();
            actuator.setName("pin_" + (i + 1));
            actuator.setPin(i + 1);
            actuator.setMode(MODE.OUTPUT);
            sevenSegActuators[i] = actuator;
        }

        // LED ACTUATOR
        Actuator led = new Actuator();
        led.setName("led");
        led.setPin(12);
        led.setMode(MODE.OUTPUT);

        // BUTTON ACTUATOR
        Actuator button = new Actuator();
        button.setName("button");
        button.setPin(10);
        button.setMode(MODE.INPUT);


        ////////////////////////////////
        /////////////EVENTS/////////////
        ////////////////////////////////

        Event null_event = new Event();
        null_event.setName("null");
        Event buttonPressed = new Event();
        buttonPressed.setName("button_pressed");
        Event buttonReleased = new Event();
        buttonReleased.setName("button_released");
        Event sevenSegOverflow = new Event();
        sevenSegOverflow.setName("sevenseg_reset");


        ////////////////////////////////
        /////////////SENSORS////////////
        ////////////////////////////////

        Sensor isPressed = new Sensor();
        isPressed.setActuator(button);
        isPressed.setValue(SIGNAL.LOW);
        isPressed.setEvent(buttonPressed);

        Sensor isReleased = new Sensor();
        isReleased.setActuator(button);
        isReleased.setValue(SIGNAL.HIGH);
        isReleased.setEvent(buttonReleased);



        ////////////////////////////////
        /////////////ACTIONS////////////
        ////////////////////////////////

        // LED
        ActuatorAction switchTheLightOn = new ActuatorAction();
        switchTheLightOn.setActuator(led);
        switchTheLightOn.setValue(SIGNAL.HIGH);

        ActuatorAction switchTheLightOff = new ActuatorAction();
        switchTheLightOff.setActuator(led);
        switchTheLightOff.setValue(SIGNAL.LOW);

        // SEVEN SEGMENT DISPLAY SELECTORS
        ActuatorAction set_prop1 = new ActuatorAction();
        set_prop1.setActuator(seven_seg_prop1);
        set_prop1.setValue(SIGNAL.LOW);

        ActuatorAction set_prop2 = new ActuatorAction();
        set_prop2.setActuator(seven_seg_prop2);
        set_prop2.setValue(SIGNAL.HIGH);

        // SINGLE SEGMENT ACTIONS
        ActuatorAction[][] sevenSegActions = new ActuatorAction[7][10];
        for (int i = 0; i < NUMBERS.length; i++) {
            for (int j = 0; j < 7; j++) {
                ActuatorAction action = new ActuatorAction();
                action.setActuator(sevenSegActuators[j]);
                action.setValue(NUMBERS[i][j]);
                sevenSegActions[j][i] = action;
            }
        }

        ConditionalAction[] conditionalActions = new ConditionalAction[7];
        for (int i = 0; i < conditionalActions.length; i++) {
            ConditionalAction conditionalAction = new ConditionalAction();
            conditionalAction.setVariable(count);
            conditionalAction.setActions(sevenSegActions[i]);
            conditionalActions[i] = conditionalAction;
        }

        VariableAction resetCount = new VariableAction();
        resetCount.setVariable(count);
        resetCount.setAction_type(VACTIONTYPE.SET);
        resetCount.setNew_value("0");

        VariableAction incrementCount = new VariableAction();
        incrementCount.setVariable(count);
        incrementCount.setAction_type(VACTIONTYPE.INCREMENT);

        DelayedAction IncrementAfterDelay = new DelayedAction();
        IncrementAfterDelay.setDelay(1000);
        IncrementAfterDelay.setTimer(sevenSegLastUpdated);
        IncrementAfterDelay.setAction(incrementCount);


        ////////////////////////////////
        //////////////STATES////////////
        ////////////////////////////////

        // LED
        State ledOn = new State();
        ledOn.setName(String.format("%s_state_on", ledStateMachineName));

        State ledOff = new State();
        ledOff.setName(String.format("%s_state_off", ledStateMachineName));

        // BUTTON
        State buttonUp = new State();
        buttonUp.setName(String.format("%s_state_up", buttonStateMachineName));
        buttonUp.setTriggers(Arrays.asList(isPressed));

        State buttonDown = new State();
        buttonDown.setName(String.format("%s_state_down", buttonStateMachineName));
        buttonDown.setTriggers(Arrays.asList(isReleased));

        // SEVEN SEGMENT DISPLAY
        State sevenSegInitialise = new State();
        sevenSegInitialise.setName(String.format("%s_state_initialising", sevenSegStateMachineName));

        State sevenSegCounting = new State();
        sevenSegCounting.setName(String.format("%s_state_counting", sevenSegStateMachineName));
        sevenSegCounting.setVariables(Arrays.asList(count, sevenSegLastUpdated));

        State sevenSegReset = new State();
        sevenSegReset.setName(String.format("%s_state_reset", sevenSegStateMachineName));

        ////////////////////////////////
        ///////////TRANSITIONS//////////
        ////////////////////////////////

        // SEVEN SEGMENT
        Transition initialiseFinished = new Transition();
        initialiseFinished.setName(sevenSegStateMachineName);
        initialiseFinished.setEventTrigger(null_event);
        initialiseFinished.setTarget(sevenSegCounting);

        Transition countOverflown = new Transition();
        countOverflown.setName(sevenSegStateMachineName);
        countOverflown.setEventTrigger(sevenSegOverflow);
        countOverflown.setTarget(sevenSegReset);
        countOverflown.setAction(resetCount);

        Transition resetAfterButton = new Transition();
        resetAfterButton.setName(sevenSegStateMachineName);
        resetAfterButton.setEventTrigger(buttonPressed);
        resetAfterButton.setTarget(sevenSegReset);
        resetAfterButton.setAction(resetCount);

        Transition countReset = new Transition();
        countReset.setName(sevenSegStateMachineName);
        countReset.setEventTrigger(null_event);
        countReset.setTarget(sevenSegCounting);

        sevenSegInitialise.setTransitions(Arrays.asList(initialiseFinished));
        sevenSegCounting.setTransitions(Arrays.asList(countOverflown, resetAfterButton));
        sevenSegReset.setTransitions(Arrays.asList(countReset));

        // LED
        Transition turnOn = new Transition();
        turnOn.setEventTrigger(buttonPressed);
        turnOn.setName(ledStateMachineName);
        turnOn.setTarget(ledOn);

        Transition turnOff = new Transition();
        turnOff.setEventTrigger(buttonPressed);
        turnOff.setName(ledStateMachineName);
        turnOff.setTarget(ledOff);

        ledOff.setTransitions(Arrays.asList(turnOn));
        ledOn.setTransitions(Arrays.asList(turnOff));

        // BUTTON
        Transition push = new Transition();
        push.setEventTrigger(buttonPressed);
        push.setName(buttonStateMachineName);
        push.setTarget(buttonDown);

        Transition release = new Transition();
        release.setEventTrigger(buttonReleased);
        release.setName(buttonStateMachineName);
        release.setTarget(buttonUp);

        buttonDown.setTransitions(Arrays.asList(release));
        buttonUp.setTransitions(Arrays.asList(push));

        // BINDING ACTIONS TO STATES

        // DIGIT SELECTING
        sevenSegInitialise.setActions(Arrays.asList(set_prop1, set_prop2));

        // SINGLE SEGMENTS
        List<Action> actions = new ArrayList<>(Arrays.asList(conditionalActions));
        actions.add(IncrementAfterDelay);
        sevenSegCounting.setActions(actions);

        // EVENT TRIGGERS
        //SEVENSEG RESET
        VariableGreaterTrigger variableGreaterTrigger = new VariableGreaterTrigger();
        variableGreaterTrigger.setEvent(sevenSegOverflow);
        variableGreaterTrigger.setThreshold(9);
        variableGreaterTrigger.setVariable(count);
        sevenSegCounting.setTriggers(Arrays.asList(variableGreaterTrigger));



        // LED
        ledOn.setActions(Arrays.asList(switchTheLightOn));
        ledOff.setActions(Arrays.asList(switchTheLightOff));





        // Building the StateMachine



        // LED State Machine

        StateMachine ledSM = new StateMachine();
        ledSM.setName(ledStateMachineName);

        ledSM.setBricks((Arrays.asList(led)));

        ledSM.setEvents(Arrays.asList(null_event));
        ledSM.setNull_event(null_event);

        ledSM.setStates(Arrays.asList(ledOn, ledOff));
        ledSM.setInitialStates(Arrays.asList(ledOn));

        ledSM.setInitial(ledOn);



        //SevenSeg State Machine


        StateMachine sevenSegSM = new StateMachine();
        sevenSegSM.setName(sevenSegStateMachineName);

        sevenSegSM.setVariables(Arrays.asList(count, sevenSegLastUpdated));

        List<Actuator> allActuators = new ArrayList<>(Arrays.asList(sevenSegActuators));
        allActuators.add(seven_seg_prop1);
        allActuators.add(seven_seg_prop2);
        sevenSegSM.setBricks(allActuators);

        sevenSegSM.setEvents(Arrays.asList(sevenSegOverflow));
        sevenSegSM.setNull_event(null_event);

        sevenSegSM.setStates(Arrays.asList(sevenSegInitialise, sevenSegCounting, sevenSegReset));
        sevenSegSM.setInitialStates(Arrays.asList(sevenSegInitialise));


        sevenSegSM.setInitial(sevenSegInitialise);


        // Button State Machine

        StateMachine buttonSM = new StateMachine();
        buttonSM.setName(buttonStateMachineName);
        buttonSM.setMachines(Arrays.asList(ledSM, sevenSegSM));


        buttonSM.setBricks(Arrays.asList(button));

        buttonSM.setEvents(Arrays.asList(buttonPressed, buttonReleased));
        buttonSM.setNull_event(null_event);

        buttonSM.setStates(Arrays.asList(buttonDown, buttonUp));
        buttonSM.setInitialStates(Arrays.asList(buttonUp));

        buttonSM.setInitial(buttonUp);



        // Generating Code
        Visitor codeGenerator = new ToC();
        buttonSM.accept(codeGenerator);



        // Writing C files
        try {
            System.out.println("Generating C code: ./output/fsm.h");
            Files.write(Paths.get("./output/fsm.h"), codeGenerator.getHeaders().toString().getBytes());
            System.out.println("Generating C code: ./output/main.c");
            Files.write(Paths.get("./output/main.c"), codeGenerator.getCode().toString().getBytes());
            System.out.println("Code generation: done");
            System.out.println("Board upload : cd output && make upload && cd ..;");
        } catch (IOException e) {
            System.err.println(e);
        }

    }

}
