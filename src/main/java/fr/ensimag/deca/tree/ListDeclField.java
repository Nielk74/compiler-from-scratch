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

    /* Pass 2 */
    void verifyListDeclField(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        for (AbstractDeclField f : getList()) {
            f.verifyDeclField(compiler, localEnv, currentClass);
        }
    }

    /* Pass 3 */
    void verifyListDeclFieldInit(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        for (AbstractDeclField f : getList()) {
            f.verifyDeclFieldInit(compiler, currentClass);
        }
    }

    void codeGenListDeclField(DecacCompiler compiler) {
        for (AbstractDeclField f : getList()) {
            f.codeGenDeclField(compiler);
        }
    }
}
