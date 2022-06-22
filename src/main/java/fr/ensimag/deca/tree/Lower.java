package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 * Operator Lower than between two AbstractExpr
 * @author gl10
 * 
 */
public class Lower extends AbstractOpIneq {

    /**
    * @param leftOperand
    * @param rightOperand
     */
    public Lower(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getOperatorName() {
        return "<";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void codeGenCondition(DecacCompiler compiler, boolean negative, Label l) {
        super.codeGenCondition(compiler, negative, l);
        compiler.addInstruction(negative ? new BLT(l) : new BGE(l));
    }
}
