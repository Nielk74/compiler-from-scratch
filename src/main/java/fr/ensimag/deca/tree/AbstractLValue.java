package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 * Left-hand side value of an assignment.
 * 
 * @author gl10
 * 
 */
public abstract class AbstractLValue extends AbstractExpr {
    /**
     * Returns the register containing the left value
     * Usable only by its sub classes
     * 
     * @param compiler
     * @return DAddr
     */
    protected abstract DAddr codeGenLeftValue(DecacCompiler compiler, int register_name);

    /**
     * {@inheritDoc} 
     * evalue l'expression et stocke son résultat dans le registre
     * Register.getR(register_name)
     */
    @Override
    public void codeGenExp(DecacCompiler compiler, int register_name) {
        DAddr offset = this.codeGenLeftValue(compiler, register_name);
        compiler.addInstruction(new LOAD(offset, Register.getR(register_name)));
    }
}
