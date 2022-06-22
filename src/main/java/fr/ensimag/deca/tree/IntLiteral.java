package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

import java.io.PrintStream;

/**
 * Integer literal
 *
 * @author gl10
 * 
 */
public class IntLiteral extends AbstractExpr {

    /**
     * Return the value of the int. 
     */
    public int getValue() {
        return value;
    }

    // Value of the int. 
    private int value;

    /**
     * @param value Value of the int. 
     */
    public IntLiteral(int value) {
        this.value = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        this.setType(compiler.environmentType.INT);
        return compiler.environmentType.INT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    String prettyPrintNode() {
        return "Int (" + getValue() + ")";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void decompile(IndentPrintStream s) {
        s.print(Integer.toString(value));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void codeGenExp(DecacCompiler compiler, int register_name) {
        compiler.addInstruction(new LOAD(new ImmediateInteger(this.getValue()), Register.getR(register_name)));
    }
}
