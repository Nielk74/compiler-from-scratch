package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;

public abstract class AbstractDeclMethod extends Tree {

    protected abstract void verifyDeclMethod(DecacCompiler compiler, ClassDefinition currentClass)
        throws ContextualError;
    
    protected abstract void codeGenImplementMethod(DecacCompiler compiler);

    protected abstract void verifyDeclMethodBody(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError;
}
