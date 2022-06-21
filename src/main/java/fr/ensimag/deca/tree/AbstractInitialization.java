package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * Initialization (of variable, field, ...)
 *
 * @author gl10
 * 
 */
public abstract class AbstractInitialization extends Tree {
    
    /**
     * Implements non-terminal "initialization" of [SyntaxeContextuelle] in pass 3
     * @param compiler contains "env_types" attribute
     * @param t corresponds to the "type" attribute
     * @param localEnv corresponds to the "env_exp" attribute
     * @param currentClass 
     *          corresponds to the "class" attribute (null in the main bloc).
     */
    protected abstract void verifyInitialization(DecacCompiler compiler,
            Type t, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError;

    /**
     * Generate assembly code for the initialization
     * @param compiler
     * @param register_name 
     */
    protected abstract void codeGenInitialization(DecacCompiler compiler, int register_name);

    /**
     * Generate assembly code for the initialization with default values
     * @param compiler
     * @param register_name
     * @param type Type of the variable to initialize with default value in fonction of the type
     */
    protected abstract void codeGenInitialization(DecacCompiler compiler, int register_name, Type type);
}
