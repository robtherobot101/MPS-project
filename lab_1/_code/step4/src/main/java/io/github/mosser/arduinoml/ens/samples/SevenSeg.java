package io.github.mosser.arduinoml.ens.samples;

import io.github.mosser.arduinoml.ens.generator.ToC;
import io.github.mosser.arduinoml.ens.generator.Visitor;
import io.github.mosser.arduinoml.ens.model.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SevenSeg {
    public static void main(String[] args) {

        SIGNAL[][] NUMBERS = {
                {SIGNAL.HIGH, SIGNAL.HIGH, SIGNAL.HIGH, SIGNAL.HIGH, SIGNAL.HIGH, SIGNAL.HIGH, SIGNAL.LOW },
                {SIGNAL.LOW , SIGNAL.HIGH, SIGNAL.HIGH, SIGNAL.LOW , SIGNAL.LOW , SIGNAL.LOW , SIGNAL.LOW },
                {SIGNAL.HIGH, SIGNAL.HIGH, SIGNAL.LOW , SIGNAL.HIGH, SIGNAL.HIGH, SIGNAL.LOW , SIGNAL.HIGH},
                {SIGNAL.HIGH, SIGNAL.HIGH, SIGNAL.HIGH, SIGNAL.HIGH, SIGNAL.LOW , SIGNAL.LOW , SIGNAL.HIGH},
                {SIGNAL.LOW , SIGNAL.HIGH, SIGNAL.HIGH, SIGNAL.LOW , SIGNAL.LOW , SIGNAL.HIGH, SIGNAL.HIGH},
                {SIGNAL.HIGH, SIGNAL.LOW , SIGNAL.HIGH, SIGNAL.HIGH, SIGNAL.LOW , SIGNAL.HIGH, SIGNAL.HIGH},
                {SIGNAL.HIGH, SIGNAL.LOW , SIGNAL.HIGH, SIGNAL.HIGH, SIGNAL.HIGH, SIGNAL.HIGH, SIGNAL.HIGH},
                {SIGNAL.HIGH, SIGNAL.HIGH, SIGNAL.HIGH, SIGNAL.LOW , SIGNAL.LOW , SIGNAL.LOW , SIGNAL.LOW },
                {SIGNAL.HIGH, SIGNAL.HIGH, SIGNAL.HIGH, SIGNAL.HIGH, SIGNAL.HIGH, SIGNAL.HIGH, SIGNAL.HIGH},
                {SIGNAL.HIGH, SIGNAL.HIGH, SIGNAL.HIGH, SIGNAL.LOW,  SIGNAL.HIGH, SIGNAL.HIGH, SIGNAL.HIGH},
                //{SIGNAL.LOW,  SIGNAL.LOW,  SIGNAL.LOW,  SIGNAL.LOW,  SIGNAL.LOW,  SIGNAL.LOW,  SIGNAL.LOW }
        };

        Variable count = new Variable();
        count.setName("count");
        count.setValue(0);

        Actuator seven_seg_prop1 = new Actuator();
        seven_seg_prop1.setName("7SEG_PROP1");
        seven_seg_prop1.setPin(8);

        Actuator seven_seg_prop2 = new Actuator();
        seven_seg_prop2.setName("7SEG_PROP2");
        seven_seg_prop2.setPin(9);

        Actuator[] sevenSegActuators = new Actuator[7];
        for (int i = 0; i < 7; i++) {
            Actuator actuator = new Actuator();
            actuator.setPin(i + 1);
            actuator.setName("pin " + (i + 1));
            sevenSegActuators[i] = actuator;
        }

        // Declaring states
        State initialise = new State();
        initialise.setName("Initialising");

        State counting = new State();
        counting.setName("Counting");

        // Creating actions
        Action set_prop1 = new Action();
        set_prop1.setActuator(seven_seg_prop1);
        set_prop1.setValue(SIGNAL.LOW);

        Action set_prop2 = new Action();
        set_prop2.setActuator(seven_seg_prop2);
        set_prop2.setValue(SIGNAL.HIGH);

        // Seven Seg Actions
        Action[][] sevenSegActions = new Action[7][10];
        for (int i = 0; i < NUMBERS.length; i++) {
            for (int j = 0; j < 7; j++) {
                Action action = new Action();
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

        // Binding actions to states
        initialise.setActions(Arrays.asList(set_prop1, set_prop2));
        counting.setActions(Arrays.asList(conditionalActions));

        // Binding transitions to states
        initialise.setNext(counting);
        counting.setNext(counting);

        // Building the App
        App theSwitch = new App();
        theSwitch.setName("7SEG!");
        theSwitch.setVariables(Arrays.asList(count));
        List<Actuator> allActuators = new ArrayList<>(Arrays.asList(sevenSegActuators));
        allActuators.add(seven_seg_prop1);
        allActuators.add(seven_seg_prop2);
        theSwitch.setBricks(allActuators);
        theSwitch.setStates(Arrays.asList(initialise, counting));
        theSwitch.setInitial(initialise);

        // Generating Code
        Visitor codeGenerator = new ToC();
        theSwitch.accept(codeGenerator);

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
