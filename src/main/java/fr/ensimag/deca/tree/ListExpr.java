package fr.ensimag.deca.tree;

import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * List of expressions (eg list of parameters).
 *
 * @author gl10
 * 
 */
public class ListExpr extends TreeList<AbstractExpr> {

    /**
     * {@inheritDoc}
     */
    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractExpr e : getList()) {
            e.decompile(s);
        }
    }
}
