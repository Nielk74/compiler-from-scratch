package fr.ensimag.deca.codegen;

import java.util.HashMap;
import java.util.Map;

import fr.ensimag.ima.pseudocode.Label;

/**
 * Label Manager, it manages the creation and retrieval of labels for different types of jumps.
 */
public class LabelManager {
    // Data structure to store all the labels
    private Map<String, Label> map = new HashMap<String, Label>();
    // Counter for the if
    private int ifLabelCounter = 0;
    // Counter for the while
    private int whileLabelCounter = 0;
    // Counter for the conditions
    private int condLabelCounter = 0;
    // Counter for the underflow
    private int underflowCounter = 0;
    // Counter for the instanceOf
    private int instanceOfCounter = 0;
    // Counter for the end of methods
    private int endMethodCounter = 0;

    /**
     * Create a label if it doesn't exist in the hashmap
     * @param name Name of the label. 
     */
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

    /**
     * Create a label for an if condition. 
     */
    public int createIfThenElseLabel() {
        String name = "if_" + Integer.toString(ifLabelCounter);
        createLabel(name);
        name = "else_" + Integer.toString(ifLabelCounter);
        createLabel(name);
        name = "end_if_" + Integer.toString(ifLabelCounter);
        createLabel(name);
        return ifLabelCounter++;
    }

    /**
     * Create a label for a while condition. 
     */
    public int createWhileLabel() {
        String name = "while_" + Integer.toString(whileLabelCounter);
        createLabel(name);
        name = "end_while_" + Integer.toString(whileLabelCounter);
        createLabel(name);
        return whileLabelCounter++;
    }

    /**
     * Create a label for a condition. 
     */
    public Label createCondLabel() {
        String name = "end_cond_" + Integer.toString(condLabelCounter);
        Label l = createLabel(name);
        condLabelCounter++;
        return l;
    }

    /**
     * Create a label for an instanceOf. 
     */
    public int createInstanceOfLabel() {
        String name = "instanceof_trueBranch_" + Integer.toString(instanceOfCounter);
        createLabel(name);
        name = "instanceof_end_" + Integer.toString(instanceOfCounter);
        createLabel(name);
        return instanceOfCounter++;
    }

    /**
     * Create a label for the end of a method. 
     */
    public Label createEndMethodLabel() {
        String name = "end_method_" + Integer.toString(endMethodCounter);
        endMethodCounter++;
        return createLabel(name);
    }

    /**
     * Increments the counter of underflow. 
     */
    public void incrementUnderflowCounter(){
        this.underflowCounter++;
    }

    /**
     * Return the value of the counter of undeflow. 
     */
    public int getUnderflowCounter(){
        return this.underflowCounter;
    }

    /**
     * Return the value of the counter of ends of methods. 
     */
    public int getEndMethodCounter() {
        return endMethodCounter;
    }

    /**
     * Return the label corresponding to the parameter name. 
     * @param name Name of the label 
     */
    public Label getLabel(String name) {
        return map.get(name);
    }

}
