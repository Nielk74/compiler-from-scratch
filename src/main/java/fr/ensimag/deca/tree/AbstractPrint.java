package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

import javax.xml.namespace.QName;

import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import fr.ensimag.ima.pseudocode.instructions.WFLOATX;
import fr.ensimag.ima.pseudocode.instructions.WINT;
import org.apache.commons.lang.Validate;

/**
 * Print statement (print, println, ...).
 *
 * @author gl10
 * 
 */
public abstract class AbstractPrint extends AbstractInst {

    /**
     * True if we want to print the number in hexadecimal, otherwise false. 
     */
    private boolean printHex;
    /**
     * The arguments list to print.
     */
    private ListExpr arguments = new ListExpr();
    
    /**
     * Return the suffix.
     * 
     * @return String
     */
    abstract String getSuffix();

    /**
     * @param printHex True if we want to print in hexadecimal, otherwise false.
     * @param arguments List of expression to print.
     */
    public AbstractPrint(boolean printHex, ListExpr arguments) {
        Validate.notNull(arguments);
        this.arguments = arguments;
        this.printHex = printHex;
    }

    /**
     * Return the arguments variable.
     * 
     * @return ListExpr
     */
    public ListExpr getArguments() {
        return arguments;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        for (AbstractExpr a : getArguments().getList()) {
            Type t = a.verifyExpr(compiler, localEnv, currentClass);
            if(a.getClass() == Identifier.class){
                //methode
                ExpDefinition def = ((Identifier)a).getExpDefinition();
                if(def.isMethod()){
                    throw new ContextualError("Wrong parameter type of print - expected: int, float or string ≠ current: method", a.getLocation());
                }
            }
            if (!t.isInt() && !t.isFloat() && !t.isString()) {
                throw new ContextualError("Wrong parameter type of print - expected: int, float or string ≠ current: "+ t, a.getLocation());
            }
        } 
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        for (AbstractExpr a : getArguments().getList()) {
            
            a.codeGenPrint(compiler);
            if (a.getType().isFloat() || a.getType().isInt()) {
                addInstructionPrint(compiler, a);
            }
        }
    }

    /**
     * Add in the assembly code, the instruction to print, according to the type to print.
     * 
     * @param compiler
     * @param a The expression to print, to know the type to print.
     */
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
            throw new DecacInternalError("Unexpected case in AbstractExpr.codeGenPrint, current type: "+a.getType()+" ≠ expected type: float or int");
        }
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    protected void iterChildren(TreeFunction f) {
        arguments.iter(f);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        arguments.prettyPrint(s, prefix, true);
    }

}
