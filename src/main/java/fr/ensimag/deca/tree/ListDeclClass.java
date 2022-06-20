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
        this.verifyListClassMembers(compiler);
    }

    /**
     * Pass 2 of [SyntaxeContextuelle]
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
     * Pass 3 of [SyntaxeContextuelle]
     */
    public void verifyListClassBody(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify listClassBody: start");
        for (AbstractDeclClass c : getList()) {
            c.verifyClassBody(compiler);
        }
        LOG.debug("verify listClassBody: end");
    }

    public void codeGenListDeclClass(DecacCompiler compiler) {
        compiler.addComment("Beginning of methods table:");
        codeGenDeclClassObject(compiler);
        for (AbstractDeclClass c : getList()) {
            c.codeGenDeclClass(compiler);
            compiler.stackManager.incrementVarCounter();
        }
    }
    
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
        compiler.stackManager.incrementVarCounter();
    }

    public void codeGenMethodImplementation(DecacCompiler compiler) {
        codeGenObjectMethods(compiler);
        for (AbstractDeclClass c : getList()) {
            c.codeGenMethodImplementation(compiler);
        }
    }

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
