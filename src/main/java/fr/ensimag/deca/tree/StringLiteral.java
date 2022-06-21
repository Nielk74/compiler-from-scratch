package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.ImmediateString;
import fr.ensimag.ima.pseudocode.instructions.WSTR;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * String literal
 *
 * @author gl10
 * 
 */
public class StringLiteral extends AbstractStringLiteral {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValue() {
        return value;
    }

    private String value;

    /**
     * @param value The value of the string literal.
     */
    public StringLiteral(String value) {
        Validate.notNull(value);
        this.value = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        this.setType(compiler.environmentType.STRING);
        return compiler.environmentType.STRING;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        compiler.addInstruction(new WSTR(new ImmediateString(value)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void decompile(IndentPrintStream s) {
        s.print('"' + getValue() + '"');
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
        return "StringLiteral (" + value + ")";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void codeGenExp(DecacCompiler compiler, int register_name){
        //Do nothing
    }
}
