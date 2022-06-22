package fr.ensimag.deca.tree;


/**
 * Abstract class for all the inequality operators.
 * Inequality operators : lower, lower or equals, greater,
 * greater or equals
 * 
 * @author gl10
 * 
 */
public abstract class AbstractOpIneq extends AbstractOpCmp {

    /**
     * @param leftOperand
     * @param rightOperand
     */
    public AbstractOpIneq(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }
}
