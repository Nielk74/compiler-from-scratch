package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

public abstract class AbstractDeclField extends Tree {

    /* Pass 2 */
    protected abstract void verifyDeclField(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError;

    /* Pass 3 */
    protected abstract void verifyDeclFieldInit(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError;

    protected abstract void codeGenDeclField(DecacCompiler compiler);
}
