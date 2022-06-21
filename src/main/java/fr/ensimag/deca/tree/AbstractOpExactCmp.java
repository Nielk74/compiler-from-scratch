package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;

/**
 * Abstract class for the equals and not equals operators.
 * exact comparator (==, !=).
 *
 * @author gl10
 * 
 */
public abstract class AbstractOpExactCmp extends AbstractOpCmp {

    /**
     * @param leftOperand
     * @param rightOperand
     */
    public AbstractOpExactCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
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

        if (leftType.isBoolean() && rightType.isBoolean() || leftType.isClassOrNull() && rightType.isClassOrNull()) {
            this.setType(compiler.environmentType.BOOLEAN);
            return this.getType();
        } else {
            return super.verifyExpr(compiler, localEnv, currentClass);
        }
    }
}
