package fr.ensimag.deca.context;

import fr.ensimag.deca.tree.Location;
import fr.ensimag.ima.pseudocode.DAddr;

/**
 * Definition associated to identifier in expressions.
 *
 * @author gl10
 * 
 */
public abstract class ExpDefinition extends Definition {

    /**
     * Address of the operand, of the ExpDefinition
     */
    private DAddr operand;

    /**
     * @param type Type of the expression.
     * @param location Location of the expression in the Deca code.
     */
    public ExpDefinition(Type type, Location location) {
        super(type, location);
    }
    
    /** 
     * Set the operand of the expression.
     * 
     * @param operand
     */
    public void setOperand(DAddr operand) {
        this.operand = operand;
    }

    
    /** 
     * Return the address of the operand.
     * 
     * @return DAddr
     */
    public DAddr getOperand() {
        return operand;
    }
}
