package fr.ensimag.deca.tree;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.deca.DecacCompiler;

/**
 * The or operator.
 *
 * @author gl10
 * 
 */
public class Or extends AbstractOpBool {

    /**
     * @param leftOperand
     * @param rightOperand
     */
    public Or(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getOperatorName() {
        return "||";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void codeGenCondition(DecacCompiler compiler, boolean negative, Label l) {
        if (negative) {
            this.getLeftOperand().codeGenCondition(compiler, negative, l);
            this.getRightOperand().codeGenCondition(compiler, negative, l);
        } else {
            Label end = compiler.labelManager.createCondLabel();
            this.getLeftOperand().codeGenCondition(compiler, !negative, end);
            this.getRightOperand().codeGenCondition(compiler, negative, l);
            compiler.addLabel(end);
        }
    }
}
