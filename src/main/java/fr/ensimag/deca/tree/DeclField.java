package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;

public class DeclField extends AbstractDeclField {
    
    final private AbstractIdentifier type;
    final private AbstractIdentifier name;
    final private AbstractInitialization init;
    final private Visibility visibility;

    public DeclField(AbstractIdentifier type, AbstractIdentifier name, AbstractInitialization init, Visibility visibility) {
        Validate.notNull(type);
        Validate.notNull(name);
        Validate.notNull(init);
        Validate.notNull(visibility);
        this.type = type;
        this.name = name;
        this.init = init;
        this.visibility = visibility;
    }

    @Override
    protected void verifyDeclField(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void codeGenDeclField(DecacCompiler compiler) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(this.visibility +" ");
        this.type.decompile(s);
        s.print(" ");
        this.name.decompile(s);
        this.init.decompile(s);
        s.println(";");

    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        name.prettyPrint(s, prefix, false);
        init.prettyPrint(s, prefix, false);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        
        this.type.iter(f);
        this.name.iter(f);
        this.init.iter(f);
    }
}
