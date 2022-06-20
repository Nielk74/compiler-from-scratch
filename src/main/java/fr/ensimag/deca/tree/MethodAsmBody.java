package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.InlinePortion;

public class MethodAsmBody extends AbstractMethodBody {

    private String code;

    public MethodAsmBody(String code) {
        Validate.notNull(code);
        this.code = code;
    }

    @Override
    protected void verifyMethodBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass,
            Type returnType) throws ContextualError {
        // do nothing, asm code is not verified
    }

    @Override
    protected void codeGenMethodBody(DecacCompiler compiler, Type returnType) {
        compiler.add(new InlinePortion(code));
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("asm(" + "\"" + this.code + "\"" + ");");

    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // do nothing, child node
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // do nothing, child node
    }

}
