package fr.ensimag.deca.tree;

import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.deca.DecacCompiler;

/**
 * Operator and
 * @author gl10
 * 
 */
public class And extends AbstractOpBool {

    /**
     * @param leftOperand Left operand of the expression. 
     * @param rightOperand Right operand of the expression. 
     */
    public And(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getOperatorName() {
        return "&&";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void codeGenCondition(DecacCompiler compiler, boolean negative, Label l) {
        if (negative) {
            Label end = compiler.labelManager.createCondLabel();
            this.getLeftOperand().codeGenCondition(compiler, !negative, end);
            this.getRightOperand().codeGenCondition(compiler, negative, l);
            compiler.addLabel(end);
        } else {
            this.getLeftOperand().codeGenCondition(compiler, negative, l);
            this.getRightOperand().codeGenCondition(compiler, negative, l);
        }
    }
}
