package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BGT;
import fr.ensimag.ima.pseudocode.instructions.BLT;
import fr.ensimag.ima.pseudocode.instructions.BLE;
import fr.ensimag.ima.pseudocode.instructions.BGE;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 *
 * @author gl10
 * @date 25/04/2022
 */
public class While extends AbstractInst {
    private AbstractExpr condition;
    private ListInst body;

    public AbstractExpr getCondition() {
        return condition;
    }

    public ListInst getBody() {
        return body;
    }

    public While(AbstractExpr condition, ListInst body) {
        Validate.notNull(condition);
        Validate.notNull(body);
        this.condition = condition;
        this.body = body;
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        int labelNum = compiler.labelManager.createWhileLabel();
        Label whileLabel = compiler.labelManager.getLabel("while_" + Integer.toString(labelNum));
        Label endWhileLabel = compiler.labelManager.getLabel("end_while_" + Integer.toString(labelNum));

        compiler.addLabel(whileLabel);
        if (this.condition instanceof AbstractBinaryExpr) {
            AbstractIdentifier leftOperand = (AbstractIdentifier) ((AbstractBinaryExpr) this.condition)
                    .getLeftOperand();
            AbstractIdentifier rightOperand = (AbstractIdentifier) ((AbstractBinaryExpr) this.condition)
                    .getRightOperand();

            String operatorName = ((AbstractBinaryExpr) this.condition).getOperatorName();
            // CMP val, Rx
            compiler.addInstruction(new LOAD((DVal) leftOperand.getExpDefinition().getOperand(), Register.getR(2)));
            compiler.addInstruction(new CMP((DVal) rightOperand.getExpDefinition().getOperand(), Register.getR(2)));
            // BNE | BGE | ... -> jump
            switch (operatorName) {
                case "==":
                    compiler.addInstruction(new BNE(endWhileLabel));
                    break;
                case "!=":
                    compiler.addInstruction(new BEQ(endWhileLabel));
                    break;
                case ">=":
                    compiler.addInstruction(new BLT(endWhileLabel));
                    break;
                case "<=":
                    compiler.addInstruction(new BGT(endWhileLabel));
                    break;
                case ">":
                    compiler.addInstruction(new BLE(endWhileLabel));
                    break;
                case "<":
                    compiler.addInstruction(new BGE(endWhileLabel));
                    break;
                default:
                    break;
            }
        } else if (this.condition instanceof Not) {
            if (((Not) this.condition).getOperand() instanceof AbstractIdentifier) {
                // comparaison unaire avec une variable
                AbstractIdentifier ident = (AbstractIdentifier) ((Not) this.condition).getOperand();
                compiler.addInstruction(new LOAD(ident.getExpDefinition().getOperand(), Register.getR(2)));
                compiler.addInstruction(new CMP(0, Register.getR(2)));
                compiler.addInstruction(new BEQ(endWhileLabel));
            }
        }
        // TODO : comparaison unaire d'autre type (cf page 51)
        this.body.codeGenListInst(compiler);
        // BRA
        compiler.addInstruction(new BRA(whileLabel));
        // add "end_while" label
        compiler.addLabel(endWhileLabel);
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        this.condition.verifyCondition(compiler, localEnv, currentClass);
        this.body.verifyListInst(compiler, localEnv, currentClass, returnType);
    }

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

    @Override
    protected void iterChildren(TreeFunction f) {
        condition.iter(f);
        body.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        condition.prettyPrint(s, prefix, false);
        body.prettyPrint(s, prefix, true);
    }

}
