package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;

/**
 * Main block of a Deca program.
 *
 * @author gl10
 * 
 */
public abstract class AbstractMain extends Tree {

    /**
     * Assembly code generation, for the main, 
     * and start the codegen for what is in the main, if there is something.
     * 
     * @param compiler
     */
    protected abstract void codeGenMain(DecacCompiler compiler);


    /**
     * Implements non-terminal "main" of [SyntaxeContextuelle] in pass 3 
     * 
     * @param compiler
     */
    protected abstract void verifyMain(DecacCompiler compiler) throws ContextualError;
}
