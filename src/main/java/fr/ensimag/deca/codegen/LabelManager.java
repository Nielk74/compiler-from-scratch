package fr.ensimag.deca.codegen;

import java.util.HashMap;
import java.util.Map;

import fr.ensimag.ima.pseudocode.Label;

public class LabelManager {
    private Map<String, Label> map = new HashMap<String, Label>();
    private int ifLabelCounter = 0;
    private int whileLabelCounter = 0;
    private int condLabelCounter = 0;
    private int underflowCounter = 0;
    private int instanceOfCounter = 0;
    private int endMethodCounter = 0;

    public Label createLabel(String name) {
        if (name == null)
            throw new IllegalArgumentException("Wrong label type - expected: string ≠ current: null");
        Label l = map.get(name);
        if (l == null) {
            l = new Label(name);
            map.put(name, l);
        }
        return l;
    }

    public int createIfThenElseLabel() {
        String name = "if_" + Integer.toString(ifLabelCounter);
        createLabel(name);
        name = "else_" + Integer.toString(ifLabelCounter);
        createLabel(name);
        name = "end_if_" + Integer.toString(ifLabelCounter);
        createLabel(name);
        return ifLabelCounter++;
    }

    public int createWhileLabel() {
        String name = "while_" + Integer.toString(whileLabelCounter);
        createLabel(name);
        name = "end_while_" + Integer.toString(whileLabelCounter);
        createLabel(name);
        return whileLabelCounter++;
    }

    public Label createCondLabel() {
        String name = "end_cond_" + Integer.toString(condLabelCounter);
        Label l = createLabel(name);
        condLabelCounter++;
        return l;
    }

    public int createInstanceOfLabel() {
        String name = "instanceof_trueBranch_" + Integer.toString(instanceOfCounter);
        createLabel(name);
        name = "instanceof_end_" + Integer.toString(instanceOfCounter);
        createLabel(name);
        return instanceOfCounter++;
    }

    public Label createEndMethodLabel() {
        String name = "end_method" + Integer.toString(endMethodCounter);
        endMethodCounter++;
        return createLabel(name);
    }

    public void incrementUnderflowCounter(){
        this.underflowCounter++;
    }

    public int getUnderflowCounter(){
        return this.underflowCounter;
    }

    public int getEndMethodCounter() {
        return endMethodCounter;
    }

    public Label getLabel(String name) {
        return map.get(name);
    }

}
