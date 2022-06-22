package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 * Operator equals
 *
 * @author gl10
 * 
 */
public class Equals extends AbstractOpExactCmp {

    /**
     * @param leftOperand Left operand of the equality
     * @param rightOperand Right operand of the equality
     */
    public Equals(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getOperatorName() {
        return "==";
    }    
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void codeGenCondition(DecacCompiler compiler, boolean negative, Label l) {
        super.codeGenCondition(compiler, negative, l);
        compiler.addInstruction(negative ? new BEQ(l) : new BNE(l));
    }
}
