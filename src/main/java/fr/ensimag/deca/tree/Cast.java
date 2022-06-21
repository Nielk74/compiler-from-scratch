package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.ErrorCatcher;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BGE;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.ima.pseudocode.instructions.INT;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 * Cast/convert an expression in a certain type
 * @author gl10
 * 
 */
public class Cast extends AbstractExpr {

    // conversion in type
    private AbstractIdentifier type;
    // expression to convert/cast
    private AbstractExpr expr;


    /**
    * @param type
    * @param expr
     */
    public Cast(AbstractIdentifier type, AbstractExpr expr) {
        Validate.notNull(type);
        Validate.notNull(expr);
        this.type = type;
        this.expr = expr;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        Type exprType = expr.verifyExpr(compiler, localEnv, currentClass);

        TypeDefinition typeDefType = compiler.environmentType.defOfType(type.getName());
        if (typeDefType == null) {
            throw new ContextualError("Wrong type : " + type.getName() + " is not defined", type.getLocation());
        }

        type.setDefinition(typeDefType);
        Type typeType = type.getDefinition().getType();

        if (exprType.isVoid()) {
            throw new ContextualError("Wrong type : void is forbidden", type.getLocation());
        }

        else if ((exprType.isInt() && typeType.isFloat())) {
            this.setType(compiler.environmentType.FLOAT);
        } else if (exprType.isFloat() && typeType.isInt()) {
            this.setType(compiler.environmentType.INT);
        } else if (exprType.isClass() && typeType.isClass()) {
            if (((ClassType) exprType).isSubClassOf((ClassType) typeType)
                    || ((ClassType) typeType).isSubClassOf((ClassType) exprType)) {
                this.setType(typeType);
            } else {
                throw new ContextualError(
                        "Wrong right value class type - expected: subtype of " + typeType + " ≠ current: " + exprType,
                        this.getLocation());
            }
        } else {
            throw new ContextualError(
                    "Cannot cast " + exprType + " in " + typeType,
                    this.getLocation());
        }
        return this.getType();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void codeGenExp(DecacCompiler compiler, int register_name) {
        // Create labels
        int labelNum = compiler.labelManager.createWhileLabel();
        Label whileLabel = compiler.labelManager.getLabel("while_" + Integer.toString(labelNum));
        Label endWhileLabel = compiler.labelManager.getLabel("end_while_" + Integer.toString(labelNum));
        
        compiler.addComment("Cast to type " + type.getName());

        // Load adress of expression in register register_name
        expr.codeGenExp(compiler, register_name);

        // conversion from float to int
        if (type.getDefinition().getType().isInt()) {
            // check if the value is too big for le cast to int

            compiler.addComment("Cast to int");
            Label outOfRangeLabel = compiler.labelManager.createLabel("outOfRange_" + Integer.toString(labelNum));
            Label endLabel = compiler.labelManager.createLabel("end_check_outOfRange_" + Integer.toString(labelNum));
            compiler.addInstruction(new CMP(new ImmediateFloat(2147483648.0f), Register.getR(register_name)));
            compiler.addInstruction(new BGE(outOfRangeLabel));

            compiler.addInstruction(new INT(Register.getR(register_name), Register.getR(register_name)));
            compiler.addInstruction(new BRA(endLabel));

            compiler.addLabel(outOfRangeLabel);
            compiler.addInstruction(new LOAD(new ImmediateInteger(2147483647), Register.getR(register_name)));
            compiler.addLabel(endLabel);
            compiler.addComment("End cast to int");

            return;
        // conversion from int to float
        } else if (type.getDefinition().getType().isFloat()){
            compiler.addComment("Cast to float");

            compiler.addInstruction(new FLOAT(Register.getR(register_name), Register.getR(register_name)));
            compiler.addComment("End cast to float");

            return;   
        }


        compiler.addComment("check if object is null ");

        // Check if the class address of expression is null
        compiler.addInstruction(new CMP(new NullOperand(), Register.getR(register_name)));
        compiler.addInstruction(new BEQ(endWhileLabel));

        if (!compiler.getCompilerOptions().getNocheck()) {
            compiler.addComment("Verification of cast to " + type.getName());

            // Load expression dynamic type's class address in R0
            compiler.addInstruction(new LOAD(Register.getR(register_name), Register.R0));
            compiler.addInstruction(new LOAD(new RegisterOffset(0, Register.R0), Register.R0));

            // Load address of type's class in R1
            compiler.addInstruction(new LEA(type.getClassDefinition().getOperand(), Register.R1));
            compiler.addLabel(whileLabel);

            // Check if the class address of expression is null
            compiler.addInstruction(new CMP(new NullOperand(), Register.R0));
            compiler.addInstruction(new BEQ(compiler.labelManager.getLabel(ErrorCatcher.CAST_ERROR)));

            // Compare class address of expression and class address of type
            compiler.addInstruction(new CMP(Register.R0, Register.R1));
            compiler.addInstruction(new BEQ(endWhileLabel));

            // Load superclass address of expression's class in R0
            compiler.addInstruction(new LOAD(new RegisterOffset(0, Register.R0), Register.R0));

            compiler.addInstruction(new BRA(whileLabel));
        }
        compiler.addLabel(endWhileLabel);
        compiler.addComment("End cast to type " + type.getName());

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void decompile(IndentPrintStream s) {
        s.print("(");
        this.type.decompile(s);
        s.print(") (");
        this.expr.decompile(s);
        s.print(")");

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        expr.prettyPrint(s, prefix, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void iterChildren(TreeFunction f) {
        this.type.iter(f);
        this.expr.iter(f);

    }
}
