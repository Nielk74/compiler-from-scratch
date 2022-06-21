package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.ErrorCatcher;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

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
    protected void codeGenExp(DecacCompiler compiler, int register_name) {
        compiler.addComment("InstanceOf");

        // If expr has a type Null, instanceof is always true
        if (expr.getType().isNull()) {
            compiler.addInstruction(new LOAD(1,Register.getR(register_name)));
            return;
        }

        // Load adress of expression in register register_name
        expr.codeGenExp(compiler, register_name);

        // Create labels
        int labelNum = compiler.labelManager.createWhileLabel();
        Label whileLabel = compiler.labelManager.getLabel("while_" + Integer.toString(labelNum));
        Label endWhileLabel = compiler.labelManager.getLabel("end_while_" + Integer.toString(labelNum));
        Label falseLabel = compiler.labelManager.createLabel("instance_of_false" + Integer.toString(labelNum));

        // Load expression dynamic type's class address in R0
        compiler.addInstruction(new LOAD(Register.getR(register_name), Register.R0));

        // Initialization of register register_name with 1 (in case verification is true)
        compiler.addInstruction(new LOAD(1,Register.getR(register_name)));
        
        // If expr has a type Null, instanceof is always true
        compiler.addInstruction(new CMP(new NullOperand(), Register.R0));
        compiler.addInstruction(new BEQ(endWhileLabel));

        compiler.addInstruction(new LOAD(new RegisterOffset(0, Register.R0), Register.R0));

        // Load address of type's class in R1
        compiler.addInstruction(new LEA(type.getClassDefinition().getOperand(), Register.R1));
        compiler.addLabel(whileLabel);

        // Check if the class address of expression is null
        compiler.addInstruction(new CMP(new NullOperand(), Register.R0));
        compiler.addInstruction(new BEQ(falseLabel));

        // Compare class address of expression and class address of type
        compiler.addInstruction(new CMP(Register.R0, Register.R1));
        compiler.addInstruction(new BEQ(endWhileLabel));

        // Load superclass address of expression's class in R0
        compiler.addInstruction(new LOAD(new RegisterOffset(0, Register.R0), Register.R0));

        compiler.addInstruction(new BRA(whileLabel));
        compiler.addLabel(falseLabel);
        // Put 0 in register register_name (in case verification is false)
        compiler.addInstruction(new LOAD(0,Register.getR(register_name)));
        compiler.addLabel(endWhileLabel);
        compiler.addComment("End instance");
    }
}
