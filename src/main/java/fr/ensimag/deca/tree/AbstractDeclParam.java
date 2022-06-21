package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;

/**
 * Abstract declaration of parameter for the code factorization.
 * 
 * @author gl10
 */
public abstract class AbstractDeclParam extends Tree {

    /**
     * Verify the type of a parameter.  
     * Pass 2
     * @param compiler
     */
    protected abstract Type verifyDeclParam(DecacCompiler compiler)
            throws ContextualError;

    /**
     * Verify the declaration of a parameter.  
     * Pass 3
     * @param compiler
     * @param localEnv
     */
    protected abstract void verifyDeclParamEnv(DecacCompiler compiler, EnvironmentExp localEnv) throws ContextualError;
    
    /**
     * Set the index of the parameter.  
     * @param index
     */
    protected abstract void setIndex(int index);

}