package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.ErrorCatcher;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.RINT;

/**
 * ReadInt.
 *
 * @author gl10
 * 
 */
public class ReadInt extends AbstractReadExpr {

    /**
     * {@inheritDoc}
     */
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        this.setType(compiler.environmentType.INT);
        return this.getType();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void decompile(IndentPrintStream s) {
        s.print("readInt()");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    /**
     * {@inheritDoc} 
     * evalue l'expression et stocke son résultat dans le registre
     * Register.getR(register_name)
     */
    @Override
    public void codeGenExp(DecacCompiler compiler, int register_name) {
        compiler.addInstruction(new RINT());
        compiler.addInstruction(new BOV(compiler.labelManager.getLabel(ErrorCatcher.IO_ERROR)));
        compiler.addInstruction(new LOAD(Register.R1, Register.getR(register_name)));
    }
}
