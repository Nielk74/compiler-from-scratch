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

/**
 * The selection of the field of a class.
 * 
 * @author gl10
 */
public class Selection extends AbstractLValue {

    /**
     * The expression of the selection.
     */
    private AbstractExpr expr;
    /**
     * The field of the selection.
     */
    private AbstractIdentifier field;

    /**
     * @param expr The expression of the selection.
     * @param field The field of the selection.
     */
    public Selection(AbstractExpr expr, AbstractIdentifier field) {
        Validate.notNull(expr);
        Validate.notNull(field);
        this.expr = expr;
        this.field = field;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {

        // verify expression returns a class in environment
        Type typeExpr = expr.verifyExpr(compiler, localEnv, currentClass);
        if (!typeExpr.isClass()) {
            throw new ContextualError("Wrong expression: cannot select a field of a non-class type",
                    expr.getLocation());
        }

        expr.setType(typeExpr);

        ClassDefinition classDef = ((ClassType) (typeExpr)).getDefinition();

        // verify that the field exists in the class
        ExpDefinition def = classDef.getMembers().get(field.getName());
        if (def == null) {
            throw new ContextualError("Unknown field: " + field.getName(), field.getLocation());
        }
        FieldDefinition fieldDef = def.asFieldDefinition("Wrong field name: " + field.getName(), field.getLocation());
        field.setDefinition(fieldDef);
        this.setType(fieldDef.getType());

        if (fieldDef.getVisibility() == Visibility.PROTECTED) {
            if (currentClass == null) {
                throw new ContextualError("Wrong field access: Cannot access protected field: " + field.getName(), field.getLocation());
            }
            // regle de verification de visibilite 3.66
            if (!(((ClassType) typeExpr).isSubClassOf(currentClass.getType())
                    && currentClass.getType().isSubClassOf(fieldDef.getContainingClass().getType()))) {
                throw new ContextualError("Wrong field access: Cannot access protected field: " + field.getName(), field.getLocation());
            }
        }

        return fieldDef.getType();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void decompile(IndentPrintStream s) {
        this.expr.decompile(s);
        s.print(".");
        this.field.decompile(s);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        expr.prettyPrint(s, prefix, false);
        field.prettyPrint(s, prefix, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void iterChildren(TreeFunction f) {
        this.expr.iter(f);
        this.field.iter(f);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected DAddr codeGenLeftValue(DecacCompiler compiler, int register_name) {
        expr.codeGenExp(compiler, register_name);

        // check null pointer
        if (!compiler.getCompilerOptions().getNocheck()) {
            compiler.addInstruction(new CMP(new NullOperand(), Register.getR(register_name)));
            compiler.addInstruction(new BEQ(compiler.labelManager.getLabel(ErrorCatcher.NULL_ERROR)));
        }

        Type type = expr.getType();
        ClassDefinition classDef = ((ClassType) (type)).getDefinition();
        ExpDefinition fieldDef = classDef.getMembers().get(field.getName());
        int fieldIndex = ((FieldDefinition) fieldDef).getIndex();
        RegisterOffset offset = new RegisterOffset(fieldIndex, Register.getR(register_name));
        return offset;
    }
}
