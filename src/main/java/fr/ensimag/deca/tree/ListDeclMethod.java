package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.LabelOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;


/**
 * A class' list of method declarations 
 *  @author gl10
 */

public class ListDeclMethod extends TreeList<AbstractDeclMethod> {

    /**
     * {@inheritDoc}
     */
    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclMethod m : getList()) {
            m.decompile(s);
        }
    }

    /**
    *
    * Verify contextually each method declaration.
    *
    *@param compiler 
    */
    public void verifyListDeclMethod(DecacCompiler compiler,
            ClassDefinition currentClass) throws ContextualError {
        for (AbstractDeclMethod m : getList()) {
            m.verifyDeclMethod(compiler, currentClass);
        }
    }
    
    /**
    *
    * Verify contextually each method body.
    *
    *@param compiler 
    */
    public void verifyListDeclMethodBody(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError {
        for (AbstractDeclMethod m : getList()) {
            m.verifyDeclMethodBody(compiler, currentClass);
        }
    }

    /**
    *
    * Generate assembly code for each method.
    *
    *@param compiler 
    */
    public void codeGenImplementMethod(DecacCompiler compiler) {
        for (AbstractDeclMethod m : getList()) {
            m.codeGenImplementMethod(compiler);
        }
    }
    
    /**
    *
    * Generate assembly code which store methods in the table of methodes.
    *
    *@param compiler 
    *@param currentClass class of the methods generated 
    */
    public void codeGenListDeclMethod(DecacCompiler compiler, ClassDefinition currentClass) {
        for (int i = 0; i <= currentClass.getNumberOfMethods(); i++) {
            MethodDefinition method = currentClass.getMembers().getMethod(i);

            compiler.addInstruction(new LOAD(new LabelOperand(method.getLabel()), Register.R0));
            
            DAddr offset = new RegisterOffset(compiler.stackManager.getGbOffsetCounter(), Register.GB);
            compiler.stackManager.incrementGbOffsetCounter();
            compiler.addInstruction(new STORE(Register.R0, offset));
            compiler.stackManager.incrementVarCounter();
        }
    }
}
