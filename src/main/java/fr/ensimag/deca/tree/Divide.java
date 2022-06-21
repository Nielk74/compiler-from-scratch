package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.ErrorCatcher;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.DIV;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 *
 * @author gl10
 * @date 25/04/2022
 */
public class Divide extends AbstractOpArith {
    public Divide(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "/";
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        super.verifyExpr(compiler, localEnv, currentClass);
        Type leftType = super.getLeftOperand().getType();
        Type rightType = super.getRightOperand().getType();

        // convert int operands to float for divide
        if (leftType.isInt()) {
            AbstractExpr expr = getLeftOperand().verifyRValue(compiler, localEnv, currentClass, compiler.environmentType.FLOAT);
            this.setLeftOperand(expr);
        }

        if (rightType.isInt()) {
            AbstractExpr expr = getRightOperand().verifyRValue(compiler, localEnv, currentClass, compiler.environmentType.FLOAT);
            this.setRightOperand(expr);
        }

        this.setType(compiler.environmentType.FLOAT);
        return this.getType();
    }

    @Override
    protected void codeGenExp(DecacCompiler compiler, int register_name) {
        super.codeGenExp(compiler, register_name);
        GPRegister leftRegister;
        if (compiler.getCompilerOptions().getRegisterMax() == register_name) {
            leftRegister = Register.R1;
        } else {
            leftRegister = Register.getR(register_name + 1);
        }
        GPRegister rightRegister = Register.getR(register_name);
        String index = Integer.toString(compiler.labelManager.getUnderflowCounter());
        Label no_underflow = compiler.labelManager.createLabel("no_underflow" + index);
        Label end_division = compiler.labelManager.createLabel("end_division" + Integer.toString(compiler.labelManager.getUnderflowCounter()));
        if (this.getType().isFloat() && !compiler.getCompilerOptions().getNocheck()) {
            compiler.addInstruction(new CMP(new ImmediateFloat(0), rightRegister));
            compiler.addInstruction(new BEQ(no_underflow));
        }
        compiler.addInstruction(new DIV(leftRegister, rightRegister));
        if (!compiler.getCompilerOptions().getNocheck()) {
            compiler.addInstruction(new BOV(compiler.labelManager.getLabel(ErrorCatcher.OV_ERROR)));
            compiler.addInstruction(new CMP(new ImmediateFloat(0), rightRegister));
            compiler.addInstruction(new BEQ(compiler.labelManager.getLabel(ErrorCatcher.UD_ERROR)));
            compiler.addInstruction(new BRA(end_division));
            compiler.addLabel(no_underflow);
            compiler.addInstruction(new LOAD(new ImmediateFloat(0), rightRegister));
            compiler.addLabel(end_division);
            compiler.labelManager.incrementUnderflowCounter();
        }
    }
}
