package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BRA;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * While control structure.
 *
 * @author gl10
 * 
 */
public class While extends AbstractInst {
    /**
     * Condition de la boucle while.
     */
    private AbstractExpr condition;
    /**
     * The body to execute.
     */
    private ListInst body;

    /**
     * Return the condition of the while.
     * 
     * @return AbstractExpr
     */
    public AbstractExpr getCondition() {
        return condition;
    }

    /**
     * Return the body of the while.
     * 
     * @return ListInst
     */
    public ListInst getBody() {
        return body;
    }

    /**
     * @param condition Condition of the while.
     * @param body Body of the while.
     */
    public While(AbstractExpr condition, ListInst body) {
        Validate.notNull(condition);
        Validate.notNull(body);
        this.condition = condition;
        this.body = body;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        int labelNum = compiler.labelManager.createWhileLabel();
        Label whileLabel = compiler.labelManager.getLabel("while_" + Integer.toString(labelNum));
        Label endWhileLabel = compiler.labelManager.getLabel("end_while_" + Integer.toString(labelNum));

        compiler.addLabel(whileLabel);
        compiler.addComment(this.condition.decompile());
        this.condition.codeGenCondition(compiler, false, endWhileLabel);        
        this.body.codeGenListInst(compiler);
        // BRA
        compiler.addInstruction(new BRA(whileLabel));
        // add "end_while" label
        compiler.addLabel(endWhileLabel);
        compiler.addComment("!"+this.condition.decompile());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        this.condition.verifyCondition(compiler, localEnv, currentClass);
        this.body.verifyListInst(compiler, localEnv, currentClass, returnType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void decompile(IndentPrintStream s) {
        s.print("while (");
        getCondition().decompile(s);
        s.println(") {");
        s.indent();
        getBody().decompile(s);
        s.unindent();
        s.print("}");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void iterChildren(TreeFunction f) {
        condition.iter(f);
        body.iter(f);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        condition.prettyPrint(s, prefix, false);
        body.prettyPrint(s, prefix, true);
    }

}
