package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * Binary expressions.
 *
 * @author gl10
 * 
 */
public abstract class AbstractBinaryExpr extends AbstractExpr {

    /**
     * Return the left operand. 
     * @return AbstractExpr
     */
    public AbstractExpr getLeftOperand() {
        return leftOperand;
    }

    /**
     * Return the right operand. 
     * @return AbstractExpr
     */
    public AbstractExpr getRightOperand() {
        return rightOperand;
    }

    /**
     * Set the left operand. 
     * @param leftOperand Left operand of the expression
     */
    protected void setLeftOperand(AbstractExpr leftOperand) {
        Validate.notNull(leftOperand);
        this.leftOperand = leftOperand;
    }

    /**
     * Set the right operand. 
     * @param rightOperand Right operand of the expression
     */
    protected void setRightOperand(AbstractExpr rightOperand) {
        Validate.notNull(rightOperand);
        this.rightOperand = rightOperand;
    }

    // Left operand of the operation
    private AbstractExpr leftOperand;
    // Right operand of the operation
    private AbstractExpr rightOperand;

    /**
     * @param leftOperand Left operand of the expression
     * @param rightOperand Right operand of the expression
     */
    public AbstractBinaryExpr(AbstractExpr leftOperand,
            AbstractExpr rightOperand) {
        Validate.notNull(leftOperand, "left operand cannot be null");
        Validate.notNull(rightOperand, "right operand cannot be null");
        Validate.isTrue(leftOperand != rightOperand, "Sharing subtrees is forbidden");
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void decompile(IndentPrintStream s) {
        s.print("(");
        getLeftOperand().decompile(s);
        s.print(" " + getOperatorName() + " ");
        getRightOperand().decompile(s);
        s.print(")");
    }

    /**
     * Return the name of the operator. 
     * @return String
     */
    abstract protected String getOperatorName();

    /**
     * {@inheritDoc}
     */
    @Override
    protected void iterChildren(TreeFunction f) {
        leftOperand.iter(f);
        rightOperand.iter(f);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        leftOperand.prettyPrint(s, prefix, false);
        rightOperand.prettyPrint(s, prefix, true);
    }

    // Code generation for boolean binary expressions (AbstractOpBool, AbstractOpCmp) 
    /**
     * {@inheritDoc}
     */
    @Override
    protected void codeGenExp(DecacCompiler compiler, int register_name){
        int labelNum = compiler.labelManager.createIfThenElseLabel();
        Label ifLabel = compiler.labelManager.getLabel("if_" + Integer.toString(labelNum));
        Label elseLabel = compiler.labelManager.getLabel("else_" + Integer.toString(labelNum));
        Label endIfLabel = compiler.labelManager.getLabel("end_if_" + Integer.toString(labelNum));

        // if the expression is true, load 1 in the register
        compiler.addLabel(ifLabel);
        this.codeGenCondition(compiler, false, elseLabel);
        compiler.addInstruction(new LOAD(1, Register.getR(register_name)));
        compiler.addInstruction(new BRA(endIfLabel));

        // else, load 0 in the register
        compiler.addLabel(elseLabel);
        compiler.addInstruction(new LOAD(0, Register.getR(register_name)));
        compiler.addLabel(endIfLabel);
    }
}
