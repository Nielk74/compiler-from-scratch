package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BLT;
import fr.ensimag.ima.pseudocode.instructions.BGE;
/**
 * Operator "x >= y"
 * 
 * @author gl10
 * @date 25/04/2022
 */
public class GreaterOrEqual extends AbstractOpIneq {

    public GreaterOrEqual(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return ">=";
    }

    @Override
    protected void codeGenCondition(DecacCompiler compiler, boolean negative, Label l) {
        super.codeGenCondition(compiler, negative, l);
        compiler.addInstruction(negative ? new BGE(l) : new BLT(l));
    }
}
