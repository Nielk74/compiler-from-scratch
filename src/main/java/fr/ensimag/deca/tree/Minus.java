package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.ErrorCatcher;
import fr.ensimag.deca.codegen.RegisterManager;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.SUB;

/**
 * @author gl10
 * @date 25/04/2022
 */
public class Minus extends AbstractOpArith {
    public Minus(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "-";
    }
    
    @Override
    protected void codeGenExp(DecacCompiler compiler, int register_name) {
        super.codeGenExp(compiler, register_name);
        GPRegister leftRegister;
        if (compiler.registerManager.getNbRegisterMax() == register_name) {
            leftRegister = Register.R1;
        } else {
            leftRegister = Register.getR(register_name + 1);
        }
        GPRegister rightRegister = Register.getR(register_name);
        compiler.addInstruction(new SUB(leftRegister, rightRegister));

        // overflow error only when the result is a float
        if (this.getType().isFloat()) {
            compiler.addInstruction(new BOV(compiler.labelManager.getLabel(ErrorCatcher.OV_ERROR)));
        }
    }
}
