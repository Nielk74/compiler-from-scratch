package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 * The not equals operator.
 *
 * @author gl10
 * 
 */
public class NotEquals extends AbstractOpExactCmp {

    /**
     * @param leftOperand
     * @param rightOperand
     */
    public NotEquals(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getOperatorName() {
        return "!=";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void codeGenCondition(DecacCompiler compiler, boolean negative, Label l) {
        super.codeGenCondition(compiler, negative, l);
        compiler.addInstruction(negative ? new BNE(l) : new BEQ(l));
    }
}
