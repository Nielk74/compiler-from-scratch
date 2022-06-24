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
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.NEW;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.STORE;

/**
 * The new operator.
 * 
 * @author gl10
 */
public class New extends AbstractExpr {

    /**
     * The type of the new variable.
     */
    private AbstractIdentifier type;

    /**
     * @param type The type of the new variable.
     */
    public New(AbstractIdentifier type) {
        Validate.notNull(type);
        this.type = type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        TypeDefinition t = compiler.environmentType.defOfType(type.getName());
        if (t == null) {
            throw new ContextualError("Wrong initialization object: Type " + type.getName() + " is not defined", type.getLocation());
        }
        if(!t.isClass()){
            throw new ContextualError("Wrong initialization object: Type " + type.getName() + " is not a class", type.getLocation());
        }
        type.setDefinition(t);
        this.setType(t.getType());
        return t.getType();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void decompile(IndentPrintStream s) {
        s.print("new ");
        this.type.decompile(s);
        s.print("()");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void iterChildren(TreeFunction f) {
        this.type.iter(f);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void codeGenExp(DecacCompiler compiler, int register_name) {
        // allocate memory for the object
        compiler.addInstruction(new NEW(type.getClassDefinition().getNumberOfFields() + 1, Register.getR(register_name)));

        // initialize the object fields
        if (!compiler.getCompilerOptions().getNocheck()){
            compiler.addInstruction(new BOV(compiler.labelManager.getLabel(ErrorCatcher.HO_ERROR)));
        }
        
        compiler.addInstruction(new LEA(type.getClassDefinition().getOperand(), Register.R0));
        compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(0, Register.getR(register_name))));
        compiler.addInstruction(new PUSH(Register.getR(register_name)));
        compiler.addInstruction(new BSR(new Label("init." + type.getName())));
        compiler.addInstruction(new POP(Register.getR(register_name)));
    }
}
