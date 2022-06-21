package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;

/**
 * Abstract declaration of method for the code factorization.
 * 
 * @author gl10
 */
public abstract class AbstractDeclMethod extends Tree {

    /**
     * Verify the declaration of a method. 
     * Pass 2
     * @param compiler
     * @param currentClass
     */
    protected abstract void verifyDeclMethod(DecacCompiler compiler, ClassDefinition currentClass)
        throws ContextualError;
    
    /**
     * Generate code for the declaration of a method. 
     * @param compiler
     */
    protected abstract void codeGenImplementMethod(DecacCompiler compiler);

    /**
     * Verify the body of a method. 
     * Pass 3
     * @param compiler
     * @param currentClass
     */
    protected abstract void verifyDeclMethodBody(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError;
}
