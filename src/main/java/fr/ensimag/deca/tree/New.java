package fr.ensimag.deca.tree;

import java.io.PrintStream;

import javax.swing.plaf.synth.Region;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
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

public class New extends AbstractExpr {

    private AbstractIdentifier type;

    public New(AbstractIdentifier type) {
        Validate.notNull(type);
        this.type = type;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        TypeDefinition t = compiler.environmentType.defOfType(type.getName());
        if (t == null) {
            throw new ContextualError("Type " + type.getName() + " not found", type.getLocation());
        }
        type.setDefinition(t);
        this.setType(t.getType());
        return t.getType();
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("new ");
        this.type.decompile(s);
        s.print("()");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        this.type.iter(f);
    }

    @Override
    protected void codeGenExp(DecacCompiler compiler, int register_name) {
        // TODO changer le calcul de la taille à allouer selon le nombre d'attribut
        compiler.addInstruction(new NEW(1, Register.getR(register_name)));
        compiler.addInstruction(new BOV(compiler.labelManager.getLabel("ho_error")));
        compiler.addInstruction(new LEA(type.getClassDefinition().getOperand(), Register.R0));
        compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(0, Register.getR(register_name))));
        compiler.addInstruction(new PUSH(Register.getR(register_name)));
        //compiler.addInstruction(new BSR(new Label("init.A")));
        compiler.addInstruction(new POP(Register.getR(register_name)));
    }
}
