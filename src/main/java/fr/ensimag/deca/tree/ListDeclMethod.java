package fr.ensimag.deca.tree;

import java.util.ArrayList;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.LabelOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;

public class ListDeclMethod extends TreeList<AbstractDeclMethod> {

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclMethod m : getList()) {
            m.decompile(s);
        }
    }

    public void verifyListDeclMethod(DecacCompiler compiler,
            ClassDefinition currentClass) throws ContextualError {
        for (AbstractDeclMethod m : getList()) {
            m.verifyDeclMethod(compiler, currentClass);
        }
    }
    
    public void verifyListDeclMethodBody(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError {
        for (AbstractDeclMethod m : getList()) {
            m.verifyDeclMethodBody(compiler, currentClass);
        }
    }

    public void codeGenImplementMethod(DecacCompiler compiler, ClassDefinition currentClass) {
        for (AbstractDeclMethod m : getList()) {
            m.codeGenImplementMethod(compiler, currentClass);
        }
    }
    /* Store table of methodes in class */
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
