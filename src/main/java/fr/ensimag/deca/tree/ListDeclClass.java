package fr.ensimag.deca.tree;

import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.LabelOperand;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import fr.ensimag.ima.pseudocode.instructions.SEQ;
import fr.ensimag.ima.pseudocode.instructions.STORE;

/**
 * List of class declaration
 * 
 * @author gl10
 * 
 */
public class ListDeclClass extends TreeList<AbstractDeclClass> {

    private static final Logger LOG = Logger.getLogger(ListDeclClass.class);
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclClass c : getList()) {
            c.decompile(s);
            s.println();
        }
    }

    /**
     * Verify contextually each class.
     * 
     * @param compiler
     */
    void verifyListClass(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify listClass: start");
        for (AbstractDeclClass c : getList()) {
            c.verifyClass(compiler);
        }
        LOG.debug("verify listClass: end");
        this.verifyListClassMembers(compiler);
    }

    /**
     * Verify fields and methods contextually for each class.
     * 
     * @param compiler
     */
    public void verifyListClassMembers(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify listClassMembers: start");
        for (AbstractDeclClass c : getList()) {
            c.verifyClassMembers(compiler);
        }
        LOG.debug("verify listClassMembers: end");
        this.verifyListClassBody(compiler);
    }
    
    /**
     * Verify fields initialization and methods declaration contextually for each class.
     *
     * @param compiler
     */
    public void verifyListClassBody(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify listClassBody: start");
        for (AbstractDeclClass c : getList()) {
            c.verifyClassBody(compiler);
        }
        LOG.debug("verify listClassBody: end");
    }

    /**
     * Generate assembly code for the methods table
     * Init the table and then create each class in the table.
     *
     * @param compiler
     */
    public void codeGenListDeclClass(DecacCompiler compiler) {
        compiler.addComment("Beginning of methods table:");
        codeGenDeclClassObject(compiler);
        for (AbstractDeclClass c : getList()) {
            c.codeGenDeclClass(compiler);
            compiler.stackManager.incrementVarCounter();
        }
    }

    /**
     * Generate assembly code for the Object class in
     * the method table
     *
     * @param compiler
     */
    private void codeGenDeclClassObject(DecacCompiler compiler) {
        compiler.addInstruction(new LOAD(new NullOperand(), Register.R0));
        DAddr offset = new RegisterOffset(compiler.stackManager.getGbOffsetCounter(), Register.GB);
        compiler.stackManager.incrementGbOffsetCounter();
        compiler.addInstruction(new STORE(Register.R0, offset));
        Label label = compiler.labelManager.createLabel("code.Object.equals");
        compiler.addInstruction(new LOAD(new LabelOperand(label), Register.R0));


        offset = new RegisterOffset(compiler.stackManager.getGbOffsetCounter(), Register.GB);
        compiler.stackManager.incrementGbOffsetCounter();
        compiler.addInstruction(new STORE(Register.R0, offset));

        // table of method for object
        compiler.environmentType.OBJECT.getDefinition().setOperand(new RegisterOffset(1, Register.GB));

        // space for Object's superclass (null) and the method equals 
        compiler.stackManager.incrementVarCounter();
        compiler.stackManager.incrementVarCounter();
    }
    /**
     * Generate assembly code to implement all classes methods in the method table.
     *
     * @param compiler
     */
    public void codeGenMethodImplementation(DecacCompiler compiler) {
        codeGenObjectMethods(compiler);
        for (AbstractDeclClass c : getList()) {
            c.codeGenMethodImplementation(compiler);
        }
    }

    /**
     * Generate assembly code to implement Object's methods.
     *
     * @param compiler
     */
    private void codeGenObjectMethods(DecacCompiler compiler) {
        compiler.addComment("Initialisation and methods code of class Object");

        // Object init instructions
        Label label = compiler.labelManager.createLabel("init.Object");
        compiler.addLabel(label);
        compiler.addInstruction(new RTS());
        
        // Object equals instructions
        label = compiler.labelManager.createLabel("code.Object.equals");
        compiler.addLabel(label);
        RegisterOffset offset = new RegisterOffset(-2, Register.LB);
        compiler.addInstruction(new LOAD(offset, Register.R1));
        offset = new RegisterOffset(-3, Register.LB);
        compiler.addInstruction(new LOAD(offset, Register.R0));
        compiler.addInstruction(new CMP(Register.R0, Register.R1));
        compiler.addInstruction(new SEQ(Register.R0));
        compiler.addInstruction(new RTS());
    }
}
