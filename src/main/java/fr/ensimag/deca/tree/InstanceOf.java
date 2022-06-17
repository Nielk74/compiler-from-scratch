package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.LabelManager;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.SEQ;

public class InstanceOf extends AbstractExpr {

    private AbstractIdentifier type;
    private AbstractExpr expr;

    public InstanceOf(AbstractIdentifier type, AbstractExpr expr) {
        Validate.notNull(type);
        Validate.notNull(expr);
        this.type = type;
        this.expr = expr;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        // Verify the variable
        Type classType = expr.verifyExpr(compiler, localEnv, currentClass);
        if (!(classType.isClassOrNull())) {
            throw new ContextualError("Wrong type : " + expr.getType() + " is not a Class type", type.getLocation());
        }

        // Retrieve definition of the type
        TypeDefinition typeType = compiler.environmentType.defOfType(type.getName());
        if (typeType == null) {
            throw new ContextualError("Wrong type : " + type.getName() + " is not defined", type.getLocation());
        }

        if (!(typeType.isClass())) {
            throw new ContextualError("Wrong type : " + type.getName() + " is not a Class type", type.getLocation());
        }
        type.setDefinition(typeType);

        this.setType(compiler.environmentType.BOOLEAN);
        return compiler.environmentType.BOOLEAN;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("(");
        this.expr.decompile(s);
        s.print(" instanceof ");
        this.type.decompile(s);
        s.print(")");

    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, true);
        expr.prettyPrint(s, prefix, false);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        this.type.iter(f);
        this.expr.iter(f);

    }

    @Override
    protected void codeGenCondition(DecacCompiler compiler, boolean negative, Label l) {
        DAddr addrRight = type.getClassDefinition().getOperand();
        compiler.addInstruction(new LEA(addrRight, Register.getR(0)));
        int labelNum = compiler.labelManager.createInstanceOfLabel();
        Label trueBranch = compiler.labelManager.getLabel("instanceof_trueBranch_" + Integer.toString(labelNum));

        if (expr.getType().isNull()) {
            compiler.addInstruction(new LOAD(new NullOperand(), Register.getR(1)));
            compiler.addInstruction(new CMP(Register.getR(1), Register.getR(0)));
            if (negative) {
                compiler.addInstruction(new BEQ(l));
            } else {
                compiler.addInstruction(new BEQ(trueBranch));
                compiler.addInstruction(new BRA(l));
            }
        } else if (expr.getType().isClass()) {
            ClassDefinition defClassLeft = ((ClassType) expr.getType()).getDefinition();
            while (defClassLeft != null) {
                compiler.addInstruction(new LEA(defClassLeft.getOperand(), Register.getR(1)));
                compiler.addInstruction(new CMP(Register.getR(1), Register.getR(0)));
                if (negative) {
                    compiler.addInstruction(new BEQ(l));
                } else {
                    compiler.addInstruction(new BEQ(trueBranch));
                }
                defClassLeft = defClassLeft.getSuperClass();

            }
            if (!negative) {
                compiler.addInstruction(new BRA(l));
            }
        }
        compiler.addLabel(trueBranch);
    }

    @Override
    protected void codeGenExp(DecacCompiler compiler, int register_name) {
        int labelNum = compiler.labelManager.createInstanceOfLabel();
        Label trueLabel = compiler.labelManager.getLabel("instanceof_trueBranch_" + Integer.toString(labelNum));
        Label endLabel = compiler.labelManager.getLabel("instanceof_end_" + Integer.toString(labelNum));

        this.codeGenCondition(compiler, false, trueLabel);
        compiler.addInstruction(new LOAD(1, Register.getR(register_name)));
        compiler.addInstruction(new BRA(endLabel));

        // if the expression is false, we load 0 in the register
        compiler.addLabel(trueLabel);
        compiler.addInstruction(new LOAD(0, Register.getR(register_name)));
        compiler.addLabel(endLabel);
    }

}
