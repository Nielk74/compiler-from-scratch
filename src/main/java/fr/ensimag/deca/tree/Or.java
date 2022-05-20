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
        this.getLeftOperand().codeGenCondition(compiler, !negative, l);
        this.getRightOperand().codeGenCondition(compiler, !negative, l);
    


        this.getLeftOperand().codeGenCondition(compiler, !negative, l);
        this.getRightOperand().codeGenCondition(compiler, !negative, l);
    }
}
