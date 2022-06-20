package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.EnvironmentType;

public abstract class AbstractDeclMethod extends Tree {

    protected abstract void verifyDeclMethod(DecacCompiler compiler, ClassDefinition currentClass)
        throws ContextualError;
    
    protected abstract void codeGenImplementMethod(DecacCompiler compiler, ClassDefinition currentClass);

    protected abstract void verifyDeclMethodBody(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError;
}
