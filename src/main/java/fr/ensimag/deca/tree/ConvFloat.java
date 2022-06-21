package fr.ensimag.deca.tree;

import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * Conversion of an int into a float. Used for implicit conversions.
 * 
 * @author gl10
 * 
 */
public class ConvFloat extends AbstractUnaryExpr {

    /**
     * @param operand Operand of unary expression
     */
    public ConvFloat(AbstractExpr operand) {
        super(operand);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) {
        this.setType(compiler.environmentType.FLOAT);
        return this.getType();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getOperatorName() {
        return "/* conv float */";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void codeGenExp(DecacCompiler compiler, int register_name) {
        this.getOperand().codeGenExp(compiler, register_name);
        compiler.addInstruction(new FLOAT(Register.getR(register_name), Register.getR(register_name)));
    }
}
