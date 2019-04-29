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
//public class NewLed {
//
//    public static void main(String[] args) {
//
//        // Name of the state machine
//        String stateMachineName = "led";
//
//
//        Actuator led = new Actuator();
//        led.setName("LED");
//        led.setPin(12);
//        led.setMode(MODE.OUTPUT);
//
//
//        // Creating transitions
//        Transition turnOn = new Transition();
//        turnOn.setEventTrigger(EVENT.NULL_EVENT);
//        turnOn.setName(stateMachineName);
//
//        Transition turnOff = new Transition();
//        turnOff.setEventTrigger(EVENT.NULL_EVENT);
//        turnOff.setName(stateMachineName);
//
//        // Declaring states - Note we must prefix the name of the state with the name of the app (state machine)
//        State on = new State();
//        on.setName(String.format("%s_state_on", stateMachineName));
//
//        State off = new State();
//        off.setName(String.format("%s_state_off", stateMachineName));
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
//        // Binding actions to states
//        on.setActions(Arrays.asList(switchTheLightOn));
//        off.setActions(Arrays.asList(switchTheLightOff));
//
//
//        // Adding targets to transitions
//        turnOn.setTarget(on);
//        turnOff.setTarget(off);
//
//        // Binding transitions to states
//        on.setTransitions((Arrays.asList(turnOff)));
//        off.setTransitions((Arrays.asList(turnOn)));
//
//        // Building the StateMachine
//        StateMachine theSwitch = new StateMachine();
//        theSwitch.setName(stateMachineName);
//        theSwitch.setBricks(Arrays.asList(led));
//        theSwitch.setStates(Arrays.asList(on, off));
//        theSwitch.setInitial(on);
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
