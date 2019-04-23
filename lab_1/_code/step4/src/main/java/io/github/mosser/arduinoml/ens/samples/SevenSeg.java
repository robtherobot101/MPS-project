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
                {SIGNAL.LOW,  SIGNAL.LOW,  SIGNAL.LOW,  SIGNAL.LOW,  SIGNAL.LOW,  SIGNAL.LOW,  SIGNAL.LOW }
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

        CompositeActuator seven_seg = new CompositeActuator();
        seven_seg.setName("7SEG");
        List<Actuator> actuators = new ArrayList<>();
        for (int i = 1; i < 8; i++) {
            Actuator actuator = new Actuator();
            actuator.setName(String.format("Segment, %d", i));
            actuator.setPin(i);
            actuators.add(actuator);
        }
        seven_seg.setActuators(actuators);

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
        CompositeAction zero = new CompositeAction();
        zero.setValues(NUMBERS[0]);
        CompositeAction one = new CompositeAction();
        one.setValues(NUMBERS[1]);
        CompositeAction two = new CompositeAction();
        two.setValues(NUMBERS[2]);
        CompositeAction three = new CompositeAction();
        three.setValues(NUMBERS[3]);
        CompositeAction four = new CompositeAction();
        four.setValues(NUMBERS[4]);
        CompositeAction five = new CompositeAction();
        five.setValues(NUMBERS[5]);
        CompositeAction six = new CompositeAction();
        six.setValues(NUMBERS[6]);
        CompositeAction seven = new CompositeAction();
        seven.setValues(NUMBERS[7]);
        CompositeAction eight = new CompositeAction();
        eight.setValues(NUMBERS[8]);
        CompositeAction nine = new CompositeAction();
        nine.setValues(NUMBERS[9]);
        CompositeAction[] sevenSegActions = {zero, one, two, three, four, five, six, seven, eight, nine};
        ConditionalAction sevenSegAction = new ConditionalAction();
        sevenSegAction.setVariable(count);
        sevenSegAction.setActions(sevenSegActions);

        // Binding actions to states
        initialise.setActions(Arrays.asList(set_prop1, set_prop2));
        counting.setActions(Arrays.asList(sevenSegActions));

        // Binding transitions to states
        initialise.setNext(counting);
        counting.setNext(counting);

        // Building the App
        App theSwitch = new App();
        theSwitch.setName("7SEG!");
        theSwitch.setVariables(Arrays.asList(count));
        theSwitch.setBricks(Arrays.asList(seven_seg, seven_seg_prop1, seven_seg_prop2));
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
