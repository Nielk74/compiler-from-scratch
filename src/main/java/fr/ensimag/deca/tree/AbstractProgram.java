package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;

/**
 * Entry point for contextual verifications and code generation from outside the package.
 * 
 * @author gl10
 * 
 *
 */
public abstract class AbstractProgram extends Tree {
    /**
     * 
     * 
     * @param compiler
     */

    /**
     * Start the contextual verification.
     *
     * @param compiler
     */
    public abstract void verifyProgram(DecacCompiler compiler) throws ContextualError;
    /**
     * Start the code generation.
     *
     * @param compiler
     */
    public abstract void codeGenProgram(DecacCompiler compiler) ;

}
