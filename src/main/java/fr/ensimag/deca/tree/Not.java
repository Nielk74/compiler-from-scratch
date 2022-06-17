package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
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
public class Not extends AbstractUnaryExpr {

    public Not(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        this.setType(compiler.environmentType.BOOLEAN);
        this.getOperand().verifyCondition(compiler, localEnv, currentClass);
        return this.getType();
    }

    @Override
    protected void codeGenCondition(DecacCompiler compiler, boolean negative, Label l) {
        this.getOperand().codeGenCondition(compiler, !negative, l);
    }

    @Override
    public void codeGenExp(DecacCompiler compiler, int register_name) {
        int labelNum = compiler.labelManager.createIfThenElseLabel();
        Label ifLabel = compiler.labelManager.getLabel("if_" + Integer.toString(labelNum));
        Label elseLabel = compiler.labelManager.getLabel("else_" + Integer.toString(labelNum));
        Label endIfLabel = compiler.labelManager.getLabel("end_if_" + Integer.toString(labelNum));
        
        // if the expression is true, load 1 in the register
        compiler.addLabel(ifLabel);
        this.codeGenCondition(compiler, false, elseLabel);
        compiler.addInstruction(new LOAD(1, Register.getR(register_name)));
        compiler.addInstruction(new BRA(endIfLabel));

        // else, load 0 in the register
        compiler.addLabel(elseLabel);
        compiler.addInstruction(new LOAD(0, Register.getR(register_name)));
        compiler.addLabel(endIfLabel);
    }
    @Override
    protected String getOperatorName() {
        return "!";
    }
}
