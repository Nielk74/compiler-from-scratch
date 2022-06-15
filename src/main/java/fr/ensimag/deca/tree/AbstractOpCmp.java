package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 *
 * @author gl10
 * @date 25/04/2022
 */
public abstract class AbstractOpCmp extends AbstractBinaryExpr {

    public AbstractOpCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        super.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        super.getRightOperand().verifyExpr(compiler, localEnv, currentClass);

        Type leftType = super.getLeftOperand().getType();
        Type rightType = super.getRightOperand().getType();
        if (leftType.isInt() && rightType.isFloat()) {
            AbstractExpr expr = getLeftOperand().verifyRValue(compiler, localEnv, currentClass, rightType);
            this.setLeftOperand(expr);
        } else if (leftType.isFloat() && rightType.isInt()) {
            AbstractExpr expr = getRightOperand().verifyRValue(compiler, localEnv, currentClass, leftType);
            this.setRightOperand(expr);
        } else if (!leftType.sameType(rightType)) {
            throw new ContextualError("Wrong right value type - expected: " + leftType + " ≠ current: " + rightType,
                    this.getLocation());
        } else if (leftType.isBoolean() || rightType.isBoolean()) {
            throw new ContextualError("Wrong type: boolean is forbidden", this.getLocation());
        } else if (leftType.isClassOrNull() || rightType.isClassOrNull()) {
            throw new ContextualError("Wrong type: class is forbidden", this.getLocation());
        } else if (leftType.isString() || rightType.isString()) {
            throw new ContextualError("Wrong type: string is forbidden", this.getLocation());
        }

        this.setType(compiler.environmentType.BOOLEAN);
        return this.getType();
    }

    protected void codeGenCondition(DecacCompiler compiler, boolean negative, Label l) {
        getLeftOperand().codeGenExp(compiler, 2);
        getRightOperand().codeGenExp(compiler, 3);
        compiler.addInstruction(new CMP(Register.getR(3), Register.getR(2)));
    }
}
