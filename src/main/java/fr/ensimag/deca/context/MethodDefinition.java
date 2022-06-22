package fr.ensimag.deca.context;

import fr.ensimag.deca.tree.Location;
import fr.ensimag.ima.pseudocode.Label;
import org.apache.commons.lang.Validate;

/**
 * Definition of a method
 *
 * @author gl10
 * 
 */
public class MethodDefinition extends ExpDefinition {

    /**
     * 
     * @param type Return type of the method
     * @param location Location of the declaration of the method
     * @param signature List of arguments of the method
     * @param index Index of the method in the class. Starts from 0.
     */
    public MethodDefinition(Type type, Location location, Signature signature, int index) {
        super(type, location);
        this.signature = signature;
        this.index = index;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isMethod() {
        return true;
    }

    
    /**
     * Return the Label of the methode, its name on assembly: code.<classname>.<methodename> if it exists, 
     * otherwise, return "setLabel() should have been called before".
     * @return Label
     */
    public Label getLabel() {
        Validate.isTrue(label != null,
                "setLabel() should have been called before");
        return label;
    }

    
    /** 
     * Set the label of the methode, its name on assembly: code.<classname>.<methodename>.
     * @param label
     */
    public void setLabel(Label label) {
        this.label = label;
    }

    
    /** 
     * Return the value of the current index.
     * @return int
     */
    public int getIndex() {
        return index;
    }

    private int index;

    
    /**
     * {@inheritDoc}
     */
    @Override
    public MethodDefinition asMethodDefinition(String errorMessage, Location l)
            throws ContextualError {
        return this;
    }

    private final Signature signature;
    private Label label;

    /**
     * Returns the Signature of the method
     * 
     * @return Signature
     */
    public Signature getSignature() {
        return signature;
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getNature() {
        return "method";
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isExpression() {
        return false;
    }

}
