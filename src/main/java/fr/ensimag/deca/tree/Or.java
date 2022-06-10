package fr.ensimag.deca.tree;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

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

    @Override
    public void codeGenExp(DecacCompiler compiler, int register_name){
        AbstractExpr expr = new Not(new And( new Not(getLeftOperand()), new Not (getRightOperand())));
        expr.codeGenExp(compiler, register_name);
    }
}
