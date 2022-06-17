package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 * Expression, i.e. anything that has a value.
 *
 * @author gl10
 * @date 25/04/2022
 */
public abstract class AbstractExpr extends AbstractInst {
    /**
     * @return true if the expression does not correspond to any concrete token
     *         in the source code (and should be decompiled to the empty string).
     */
    boolean isImplicit() {
        return false;
    }

    /**
     * Get the type decoration associated to this expression (i.e. the type computed
     * by contextual verification).
     */
    public Type getType() {
        return type;
    }

    protected void setType(Type type) {
        Validate.notNull(type);
        this.type = type;
    }

    private Type type;

    @Override
    protected void checkDecoration() {
        if (getType() == null) {
            throw new DecacInternalError("Expression " + decompile() + " has no Type decoration");
        }
    }

    /**
     * Verify the expression for contextual error.
     * 
     * implements non-terminals "expr" and "lvalue"
     * of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler     (contains the "env_types" attribute)
     * @param localEnv
     *                     Environment in which the expression should be checked
     *                     (corresponds to the "env_exp" attribute)
     * @param currentClass
     *                     Definition of the class containing the expression
     *                     (corresponds to the "class" attribute)
     *                     is null in the main bloc.
     * @return the Type of the expression
     *         (corresponds to the "type" attribute)
     */
    public abstract Type verifyExpr(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError;

    /**
     * Verify the expression in right hand-side of (implicit) assignments
     * 
     * implements non-terminal "rvalue" of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler     contains the "env_types" attribute
     * @param localEnv     corresponds to the "env_exp" attribute
     * @param currentClass corresponds to the "class" attribute
     * @param expectedType corresponds to the "type1" attribute
     * @return this with an additional ConvFloat if needed...
     */
    public AbstractExpr verifyRValue(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass,
            Type expectedType)
            throws ContextualError {
        Type currentType = this.verifyExpr(compiler, localEnv, currentClass);
        if (currentType.isInt() && expectedType.isFloat()) {
            ConvFloat convExpr = new ConvFloat(this);
            currentType = convExpr.verifyExpr(compiler, localEnv, currentClass);
            return convExpr;
        }
        if (currentType.isNull() && expectedType.isClass()) {
            return this;
        }
        if (currentType.isClass() && expectedType.isClass()) {
            if (!((ClassType) currentType).isSubClassOf((ClassType) expectedType)) {
                throw new ContextualError(
                        "Wrong right value class type - expected: subtype of " + expectedType + " ≠ current: " + currentType,
                        this.getLocation());
            } else {
                return this;
            }
        }
        if (!currentType.equals(expectedType)) {
            throw new ContextualError(
                    "Wrong right value type - expected: " + expectedType + " ≠ current: " + currentType,
                    this.getLocation());
        }
        return this;
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        Type expectedType = this.verifyExpr(compiler, localEnv, currentClass);
        this.setType(expectedType);
    }

    /**
     * Verify the expression as a condition, i.e. check that the type is
     * boolean.
     *
     * @param localEnv
     *                     Environment in which the condition should be checked.
     * @param currentClass
     *                     Definition of the class containing the expression, or
     *                     null in
     *                     the main program.
     */
    void verifyCondition(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type t = this.verifyExpr(compiler, localEnv, currentClass);

        if (!t.isBoolean())
            throw new ContextualError("Wrong condition type - expected: boolean ≠ current: " + t, this.getLocation());
    }

    /**
     * Generate code to print the expression
     *
     * @param compiler
     */
    protected void codeGenPrint(DecacCompiler compiler) {
        DVal d;
        d = this.codeGenExp(compiler);

        compiler.addInstruction(new LOAD(d, Register.R1));
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        this.codeGenExp(compiler, 2);
    }

    @Override
    protected void decompileInst(IndentPrintStream s) {
        decompile(s);
        s.print(";");
    }

    @Override
    protected void prettyPrintType(PrintStream s, String prefix) {
        Type t = getType();
        if (t != null) {
            s.print(prefix);
            s.print("type: ");
            s.print(t);
            s.println();
        }
    }

    // evalue l'expression et stocke son résultat dans le registre
    // Register.getR(register_name)
    protected void codeGenExp(DecacCompiler compiler, int register_name) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    // evalue l'expression et retourne une Dval contenant son résultat
    protected DVal codeGenExp(DecacCompiler compiler) {
        this.codeGenExp(compiler, 2);
        return Register.getR(2);
    }

    protected void codeGenCondition(DecacCompiler compiler, boolean negative, Label l) {
        if (!this.getType().isBoolean()) {
            throw new DecacInternalError("Type non boolean forbidden in AbstractExpr.codeGenCondition");
        }
        DVal d = this.codeGenExp(compiler);
     
        compiler.addInstruction(new LOAD(d, Register.R1));
        compiler.addInstruction(new CMP(0, Register.R1));
        if (negative) {
            compiler.addInstruction(new BNE(l));
        } else {
            compiler.addInstruction(new BEQ(l));
        }
    }
}
