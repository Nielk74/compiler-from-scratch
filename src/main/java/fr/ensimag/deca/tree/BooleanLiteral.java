package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 * Boolean literal
 * @author gl10
 * 
 */
public class BooleanLiteral extends AbstractExpr {

    // Value of the literal
    private boolean value;

    /**
     * @param value Value of the literal
     */
    public BooleanLiteral(boolean value) {
        this.value = value;
    }

    /**
     * Return the value of the literal
     */
    public boolean getValue() {
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        this.setType(compiler.environmentType.BOOLEAN);
        return compiler.environmentType.BOOLEAN;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void decompile(IndentPrintStream s) {
        s.print(Boolean.toString(value));
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
    String prettyPrintNode() {
        return "BooleanLiteral (" + value + ")";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void codeGenExp(DecacCompiler compiler, int register_name) {
        if(this.getValue()) {
            compiler.addInstruction(new LOAD(new ImmediateInteger(1), Register.getR(register_name)));
        }else{
            compiler.addInstruction(new LOAD(new ImmediateInteger(0), Register.getR(register_name)));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void codeGenCondition(DecacCompiler compiler, boolean negative, Label l) {
        if(negative){
            if(this.getValue()){
                compiler.addInstruction(new BRA(l));
            }
        }else{
            if(!this.getValue()){
                compiler.addInstruction(new BRA(l));
            }
        }
    }
}
