package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import fr.ensimag.ima.pseudocode.instructions.WFLOATX;
import fr.ensimag.ima.pseudocode.instructions.WINT;
import org.apache.commons.lang.Validate;

/**
 * Print statement (print, println, ...).
 *
 * @author gl10
 * @date 25/04/2022
 */
public abstract class AbstractPrint extends AbstractInst {

    private boolean printHex;
    private ListExpr arguments = new ListExpr();
    
    abstract String getSuffix();

    public AbstractPrint(boolean printHex, ListExpr arguments) {
        Validate.notNull(arguments);
        this.arguments = arguments;
        this.printHex = printHex;
    }

    public ListExpr getArguments() {
        return arguments;
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        for (AbstractExpr a : getArguments().getList()) {
            Type t = a.verifyExpr(compiler, localEnv, currentClass);
            if (!t.isInt() && !t.isFloat() && !t.isString()) {
                throw new ContextualError("expression is not an int, a float, or a string", a.getLocation());
            }
        } 
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        for (AbstractExpr a : getArguments().getList()) {
            a.codeGenPrint(compiler);
            if (a.getType().isFloat() || a.getType().isInt()) {
                addInstructionPrint(compiler, a);
            }
        }
    }

    protected void addInstructionPrint(DecacCompiler compiler, AbstractExpr a) {
        if (a.getType().isFloat()) {
            if (printHex) {
                compiler.addInstruction(new WFLOATX());
            } else {
                compiler.addInstruction(new WFLOAT());
            }
        } else if (a.getType().isInt()) {
            compiler.addInstruction(new WINT());
        } else {
            throw new DecacInternalError("Unexpected case in AbstractExpr.codeGenPrint");
        }
    }

    private boolean getPrintHex() {
        return printHex;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        if (printHex) {
            s.print("print" + getSuffix() +"x(");
        } else {
            s.print("print" + getSuffix() +"(");
        }
        
        for (AbstractExpr a : getArguments().getList()) {
            a.decompile(s);
            if (this.getArguments().getList().lastIndexOf(a) != this.getArguments().getList().size() - 1)
                s.print(", ");
        }
        s.print(");");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        arguments.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        arguments.prettyPrint(s, prefix, true);
    }

}
