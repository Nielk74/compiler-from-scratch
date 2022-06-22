package fr.ensimag.deca.tree;

import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * Full if/else if/else statement.
 *
 * @author gl10
 * 
 */
public class IfThenElse extends AbstractInst {

    // Condition of the if
    private final AbstractExpr condition;
    // Instructions in the then
    private final ListInst thenBranch;
    // Instructions in the else
    private ListInst elseBranch;

    /**
     * @param condition Condition of the if
     * @param thenBranch Instructions in the then
     * @param elseBranch Instructions in the else
     */
    public IfThenElse(AbstractExpr condition, ListInst thenBranch, ListInst elseBranch) {
        Validate.notNull(condition);
        Validate.notNull(thenBranch);
        Validate.notNull(elseBranch);
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        this.condition.verifyCondition(compiler, localEnv, currentClass);
        this.thenBranch.verifyListInst(compiler, localEnv, currentClass, returnType);
        this.elseBranch.verifyListInst(compiler, localEnv, currentClass, returnType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        int labelNum = compiler.labelManager.createIfThenElseLabel();
        Label ifLabel = compiler.labelManager.getLabel("if_" + Integer.toString(labelNum));
        Label elseLabel = compiler.labelManager.getLabel("else_" + Integer.toString(labelNum));
        Label endIfLabel = compiler.labelManager.getLabel("end_if_" + Integer.toString(labelNum));
        compiler.addLabel(ifLabel);
        this.condition.codeGenCondition(compiler,false, elseLabel);
        this.thenBranch.codeGenListInst(compiler);
        compiler.addInstruction(new BRA(endIfLabel));
        compiler.addLabel(elseLabel);
        this.elseBranch.codeGenListInst(compiler);
        compiler.addLabel(endIfLabel);
    }

    /**
     * Set else branch
     * @param newElse Instructions in the else
     */
    public void setElse(ListInst newElse) {
        Validate.notNull(elseBranch);
        this.elseBranch = newElse;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void decompile(IndentPrintStream s) {
        s.print("if(");
        condition.decompile(s);
        s.println(") {");
        s.indent();
        thenBranch.decompile(s);
        s.unindent();
        s.println("}");
        s.println("else {");
        s.indent();
        elseBranch.decompile(s);
        s.unindent();
        s.print("}");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void iterChildren(TreeFunction f) {
        condition.iter(f);
        thenBranch.iter(f);
        elseBranch.iter(f);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        condition.prettyPrint(s, prefix, false);
        thenBranch.prettyPrint(s, prefix, false);
        elseBranch.prettyPrint(s, prefix, true);
    }
}
