package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;

public class ListDeclField extends TreeList<AbstractDeclField> {

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclField f : getList()) {
            f.decompile(s);
        }
    }

    void verifyListDeclFielad(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        for (AbstractDeclField f : getList()) {
            f.verifyDeclField(compiler, localEnv, currentClass);
        }
    }

    public void codeGenListDeclField(DecacCompiler compiler) {
        for (AbstractDeclField f : getList()) {
            f.codeGenDeclField(compiler);
        }
    }
}
