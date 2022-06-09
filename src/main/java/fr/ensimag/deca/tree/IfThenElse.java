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
 * @date 25/04/2022
 */
public class IfThenElse extends AbstractInst {

    private final AbstractExpr condition;
    private final ListInst thenBranch;
    private ListInst elseBranch;

    public IfThenElse(AbstractExpr condition, ListInst thenBranch, ListInst elseBranch) {
        Validate.notNull(condition);
        Validate.notNull(thenBranch);
        Validate.notNull(elseBranch);
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        this.condition.verifyCondition(compiler, localEnv, currentClass);
        this.thenBranch.verifyListInst(compiler, localEnv, currentClass, returnType);
        this.elseBranch.verifyListInst(compiler, localEnv, currentClass, returnType);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        int labelNum = compiler.labelManager.createIfThenElseLabel();
        Label ifLabel = compiler.labelManager.getLabel("if_" + Integer.toString(labelNum));
        Label elseLabel = compiler.labelManager.getLabel("else_" + Integer.toString(labelNum));
        Label endIfLabel = compiler.labelManager.getLabel("end_if_" + Integer.toString(labelNum));
        String elseIfLabelFormat = "else_if_" + Integer.toString(labelNum)+ "_";
        int cptElseif = 0;
        Label elseifLabel = compiler.labelManager.createLabel(elseIfLabelFormat + cptElseif);
        ListInst elseIfBranch = this.elseBranch;
        boolean isElseif = false;
        compiler.addLabel(ifLabel);
        //TODO: ajouter else if dans labelmanager
        if (!elseBranch.isEmpty()) {
            // if elseif endif
            if (elseBranch.getList().get(0).getClass() == IfThenElse.class) {
                this.condition.codeGenCondition(compiler, false, elseifLabel);
                isElseif = true;
            } else {
                // if else endif
                this.condition.codeGenCondition(compiler, false, elseLabel);
            }
            // generate "then" instructions
            thenBranch.codeGenListInst(compiler);
            // else if label and instructions
            compiler.addInstruction(new BRA(endIfLabel));
        } else {
            // if endif
            this.condition.codeGenCondition(compiler, false, endIfLabel);
            // generate "then" instructions
            thenBranch.codeGenListInst(compiler);
        }
        // elseif et distinguer else de elseif
        while (isElseif) {
            IfThenElse elseif = (IfThenElse) elseIfBranch.getList().get(0);
            compiler.addLabel(elseifLabel);
            // else de elseif = endif
            if (elseif.elseBranch.isEmpty()) {
                elseif.codeGenElseIf(compiler, endIfLabel, endIfLabel, false);
                isElseif = false;
            // else de elseif = elseif
            } else if (elseif.elseBranch.getList().get(0).getClass() == IfThenElse.class) {
                elseifLabel = compiler.labelManager.createLabel(elseIfLabelFormat + Integer.toString(cptElseif + 1));
                elseif.codeGenElseIf(compiler, endIfLabel, elseifLabel, true);
            // else de elseif = else
            } else {
                elseif.codeGenElseIf(compiler, endIfLabel, elseLabel, true);
                isElseif = false;
            }
            elseIfBranch = elseif.elseBranch;
            cptElseif++;
        }
        // else
        if (!elseIfBranch.isEmpty()) {
            compiler.addLabel(elseLabel);
            elseIfBranch.codeGenListInst(compiler);
        }
        // add "end_if" label
        compiler.addLabel(endIfLabel);
    }

    protected void codeGenElseIf(DecacCompiler compiler, Label if_fin, Label jmp, boolean fin) {
        this.condition.codeGenCondition(compiler, false, jmp);
        this.thenBranch.codeGenListInst(compiler);
        if (fin)
            compiler.addInstruction(new BRA(if_fin));
    }

    public void setElse(ListInst newElse) {
        Validate.notNull(elseBranch);
        this.elseBranch = newElse;
    }

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

    @Override
    protected void iterChildren(TreeFunction f) {
        condition.iter(f);
        thenBranch.iter(f);
        elseBranch.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        condition.prettyPrint(s, prefix, false);
        thenBranch.prettyPrint(s, prefix, false);
        elseBranch.prettyPrint(s, prefix, true);
    }
}
