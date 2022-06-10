package fr.ensimag.deca.tree;

import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 *
 * @author gl10
 * @date 25/04/2022
 */
public class And extends AbstractOpBool {

    public And(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "&&";
    }

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

    @Override
    public void codeGenExp(DecacCompiler compiler, int register_name){
        getLeftOperand().codeGenExp(compiler, register_name);
        getRightOperand().codeGenExp(compiler, register_name + 1);
        int labelNum = compiler.labelManager.createIfThenElseLabel();
        Label ifLabel = compiler.labelManager.getLabel("if_" + Integer.toString(labelNum));
        Label elseLabel = compiler.labelManager.getLabel("else_" + Integer.toString(labelNum));
        Label endIfLabel = compiler.labelManager.getLabel("end_if_" + Integer.toString(labelNum));
        compiler.addLabel(ifLabel);
        compiler.addInstruction(new CMP(0, Register.getR(register_name)));
        compiler.addInstruction(new BEQ(elseLabel));
        compiler.addInstruction(new CMP(0, Register.getR(register_name + 1)));
        compiler.addInstruction(new BEQ(elseLabel));
        compiler.addInstruction(new LOAD(1, Register.getR(register_name)));
        compiler.addInstruction(new BRA(endIfLabel));
        compiler.addLabel(elseLabel);
        compiler.addInstruction(new LOAD(0, Register.getR(register_name)));
        compiler.addLabel(endIfLabel);
    }
}
