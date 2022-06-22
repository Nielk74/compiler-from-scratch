package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

import java.io.PrintStream;

/**
 * Absence of initialization (e.g. "int x;" as opposed to "int x =
 * 42;").
 *
 * @author gl10
 * 
 */
public class NoInitialization extends AbstractInitialization {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void verifyInitialization(DecacCompiler compiler, Type t,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        // do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void codeGenInitialization(DecacCompiler compiler, int register_name) {
        // do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void codeGenInitialization(DecacCompiler compiler, int register_name, Type type) {
        if (type.isBoolean() || type.isInt() || type.isFloat()){
            compiler.addInstruction(new LOAD(0, Register.getR(register_name)));
            if (type.isFloat()) {
                compiler.addInstruction(new FLOAT(Register.getR(register_name), Register.getR(register_name)));
            }
        } else {
            // case class
            compiler.addInstruction(new LOAD(new NullOperand(), Register.getR(register_name)));
        };
    }

    /**
     * Node contains no real information, nothing to check.
     */
    @Override
    protected void checkLocation() {
        // nothing
    }

    /**
     * Node contains no real information, nothing to print.
     */
    @Override
    public void decompile(IndentPrintStream s) {
        // nothing
    }

    /**
     * leaf node, nothing to do
     */
    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    /**
     * leaf node, nothing to do
     */
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

}
