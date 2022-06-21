package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.ErrorCatcher;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.MUL;

/**
 * @author gl10
 * @date 25/04/2022
 */
public class Multiply extends AbstractOpArith {
    public Multiply(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "*";
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
        if (this.getType().isFloat() && !compiler.getCompilerOptions().getNocheck()) {
            compiler.addInstruction(new CMP(new ImmediateFloat(0), leftRegister));
            compiler.addInstruction(new BEQ(no_underflow));
            compiler.addInstruction(new CMP(new ImmediateFloat(0), rightRegister));
            compiler.addInstruction(new BEQ(no_underflow));
        }
        compiler.addInstruction(new MUL(leftRegister, rightRegister));

        // overflow error only when the result is a float
        if (this.getType().isFloat()) {
            if (!compiler.getCompilerOptions().getNocheck()) {
                Label end_multiply = compiler.labelManager.createLabel("end_multiply" + index);
                compiler.addInstruction(new BOV(compiler.labelManager.getLabel(ErrorCatcher.OV_ERROR)));
                compiler.addInstruction(new CMP(new ImmediateFloat(0), rightRegister));
                compiler.addInstruction(new BEQ(compiler.labelManager.getLabel(ErrorCatcher.UD_ERROR)));
                compiler.addInstruction(new BRA(end_multiply));
                compiler.addLabel(no_underflow);
                compiler.addInstruction(new LOAD(new ImmediateFloat(0), rightRegister));
                compiler.addLabel(end_multiply);
                compiler.labelManager.incrementUnderflowCounter();
            }
        }
    }
}
