package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * Initialization
 * @author gl10
 * 
 */
public class Initialization extends AbstractInitialization {

    /**
     * Return the expression of the initialization.
     */
    public AbstractExpr getExpression() {
        return expression;
    }

    // Expression of the initialization.
    private AbstractExpr expression;

    /**
     * Set the expression of the initialization. 
     * @param expression
     */
    public void setExpression(AbstractExpr expression) {
        Validate.notNull(expression);
        this.expression = expression;
    }
    /**
     * @param expression Expression of the initialization
     */
    public Initialization(AbstractExpr expression) {
        Validate.notNull(expression);
        this.expression = expression;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void verifyInitialization(DecacCompiler compiler, Type t,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        setExpression(expression.verifyRValue(compiler, localEnv, currentClass, t));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void codeGenInitialization(DecacCompiler compiler, int register_name) {
        this.expression.codeGenExp(compiler, register_name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void codeGenInitialization(DecacCompiler compiler, int register_name, Type type) {
        this.codeGenInitialization(compiler, register_name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void decompile(IndentPrintStream s) {
        s.print(" = ");
        expression.decompile(s);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected
    void iterChildren(TreeFunction f) {
        expression.iter(f);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        expression.prettyPrint(s, prefix, true);
    }
}
