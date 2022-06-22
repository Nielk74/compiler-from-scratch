package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * Abstract class to factorize the code of the And, and the Or.
 * Boolean operator (and, or).
 *
 * @author gl10
 * 
 */
public abstract class AbstractOpBool extends AbstractBinaryExpr {

    /**
     * @param leftOperand
     * @param rightOperand
     */
    public AbstractOpBool(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type type1 = super.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type type2 = super.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        if (!(type1.isBoolean() && type2.isBoolean())) {
            throw new ContextualError("Wrong type - expected: boolean boolean ≠ current: " + type1 + " " + type2, this.getLocation());
        } 
        this.setType(compiler.environmentType.BOOLEAN);
        return this.getType();
    }

}
