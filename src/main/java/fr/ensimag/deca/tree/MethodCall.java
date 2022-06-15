package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

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
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public void decompile(IndentPrintStream s) {

        if (this.expr!=null){
            this.expr.decompile(s);
            s.print(".");
        }
        this.method.decompile(s);
        s.print("(");
        this.args.decompile(s);
        s.print(")");
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
