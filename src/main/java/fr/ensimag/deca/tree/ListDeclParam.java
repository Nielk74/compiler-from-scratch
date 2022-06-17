package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;

public class ListDeclParam extends TreeList<AbstractDeclParam> {

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclParam p : getList()) {
            p.decompile(s);
        }
    }

    void verifyListDeclParam(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        for (AbstractDeclParam p : getList()) {
            p.verifyDeclParam(compiler, localEnv, currentClass);
        }
    }

    public void codeGenListDeclParam(DecacCompiler compiler) {
        for (AbstractDeclParam p : getList()) {
            p.codeGenDeclParam(compiler);
        }
    }
}
