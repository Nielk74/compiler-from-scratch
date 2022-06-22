package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.OPP;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.ErrorCatcher;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * Unary minus, the unary expression.
 * @author gl10
 * 
 */
public class UnaryMinus extends AbstractUnaryExpr {

    /**
     * @param operand Operand of the unary minus.
     */
    public UnaryMinus(AbstractExpr operand) {
        super(operand);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type typeOperand = getOperand().verifyExpr(compiler, localEnv, currentClass);
        if (!typeOperand.isFloat() && !typeOperand.isInt()){
            throw new ContextualError("Wrong left type - expected: float or int ≠ current: "+ typeOperand, getLocation());
        }
        setType(typeOperand);
        return typeOperand;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getOperatorName() {
        return "-";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void codeGenExp(DecacCompiler compiler, int register_name) {
        this.getOperand().codeGenExp(compiler, register_name);
        GPRegister register = Register.getR(register_name);
        compiler.addInstruction(new OPP(register, register));
        
        // overflow error only when the result is a float
        if (this.getType().isFloat() && !compiler.getCompilerOptions().getNocheck()) {
            compiler.addInstruction(new BOV(compiler.labelManager.getLabel(ErrorCatcher.OV_ERROR)));
        }
    }

}
