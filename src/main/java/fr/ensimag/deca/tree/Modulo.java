package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.ErrorCatcher;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.REM;

/**
 * Operator Modulo between two AbstractExpr: %
 * @author gl10
 * 
 */
public class Modulo extends AbstractOpArith {

    /**
    * @param leftOperand
    * @param rightOperand
     */
    public Modulo(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        super.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        super.getRightOperand().verifyExpr(compiler, localEnv, currentClass);

        Type leftType = super.getLeftOperand().getType();
        Type rightType = super.getRightOperand().getType();

        // modulo works only when operands are int type
        if (!leftType.isInt()) {
            throw new ContextualError("Wrong type: expected int ≠ current: " + leftType, this.getLocation());
        }

        if (!rightType.isInt()) {
            throw new ContextualError("Wrong type: expected int ≠ current: " + rightType, this.getLocation());
        }

        this.setType(compiler.environmentType.INT);
        return this.getType();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected String getOperatorName() {
        return "%";
    }


    /**
     * {@inheritDoc}
     */
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
        compiler.addInstruction(new REM(leftRegister, rightRegister));

        // the overflow error should only be triggered by x%0
        if (!compiler.getCompilerOptions().getNocheck()) {
            compiler.addInstruction(new BOV(compiler.labelManager.getLabel(ErrorCatcher.OV_ERROR)));
        }
    }
}
