package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;

public abstract class AbstractDeclField extends Tree {

    /* Pass 2 */
    protected abstract void verifyDeclField(DecacCompiler compiler, ClassDefinition currentClass)
            throws ContextualError;

    /* Pass 3 */
    protected abstract void verifyDeclFieldInit(DecacCompiler compiler, ClassDefinition currentClass)
            throws ContextualError;

    protected abstract void codeGenDeclField(DecacCompiler compiler);
}
