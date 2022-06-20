package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.ErrorCatcher;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.SUBSP;

public class MethodCall extends AbstractExpr {

    private AbstractExpr expr;
    private AbstractIdentifier method;
    private ListExpr args;

    public MethodCall(AbstractExpr expr, AbstractIdentifier method, ListExpr args) {
        Validate.notNull(expr);
        Validate.notNull(method);
        Validate.notNull(args);
        this.expr = expr;
        this.method = method;
        this.args = args;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        Type typeClass =expr.verifyExpr(compiler, localEnv, currentClass);
        if (typeClass.isNull()) {
            throw new ContextualError("Cannot call method on null", this.getLocation());
        }
        ClassDefinition classDef = ((ClassType) typeClass).getDefinition();
        ExpDefinition defTest = classDef.getMembers().get(method.getName());
        if (defTest == null) {
            throw new ContextualError("Unknown method: " + method.getName(), method.getLocation());
        }
        if (!defTest.isMethod()) {
            throw new ContextualError("Not a method: " + method.getName(), method.getLocation());
        }
        MethodDefinition methodDef = (MethodDefinition) defTest;
        method.setDefinition(methodDef);
        Signature sig = methodDef.getSignature();

        if (args.size() != sig.size()) {
            throw new ContextualError("Wrong number of arguments in the call of method " + typeClass.getName().getName()
                    + "." + method.getName().getName(), this.getLocation());
        }

        for (int i = 0; i < args.size(); i++) {
            AbstractExpr expr = args.getList().get(i).verifyRValue(compiler, localEnv, currentClass, sig.paramNumber(i));
            args.set(i, expr);
        }

        this.setType(methodDef.getType());

        return methodDef.getType();
    }

    @Override
    public void decompile(IndentPrintStream s) {

        if (!this.expr.isImplicit()) {
            this.expr.decompile(s);
            s.print(".");
        }
        this.method.decompile(s);
        s.print("(");
        for (AbstractExpr a : this.args.getList()) {
            a.decompile(s);
            if (this.args.getList().lastIndexOf(a) != this.args.getList().size() - 1)
                s.print(", ");
        }
        s.print(")");
    }

    @Override
    protected void codeGenExp(DecacCompiler compiler, int register_name) {
        ClassDefinition classDef = ((ClassType) (expr.getType())).getDefinition();
        MethodDefinition methodDef = (MethodDefinition) classDef.getMembers().get(method.getName());

        // reserve space for the arguments + address of the class
        compiler.addComment(
                "Call of method " + classDef.getType().getName().getName() + "." + method.getName().getName());
        compiler.addInstruction(new ADDSP(methodDef.getSignature().size() + 1));
        compiler.stackManager.setMaxMethodCallParamNb(methodDef.getSignature().size() + 1);
        
        // load address of class
        compiler.addComment("Push address of class");
        expr.codeGenExp(compiler, register_name);
        compiler.addInstruction(new STORE(Register.getR(register_name), new RegisterOffset(0, Register.SP)));

        // push arguments
        compiler.addComment("Push arguments");
        int i = 1;
        for (AbstractExpr expr : args.getList()) {
            expr.codeGenExp(compiler, register_name);
            compiler.addInstruction(new STORE(Register.getR(register_name), new RegisterOffset(-i, Register.SP)));
            i++;
        }

        // check null
        compiler.addComment("Check null");
        compiler.addInstruction(new LOAD(new RegisterOffset(0, Register.SP), Register.getR(0)));
        if (!compiler.getCompilerOptions().getNocheck()) {
            compiler.addInstruction(new CMP(new NullOperand(), Register.getR(0)));
            compiler.addInstruction(new BEQ(compiler.labelManager.getLabel(ErrorCatcher.NULL_ERROR)));
        }

        // call method
        compiler.addComment("Call method");
        compiler.addInstruction(new LOAD(new RegisterOffset(0, Register.getR(0)), Register.getR(0)));
        compiler.addInstruction(new BSR(new RegisterOffset(methodDef.getIndex() + 1, Register.getR(0))));

        // Pop arguments
        compiler.addComment("Pop arguments");
        compiler.addInstruction(new SUBSP(methodDef.getSignature().size() + 1));

        // load return value
        if (methodDef.getType() != null) {
            compiler.addComment("Load return value");
            compiler.addInstruction(new LOAD(Register.R0, Register.getR(register_name)));
        }
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        expr.prettyPrint(s, prefix, false);
        method.prettyPrint(s, prefix, false);
        args.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        this.expr.iter(f);
        this.method.iter(f);
        this.args.iter(f);
    }

}
