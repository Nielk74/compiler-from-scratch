package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;

/**
 * Represent the method body
 * 
 * @author gl10
 * 
 */
public abstract class AbstractMethodBody extends Tree {

    /**
     * Verify the variable declarations and the instructions, of a methode.
     * 
     * @param compiler
     * @param localEnv
     * @param currentClass Definition of this class.
     * @param returnType Return type of the method.
     */
    protected abstract void verifyMethodBody(DecacCompiler compiler,
        EnvironmentExp localEnv, ClassDefinition currentClass, Type returnType)
        throws ContextualError;

    /**
     * Generate the assembly code, for the variable declarations and the instructions, of a methode.
     * 
     * @param compiler
     * @param returnType Return type of the method.
     */
    protected abstract void codeGenMethodBody(DecacCompiler compiler, Type returnType);
}
