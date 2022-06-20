package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

public class MethodBody extends AbstractMethodBody {

    private ListDeclVar declVariables;
    private ListInst insts;

    public MethodBody(ListDeclVar declVariables, ListInst insts) {
        Validate.notNull(declVariables);
        Validate.notNull(insts);
        this.declVariables = declVariables;
        this.insts = insts;
    }

    @Override
    protected void verifyMethodBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        declVariables.verifyListDeclVariable(compiler, localEnv, currentClass); 
        insts.verifyListInst(compiler, localEnv, currentClass, returnType);       
    }

    /* codegen of declaration variable and instructions */
    @Override
    protected void codeGenMethodBody(DecacCompiler compiler) {
        declVariables.codeGenListDeclVariable(compiler);
        insts.codeGenListInst(compiler);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.println("{");
        s.indent();
        this.declVariables.decompile(s);
        this.insts.decompile(s);
        s.unindent();
        s.println("}");

    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        declVariables.prettyPrint(s, prefix, false);
        insts.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        this.insts.iter(f);
        this.declVariables.iter(f);        
    }

}
