package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;


/**
 * Class for the this, for the classes and the main.
 * 
 * @author gl10
 */
public class This extends AbstractExpr {

    // by default, all identifiers are not implicit 
    public Boolean implicit = false;

    /**
     * Set the implicit variable with the input variable.
     * 
     * @param implicit
     */
    public void setImplicit(Boolean implicit) {
        this.implicit = implicit;
    }

    /**
     * Return the implicit variable, true if this is implicit, otherwise false.
     * 
     * @return boolean
     */
    boolean isImplicit() {
        return implicit;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        if (currentClass == null){
            throw new ContextualError("this is null", this.getLocation());
        }
        setType(currentClass.getType());
        return currentClass.getType();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void decompile(IndentPrintStream s) {
        if (!this.implicit){
            s.print("this");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void codeGenExp(DecacCompiler compiler, int register_name) {
        compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.getR(register_name)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // do nothing, child node
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }
}
