package fr.ensimag.deca.tree;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.deca.DecacCompiler;
/**
 *
 * @author gl10
 * @date 25/04/2022
 */
public class Or extends AbstractOpBool {

    public Or(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "||";
    }

    protected void codeGenCondition(DecacCompiler compiler, boolean negative, Label l) {
        Label end = compiler.labelManager.createCondLabel();
        this.getLeftOperand().codeGenCondition(compiler, !negative, end);
        this.getRightOperand().codeGenCondition(compiler, negative, l);
        compiler.addLabel(end);
    }
}
