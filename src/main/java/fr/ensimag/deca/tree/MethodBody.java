package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.ErrorCatcher;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import fr.ensimag.ima.pseudocode.instructions.TSTO;

public class MethodBody extends AbstractMethodBody {

    private ListDeclVar declVariables;
    private ListInst insts;

    public MethodBody(ListDeclVar declVariables, ListInst insts) {
        Validate.notNull(declVariables);
        Validate.notNull(insts);
        this.declVariables = declVariables;
        this.insts = insts;
    }

    @Override
    protected void verifyMethodBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        declVariables.verifyListDeclVariable(compiler, localEnv, currentClass); 
        insts.verifyListInst(compiler, localEnv, currentClass, returnType);       
    }

    /* codegen of declaration variable and instructions */
    @Override
    protected void codeGenMethodBody(DecacCompiler compiler, Type returnType) {
        // reset the var counter to use it in method bloc
        compiler.stackManager.resetStackCounters();

        Label endLabel = compiler.labelManager.createEndMethodLabel();

        TSTO tsto = new TSTO(0);
        compiler.addInstruction(tsto);
        if (!compiler.getCompilerOptions().getNocheck()) {
            compiler.addInstruction(new BOV(compiler.labelManager.getLabel(ErrorCatcher.SO_ERROR)));
        }
        // local variables
        ADDSP addsp = new ADDSP(0);
        compiler.addInstruction(addsp);

        // save registers R2 to RMAX
        for (int i = 2; i <= compiler.getCompilerOptions().getRegisterMax(); i++) {
            compiler.addInstruction(new PUSH(Register.getR(i)));
            compiler.stackManager.incrementSavedRegisterCounter();
        }

        declVariables.codeGenListDeclVariable(compiler);
        insts.codeGenListInst(compiler);

        // return type != void
        if (!returnType.isVoid() && !compiler.getCompilerOptions().getNocheck()) {
            compiler.addInstruction(new BRA(compiler.labelManager.getLabel(ErrorCatcher.RET_ERROR)));
        }
        compiler.addLabel(endLabel);

        // restore registers R2 to RMAX
        for (int i = compiler.getCompilerOptions().getRegisterMax(); i > 1; i--) {
            compiler.addInstruction(new POP(Register.getR(i)));
        }

        compiler.addInstruction(new RTS());

        // update the tsto value
        tsto.setValue(compiler.stackManager.getStackSize());
        addsp.setValue(compiler.stackManager.getVarCounter());
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.println("{");
        s.indent();
        this.declVariables.decompile(s);
        this.insts.decompile(s);
        s.unindent();
        s.println("}");

    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        declVariables.prettyPrint(s, prefix, false);
        insts.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        this.insts.iter(f);
        this.declVariables.iter(f);        
    }

}
