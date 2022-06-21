package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * The no operation operation, a semicolon with nothing.
 * 
 * @author gl10
 * 
 */
public class NoOperation extends AbstractInst {

    
    /** 
     * {@inheritDoc}
     * 
     * @param compiler
     * @param localEnv
     * @param currentClass
     * @param returnType
     * @throws ContextualError
     */
    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        // leaf node => nothing to do
    }

    
    /**
     * {@inheritDoc}
     * 
     * @param compiler
     */
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        // leaf node => nothing to do
    }

    
    /**
     * {@inheritDoc} 
     * 
     * @param s
     */
    @Override
    public void decompile(IndentPrintStream s) {
        s.print(";");
    }

    
    /**
     * {@inheritDoc}
     *
     * @param f
     */
    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    
    /**
     * {@inheritDoc}
     * 
     * @param s
     * @param prefix
     */
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

}
