package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * A class' list of field declarations 
 *  @author gl10
 */

public class ListDeclField extends TreeList<AbstractDeclField> {

    /**
     * {@inheritDoc}
     */
    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclField f : getList()) {
            f.decompile(s);
        }
    }

       /* Pass 2 */
    /**
    *
    * Verify contextually each field declaration.
    *
    *@param compiler 
    */
    void verifyListDeclField(DecacCompiler compiler,
            ClassDefinition currentClass) throws ContextualError {
        for (AbstractDeclField f : getList()) {
            f.verifyDeclField(compiler, currentClass);
        }
    }


    /* Pass 3 */
    /**
    *
    * Verify contextually each field initialization.
    *
    *@param compiler 
    */
    void verifyListDeclFieldInit(DecacCompiler compiler, ClassDefinition currentClass)
            throws ContextualError {
        for (AbstractDeclField f : getList()) {
            f.verifyDeclFieldInit(compiler, currentClass);
        }
    }

    /**
    *
    * Generate assembly code of each field.
    *
    *@param compiler 
    */
    void codeGenListDeclField(DecacCompiler compiler) {
        for (AbstractDeclField f : getList()) {
            f.codeGenDeclField(compiler);
        }
    }
}
