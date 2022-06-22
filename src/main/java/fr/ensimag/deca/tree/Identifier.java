package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 * Deca Identifier
 *
 * @author gl10
 * 
 */
public class Identifier extends AbstractIdentifier {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void checkDecoration() {
        if (getDefinition() == null) {
            throw new DecacInternalError("Identifier " + this.getName() + " has no attached Definition");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Definition getDefinition() {
        return definition;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ClassDefinition getClassDefinition() {
        try {
            return (ClassDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a class identifier, you can't call getClassDefinition on it");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MethodDefinition getMethodDefinition() {
        try {
            return (MethodDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a method identifier, you can't call getMethodDefinition on it");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FieldDefinition getFieldDefinition() {
        try {
            return (FieldDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a field identifier, you can't call getFieldDefinition on it");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VariableDefinition getVariableDefinition() {
        try {
            return (VariableDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a variable identifier, you can't call getVariableDefinition on it");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExpDefinition getExpDefinition() {
        try {
            return (ExpDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a Exp identifier, you can't call getExpDefinition on it");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDefinition(Definition definition) {
        this.definition = definition;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Symbol getName() {
        return name;
    }

    // Name of the identifier. 
    private Symbol name;

    /**
     * @param name Name of the identifier. 
     */
    public Identifier(Symbol name) {
        Validate.notNull(name);
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        ExpDefinition def = localEnv.get(this.getName());
        if (def == null) {
            throw new ContextualError("Wrong variable name: " + this.getName() + " is not defined", this.getLocation());
        }
        this.setDefinition(def);

        return this.verifyType(compiler);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type verifyType(DecacCompiler compiler) throws ContextualError {
        Type currentType = this.definition.getType();
        if (currentType == null) {
            throw new ContextualError("Unknown type: Type " + this.getName() + " is not defined", getLocation());
        }
        this.setType(currentType);
        return currentType;
    }

    // Definition of the identifier. 
    private Definition definition;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void decompile(IndentPrintStream s) {
        s.print(name.toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    String prettyPrintNode() {
        return "Identifier (" + getName() + ")";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void prettyPrintType(PrintStream s, String prefix) {
        Definition d = getDefinition();
        if (d != null) {
            s.print(prefix);
            s.print("definition: ");
            s.print(d);
            s.println();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void codeGenDeclClass(DecacCompiler compiler) {
        compiler.addComment("Load superclass of "+getName());
        compiler.addInstruction(new LEA(this.getClassDefinition().getSuperClass().getOperand(), Register.R0));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected DAddr codeGenLeftValue(DecacCompiler compiler, int register_name) {
        DAddr offset;
        if (this.definition.isField()) {
            compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.getR(0)));
            int index = ((FieldDefinition) this.definition).getIndex();
            offset = new RegisterOffset(index, Register.getR(0));
        } else {
            offset = this.getExpDefinition().getOperand();
            if (offset == null) {
                offset = new RegisterOffset(compiler.stackManager.getLbOffsetCounter(), Register.LB);
                compiler.stackManager.incrementLbOffsetCounter();
            }
            this.getExpDefinition().setOperand(offset);
        }
        return offset;
    }
}
