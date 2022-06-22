package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * A class' list of parameter declarations 
 *  @author gl10
 */
public class ListDeclParam extends TreeList<AbstractDeclParam> {

    /**
     * {@inheritDoc}
     */
    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclParam p : getList()) {
            p.decompile(s);
            if (getList().lastIndexOf(p) != getList().size() - 1)
                s.print(", ");
        }
    }

     /**
     * Verify contextually each parameter declaration.
     * 
     * @param compiler
     * @return Signature create a signature of a function with the paramaters types
     */
    Signature verifyListDeclParam(DecacCompiler compiler) throws ContextualError {
        Signature sig = new Signature();
        Type t;
        for (AbstractDeclParam p : getList()) {
            t = p.verifyDeclParam(compiler);
            sig.add(t);
        }
        return sig;
    }

    /**
     * Verify contextually each parameter declaration with the local environment.
     * 
     * @param compiler
     * @param localEnv 
     */
    public void verifyListDeclParamEnv(DecacCompiler compiler, EnvironmentExp localEnv) throws ContextualError {
        int i = 0;
        for (AbstractDeclParam p : getList()) {
            p.setIndex(i);
            p.verifyDeclParamEnv(compiler, localEnv);
            i++;
        }
    }
}
