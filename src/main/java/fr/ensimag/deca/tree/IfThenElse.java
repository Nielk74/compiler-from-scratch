package fr.ensimag.deca.tree;

import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.BGE;
import fr.ensimag.ima.pseudocode.instructions.BLT;
import fr.ensimag.ima.pseudocode.instructions.BLE;
import fr.ensimag.ima.pseudocode.instructions.BGT;

import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.BranchInstruction;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.UnaryInstruction;

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

    private static int labelCounter = 0;

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
        // CMP #17, R1
        // BLE label (dans le codegen de <, ==, !=, .. peut etre)
        Label elseLabel = new Label("else_" + Integer.toString(labelCounter));
        Label endIfLabel = new Label("end_if_" + Integer.toString(labelCounter));
        labelCounter++;

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
                    compiler.addInstruction(new BNE(elseBranch.isEmpty() ? endIfLabel : elseLabel));
                    break;
                case "!=":
                    compiler.addInstruction(new BEQ(elseBranch.isEmpty() ? endIfLabel : elseLabel));
                    break;
                case ">=":
                    compiler.addInstruction(new BLT(elseBranch.isEmpty() ? endIfLabel : elseLabel));
                    break;
                case "<=":
                    compiler.addInstruction(new BGT(elseBranch.isEmpty() ? endIfLabel : elseLabel));
                    break;
                case ">":
                    compiler.addInstruction(new BLE(elseBranch.isEmpty() ? endIfLabel : elseLabel));
                    break;
                case "<":
                    compiler.addInstruction(new BGE(elseBranch.isEmpty() ? endIfLabel : elseLabel));
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
                compiler.addInstruction(new BEQ(elseBranch.isEmpty() ? endIfLabel : elseLabel));
            }
            // TODO : comparaison unaire d'autre type (cf page 51)
        }

        // generate "then" instructions
        thenBranch.codeGenListInst(compiler);
        // jump to end_if
        compiler.addInstruction(new BRA(endIfLabel));

        // add "else" label and instructions
        if (!elseBranch.isEmpty()) {
            compiler.addLabel(elseLabel);
            elseBranch.codeGenListInst(compiler);
        }

        // add "end_if" label
        compiler.addLabel(endIfLabel);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("not yet implemented");
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
