package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.ParamDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;

public class DeclParam extends AbstractDeclParam {

    private AbstractIdentifier type;
    private AbstractIdentifier name;
    private int index;

    public DeclParam(AbstractIdentifier type, AbstractIdentifier name) {
        Validate.notNull(type);
        Validate.notNull(name);
        this.type = type;
        this.name = name;
    }

    @Override
    protected void setIndex(int index) {
        this.index = index;
    }

    @Override
    protected Type verifyDeclParam(DecacCompiler compiler) throws ContextualError {
        // verify type
        Symbol typeSymbol = type.getName();

        TypeDefinition typeDef = compiler.environmentType.defOfType(typeSymbol);
        if (typeDef == null) {
            throw new ContextualError("Unknown type: " + typeSymbol.getName(), type.getLocation());
        }

        Type t = typeDef.getType();

        // check if type == void
        if (t.isVoid()) {
            throw new ContextualError("Wrong type: void is forbidden", type.getLocation());
        }
        type.setType(t);
        type.setDefinition(typeDef);
        return t;
    }

    @Override
    protected void verifyDeclParamEnv(DecacCompiler compiler, EnvironmentExp localEnv) throws ContextualError {
        ParamDefinition paramDef = new ParamDefinition(type.getType(), name.getLocation());
        name.setDefinition(paramDef);

        try {
            localEnv.declare(name.getName(), name.getExpDefinition());
        } catch (DoubleDefException e) {
            throw new ContextualError("Wrong parameter definition: Parameter name " + name.getName().getName()
            + " is already defined", name.getLocation());
        }
    }

    @Override
    protected void codeGenDeclParam(DecacCompiler compiler) {
        ((ParamDefinition) name.getDefinition()).setOperand(new RegisterOffset(-(index + 3), Register.LB));
    }

    @Override
    public void decompile(IndentPrintStream s) {
        this.type.decompile(s);
        s.print(" ");
        this.name.decompile(s);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        name.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        this.type.iter(f);
        this.name.iter(f);

    }
}
