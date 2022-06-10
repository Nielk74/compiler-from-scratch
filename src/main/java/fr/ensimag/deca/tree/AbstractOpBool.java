package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 *
 * @author gl10
 * @date 25/04/2022
 */
public abstract class AbstractOpBool extends AbstractBinaryExpr {

    public AbstractOpBool(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type type1 = super.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type type2 = super.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        if (!(type1.isBoolean() && type2.isBoolean())) {
            throw new ContextualError("Invalid type for one operand (must be boolean)", this.getLocation());
        } 
        this.setType(compiler.environmentType.BOOLEAN);
        return this.getType();
    }

}
