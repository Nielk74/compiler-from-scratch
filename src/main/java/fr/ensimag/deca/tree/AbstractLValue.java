package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DAddr;

/**
 * Left-hand side value of an assignment.
 * 
 * @author gl10
 * @date 25/04/2022
 */
public abstract class AbstractLValue extends AbstractExpr {
    /*
     * Returns the register containing the left value
     */
    protected abstract DAddr codeGenLeftValue(DecacCompiler compiler);
}
