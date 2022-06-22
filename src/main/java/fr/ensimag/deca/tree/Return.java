package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 * The return of a method.
 */
public class Return extends AbstractInst {

    /**
     * The returned expression.
     */
    private AbstractExpr expr;

    /**
     * @param expr The returned expression.
     */
    public Return(AbstractExpr expr) {
        Validate.notNull(expr);
        this.expr = expr;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass,
            Type returnType) throws ContextualError {
        if (returnType.isVoid()) {
            throw new ContextualError("Wrong instruction: return statement not allowed in void method", this.getLocation());
        }
        this.expr.verifyExpr(compiler, localEnv, currentClass);
        if (!this.expr.getType().equals(returnType)) {
            throw new ContextualError("Wrong return type: it does not match method return type", this.getLocation());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        DVal result = expr.codeGenExp(compiler);
        compiler.addInstruction(new LOAD(result, Register.R0));
        Label endLabel = compiler.labelManager.getLabel("end_method_"+ Integer.toString(compiler.labelManager.getEndMethodCounter() - 1));
        compiler.addInstruction(new BRA(endLabel));    
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void decompile(IndentPrintStream s) {
        s.print("return ");
        this.expr.decompile(s);
        s.print(";");        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        expr.prettyPrint(s, prefix, true);                
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void iterChildren(TreeFunction f) {
        this.expr.iter(f);        
    }
    
}
