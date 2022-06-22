package fr.ensimag.deca.tree;

import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * Unary expression.
 *
 * @author gl10
 * 
 */
public abstract class AbstractUnaryExpr extends AbstractExpr {

    /**
     * Return the operand variable.
     * 
     * @return AbstractExpr
     */
    public AbstractExpr getOperand() {
        return operand;
    }

    /**
     * Operand of the unary expression.
     */
    private AbstractExpr operand;

    /**
     * @param operand Operand of the expression.
     */
    public AbstractUnaryExpr(AbstractExpr operand) {
        Validate.notNull(operand);
        this.operand = operand;
    }

    /**
     * Return the OperatorName, its name in the Deca code.
     * 
     * @return String
     */
    protected abstract String getOperatorName();
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void decompile(IndentPrintStream s) {
        s.print(getOperatorName());
        operand.decompile(s);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void iterChildren(TreeFunction f) {
        operand.iter(f);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        operand.prettyPrint(s, prefix, true);
    }

}
