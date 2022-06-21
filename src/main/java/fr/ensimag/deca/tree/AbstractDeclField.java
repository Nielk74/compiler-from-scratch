package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;

/**
 * Abstract declaration of field for the code factorization.
 * 
 * @author gl10
 */
public abstract class AbstractDeclField extends Tree {

    /* Pass 2 */
    /**
     * Verify the declaration of a field. 
     * Pass 2
     * @param compiler
     * @param currentClass
     */
    protected abstract void verifyDeclField(DecacCompiler compiler, ClassDefinition currentClass)
            throws ContextualError;

    /* Pass 3 */
    /**
     * Verify the initialization of a field. 
     * Pass 3
     * @param compiler
     * @param currentClass
     */
    protected abstract void verifyDeclFieldInit(DecacCompiler compiler, ClassDefinition currentClass)
            throws ContextualError;

    /**
     * Generate code for the declaration of a field. 
     * @param compiler
     */
    protected abstract void codeGenDeclField(DecacCompiler compiler);
}
