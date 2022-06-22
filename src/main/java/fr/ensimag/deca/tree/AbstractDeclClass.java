package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;

/**
 * Class declaration.
 *
 * @author gl10
 * 
 */
public abstract class AbstractDeclClass extends Tree {

        /**
         * Pass 1 of [SyntaxeContextuelle]. Verify that the class declaration is OK
         * without looking at its content.
         * @param compiler 
         */
        protected abstract void verifyClass(DecacCompiler compiler)
                        throws ContextualError;

        /**
         * Pass 2 of [SyntaxeContextuelle]. Verify that the class members (fields and
         * methods) are OK, without looking at method body and field initialization.
         * @param compiler 
         */
        protected abstract void verifyClassMembers(DecacCompiler compiler)
                        throws ContextualError;

        /**
         * Pass 3 of [SyntaxeContextuelle]. Verify that instructions and expressions
         * contained in the class are OK.
         * @param compiler 
         */
        protected abstract void verifyClassBody(DecacCompiler compiler)
                        throws ContextualError;

        /**
         * Codegen methods table
         * @param compiler 
         */
        protected abstract void codeGenDeclClass(DecacCompiler compiler);

        /**
         * Codegen constructors of classses
         * @param compiler 
         */
        protected abstract void codeGenClassInit(DecacCompiler compiler);

        /**
         * Codegen implementation of method
         * @param compiler 
         */
        protected abstract void codeGenMethodImplementation(DecacCompiler compiler);
}
