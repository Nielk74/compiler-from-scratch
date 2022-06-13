package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.ErrorCatcher;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.deca.codegen.RegisterManager;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.REM;

/**
 *
 * @author gl10
 * @date 25/04/2022
 */
public class Modulo extends AbstractOpArith {

    public Modulo(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        super.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        super.getRightOperand().verifyExpr(compiler, localEnv, currentClass);

        Type leftType = super.getLeftOperand().getType();
        Type rightType = super.getRightOperand().getType();

        // modulo works only when operands are int type
        if (!leftType.isInt()) {
            throw new ContextualError("Left type is not int in Modulo.verifyExpr", this.getLocation());
        }

        if (!rightType.isInt()) {
            throw new ContextualError("Right type is not int in Modulo.verifyExpr", this.getLocation());
        }

        this.setType(compiler.environmentType.INT);
        return this.getType();
    }


    @Override
    protected String getOperatorName() {
        return "%";
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
        compiler.addInstruction(new REM(leftRegister, rightRegister));

        // the overflow error should only be triggered by x%0
        compiler.addInstruction(new BOV(compiler.labelManager.getLabel(ErrorCatcher.OV_ERROR)));
    }
}
