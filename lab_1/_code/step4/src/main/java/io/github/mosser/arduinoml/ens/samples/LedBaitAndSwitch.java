//package io.github.mosser.arduinoml.ens.samples;
//
//import io.github.mosser.arduinoml.ens.model.*;
//import io.github.mosser.arduinoml.ens.generator.*;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.Arrays;
//
//public class LedBaitAndSwitch {
//
//    public static void main(String[] args) {
//
//
//        // Name of the state machine
//        String ledStateMachineName = "led";
//
//        String buttonStateMachineName = "button";
//
//
//        Actuator led = new Actuator();
//        led.setName("led");
//        led.setPin(12);
//        led.setMode(MODE.OUTPUT);
//
//        Actuator button = new Actuator();
//        button.setName("button");
//        button.setPin(10);
//        button.setMode(MODE.INPUT);
//
//        // Creating Sensors
//        Sensor isPressed = new Sensor();
//        isPressed.setActuator(button);
//        isPressed.setValue(SIGNAL.LOW);
//
//        Sensor isReleased = new Sensor();
//        isReleased.setActuator(button);
//        isReleased.setValue(SIGNAL.HIGH);
//
//
//        // Creating transitions
//        Transition turnOn = new Transition();
//        turnOn.setEventTrigger(EVENT.BUTTON_PRESSED);
//        turnOn.setName(ledStateMachineName);
//
//        Transition turnOff = new Transition();
//        turnOff.setEventTrigger(EVENT.BUTTON_PRESSED);
//        turnOff.setName(ledStateMachineName);
//
//        Transition push = new Transition();
//        push.setEventTrigger(EVENT.BUTTON_RELEASED);
//        push.setName(buttonStateMachineName);
//        push.setTrigger(isPressed);
//
//        Transition release = new Transition();
//        release.setEventTrigger(EVENT.BUTTON_PRESSED);
//        release.setName(buttonStateMachineName);
//        release.setTrigger(isReleased);
//
//
//
//
//        // Declaring states - Note we must prefix the name of the state with the name of the app (state machine)
//        State on = new State();
//        on.setName(String.format("%s_state_on", ledStateMachineName));
//
//        State off = new State();
//        off.setName(String.format("%s_state_off", ledStateMachineName));
//
//        State up = new State();
//        up.setName(String.format("%s_state_up", buttonStateMachineName));
//
//        State down = new State();
//        down.setName(String.format("%s_state_down", buttonStateMachineName));
//
//
//
//        // Creating actions
//        ActuatorAction switchTheLightOn = new ActuatorAction();
//        switchTheLightOn.setActuator(led);
//        switchTheLightOn.setValue(SIGNAL.HIGH);
//
//        ActuatorAction switchTheLightOff = new ActuatorAction();
//        switchTheLightOff.setActuator(led);
//        switchTheLightOff.setValue(SIGNAL.LOW);
//
//
//        // Binding actions to states
//        on.setActions(Arrays.asList(switchTheLightOn));
//        off.setActions(Arrays.asList(switchTheLightOff));
//
//
//        // Adding targets to transitions
//        turnOn.setTarget(on);
//        turnOff.setTarget(off);
//        push.setTarget(up);
//        release.setTarget(down);
//
//        // Binding transitions to states
//        on.setTransitions((Arrays.asList(turnOff)));
//        off.setTransitions((Arrays.asList(turnOn)));
//        up.setTransitions(Arrays.asList(release));
//        down.setTransitions(Arrays.asList(push));
//
//        // Building the App
//        App theSwitch = new App();
//        theSwitch.setName(ledStateMachineName);
//        theSwitch.setBricks(Arrays.asList(led, button));
//        theSwitch.setStates(Arrays.asList(off, on, up, down));
//        theSwitch.setInitialStates(Arrays.asList(on,up));
//
//        // Generating Code
//        Visitor codeGenerator = new ToC();
//        theSwitch.accept(codeGenerator);
//
//        // Writing C files
//        try {
//            System.out.println("Generating C code: ./output/fsm.h");
//            Files.write(Paths.get("./output/fsm.h"), codeGenerator.getHeaders().toString().getBytes());
//            System.out.println("Generating C code: ./output/main.c");
//            Files.write(Paths.get("./output/main.c"), codeGenerator.getCode().toString().getBytes());
//            System.out.println("Code generation: done");
//            System.out.println("Board upload : cd output && make upload && cd ..;");
//        } catch (IOException e) {
//            System.err.println(e);
//        }
//    }
//
//}
