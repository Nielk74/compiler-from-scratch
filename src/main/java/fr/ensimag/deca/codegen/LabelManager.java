package fr.ensimag.deca.codegen;

import java.util.HashMap;
import java.util.Map;

import fr.ensimag.ima.pseudocode.Label;

public class LabelManager {
    private Map<String, Label> map = new HashMap<String, Label>();
    private static int ifLabelCounter = 0;
    private static int whileLabelCounter = 0;

    public Label createLabel(String name) {
        if (name == null)
            throw new IllegalArgumentException("name should not be null");
        Label l = map.get(name);
        if (l == null) {
            l = new Label(name);
            map.put(name, l);
        }
        return l;
    }

    public int createIfThenElseLabel() {
        String name = "if_" + Integer.toString(ifLabelCounter);
        map.put(name, new Label(name));
        name = "else_" + Integer.toString(ifLabelCounter);
        map.put(name, new Label(name));
        name = "end_if_" + Integer.toString(ifLabelCounter);
        map.put(name, new Label(name));
        return ifLabelCounter++;
    }

    public int createWhileLabel() {
        String name = "while_" + Integer.toString(whileLabelCounter);
        map.put(name, new Label(name));
        name = "end_while_" + Integer.toString(whileLabelCounter);
        map.put(name, new Label(name));
        return whileLabelCounter++;
    }

    public Label getLabel(String name) {
        return map.get(name);
    }
}
