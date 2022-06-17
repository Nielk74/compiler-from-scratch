package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.ErrorCatcher;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;

public class Selection extends AbstractLValue {

    private AbstractExpr expr;
    private AbstractIdentifier field;

    public Selection(AbstractExpr expr, AbstractIdentifier field) {
        Validate.notNull(expr);
        Validate.notNull(field);
        this.expr = expr;
        this.field = field;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {

        // verify expression returns a class in environment
        Type type = expr.verifyExpr(compiler, localEnv, currentClass);
        if (!type.isClass()) {
            throw new ContextualError("Wrong expression: cannot select a field of a non-class type",
                    expr.getLocation());
        }

        expr.setType(type);

        ClassDefinition classDef = ((ClassType) (type)).getDefinition();

        // verify that the field exists in the class
        ExpDefinition fieldDef = classDef.getMembers().get(field.getName());
        if (fieldDef == null) {
            throw new ContextualError("Unknown field: " + field.getName(), field.getLocation());
        }
        if (!fieldDef.isField()) {
            throw new ContextualError("Wrong field name: " + field.getName(), field.getLocation());
        }
        fieldDef = (FieldDefinition) fieldDef;
        field.setDefinition(fieldDef);
        this.setType(fieldDef.getType());
        return fieldDef.getType();
    }

    @Override
    public void decompile(IndentPrintStream s) {
        this.expr.decompile(s);
        s.print(".");
        this.field.decompile(s);

    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        expr.prettyPrint(s, prefix, false);
        field.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        this.expr.iter(f);
        this.field.iter(f);
    }

    @Override
    protected void codeGenExp(DecacCompiler compiler, int register_name) {
        DAddr offset = this.codeGenLeftValue(compiler);
        compiler.addInstruction(new LOAD(offset, Register.getR(register_name)));
    }

    @Override
    protected DAddr codeGenLeftValue(DecacCompiler compiler) {
        expr.codeGenExp(compiler, 0);
        compiler.addInstruction(new CMP(new NullOperand(), Register.getR(0)));

        // check null pointer
        if (!compiler.getCompilerOptions().getNocheck()) {
            compiler.addInstruction(new BEQ(compiler.labelManager.getLabel(ErrorCatcher.NULL_ERROR)));
        }
        Type type = expr.getType();
        ClassDefinition classDef = ((ClassType) (type)).getDefinition();
        ExpDefinition fieldDef = classDef.getMembers().get(field.getName());
        int fieldIndex = ((FieldDefinition) fieldDef).getIndex();
        RegisterOffset offset = new RegisterOffset(fieldIndex, Register.getR(0));
        return offset;
    }
}
