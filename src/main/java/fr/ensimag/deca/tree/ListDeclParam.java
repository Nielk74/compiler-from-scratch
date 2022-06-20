package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

public class ListDeclParam extends TreeList<AbstractDeclParam> {

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclParam p : getList()) {
            p.decompile(s);
            if (getList().lastIndexOf(p) != getList().size() - 1)
                s.print(", ");
        }
    }

    Signature verifyListDeclParam(DecacCompiler compiler) throws ContextualError {
        Signature sig = new Signature();
        Type t;
        for (AbstractDeclParam p : getList()) {
            t = p.verifyDeclParam(compiler);
            sig.add(t);
        }
        return sig;
    }

    public void verifyListDeclParamEnv(DecacCompiler compiler, EnvironmentExp localEnv) throws ContextualError {
        int i = 0;
        for (AbstractDeclParam p : getList()) {
            p.setIndex(i);
            p.verifyDeclParamEnv(compiler, localEnv);
            i++;
        }
    }
    
    public void codeGenListDeclParam(DecacCompiler compiler) {
        for (AbstractDeclParam p : getList()) {
            p.codeGenDeclParam(compiler);
        }
    }

}
