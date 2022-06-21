package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.STORE;

/**
 * Assignment, i.e. lvalue = expr.
 *
 * @author gl10
 * 
 */
public class Assign extends AbstractBinaryExpr {

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractLValue getLeftOperand() {
        // The cast succeeds by construction, as the leftOperand has been set
        // as an AbstractLValue by the constructor.
        return (AbstractLValue) super.getLeftOperand();
    }

    /**
     * @param leftOperand Left operand of the expression. 
     * @param rightOperand Right operand of the expression.
     */
    public Assign(AbstractLValue leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {

        Type leftType = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        AbstractExpr expr = this.getRightOperand().verifyRValue(compiler, localEnv, currentClass, leftType);
        this.setRightOperand(expr);
        setType(leftType);
        return leftType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        this.getRightOperand().codeGenExp(compiler, 2);
        DAddr d = this.getLeftOperand().codeGenLeftValue(compiler, 3);
        compiler.addInstruction(new STORE(Register.getR(2), d));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getOperatorName() {
        return "=";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void codeGenExp(DecacCompiler compiler, int register_name) {
        this.codeGenInst(compiler);
    }
}
