package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

/**
 * Empty main Deca program
 *
 * @author gl10
 * 
 */
public class EmptyMain extends AbstractMain {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void verifyMain(DecacCompiler compiler) throws ContextualError {
        // Do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void codeGenMain(DecacCompiler compiler) {
        // Do nothing
    }

    /**
     * Contains no real information => nothing to check.
     */
    /**
     * {@inheritDoc}
     */
    @Override
    protected void checkLocation() {
        // nothing
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void decompile(IndentPrintStream s) {
        // no main program => nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }
}
