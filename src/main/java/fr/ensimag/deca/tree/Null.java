package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

public class Null extends AbstractExpr {

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
                setType(compiler.environmentType.NULL);
                return compiler.environmentType.NULL;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("null");    
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // do nothing, child node
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // do nothing, child node
        
    }

    @Override
    protected void codeGenExp(DecacCompiler compiler, int register_name) {
        compiler.addInstruction(new LOAD(new NullOperand(), Register.getR(register_name)));
    }
    
}
