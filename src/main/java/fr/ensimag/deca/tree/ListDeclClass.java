package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;

import org.apache.log4j.Logger;

/**
 *
 * @author gl10
 * @date 25/04/2022
 */
public class ListDeclClass extends TreeList<AbstractDeclClass> {
    private static final Logger LOG = Logger.getLogger(ListDeclClass.class);
    
    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclClass c : getList()) {
            c.decompile(s);
            s.println();
        }
    }

    /**
     * Pass 1 of [SyntaxeContextuelle]
     */
    void verifyListClass(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify listClass: start");
        for (AbstractDeclClass c : getList()) {
            c.verifyClass(compiler);
        }
        LOG.debug("verify listClass: end");
    }

    /**
     * Pass 2 of [SyntaxeContextuelle]
     */
    public void verifyListClassMembers(DecacCompiler compiler) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }
    
    /**
     * Pass 3 of [SyntaxeContextuelle]
     */
    public void verifyListClassBody(DecacCompiler compiler) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    public void codeGenListDeclClass(DecacCompiler compiler) {
        codeGenClassObject(compiler);
        for (AbstractDeclClass c : getList()) {
            c.codeGenDeclClass(compiler);
            compiler.stackManager.incrementVarCounter();
        }
    }

    private void codeGenClassObject(DecacCompiler compiler) {
        compiler.addInstruction(new LOAD(new NullOperand(), Register.R0));
        DAddr offset = new RegisterOffset(compiler.stackManager.getGbOffsetCounter(), Register.GB);
        compiler.stackManager.incrementGbOffsetCounter();
        compiler.addInstruction(new STORE(Register.R0, offset));

        // table of method for object
        compiler.environmentType.OBJECT.getDefinition().setOperand(new RegisterOffset(1, Register.GB));
        compiler.stackManager.incrementVarCounter();
    }
}
