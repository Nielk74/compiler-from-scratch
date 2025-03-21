package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * Arithmetic binary operations (+, -, /, ...)
 * 
 * @author gl10
 * 
 */
public abstract class AbstractOpArith extends AbstractBinaryExpr {

    /**
     * @param leftOperand
     * @param rightOperand
     */
    public AbstractOpArith(AbstractExpr leftOperand, AbstractExpr rightOperand) {
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
        this.setType(compiler.environmentType.INT);

        if (leftType.isInt() && rightType.isFloat()) {
            // convert to float the left operand
            AbstractExpr expr = getLeftOperand().verifyRValue(compiler, localEnv, currentClass, rightType);
            this.setLeftOperand(expr);

            this.setType(compiler.environmentType.FLOAT);
        }
        else if (leftType.isFloat() && rightType.isInt()) {
            // convert to float the right operand
            AbstractExpr expr = getRightOperand().verifyRValue(compiler, localEnv, currentClass, leftType);
            this.setRightOperand(expr);

            this.setType(compiler.environmentType.FLOAT);
        } 
        else if(leftType.isFloat() && rightType.isFloat()) {
            this.setType(compiler.environmentType.FLOAT);
        }
        else if (!leftType.sameType(rightType)) {
            throw new ContextualError("Wrong type: expected int or float ≠ current: " + leftType +" "+ rightType, this.getLocation());
        }

        return this.getType();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void codeGenExp(DecacCompiler compiler, int register_name) {
        if (compiler.getCompilerOptions().getRegisterMax() == register_name) {
            // add space in the stack for the expression
            compiler.stackManager.incrementSavedRegisterCounter();
            getLeftOperand().codeGenExp(compiler, register_name);
            compiler.addInstruction(new PUSH(Register.getR(register_name)));
            getRightOperand().codeGenExp(compiler, register_name);
            compiler.addInstruction(new LOAD(Register.getR(register_name), Register.R1));
            compiler.addInstruction(new POP(Register.getR(register_name)));
        } else {
            getLeftOperand().codeGenExp(compiler, register_name);
            getRightOperand().codeGenExp(compiler, register_name + 1);
        }
    }
}
