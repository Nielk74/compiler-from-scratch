package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.context.ParamDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
/**
 * Declaration of a parameter. 
 * 
 * @author gl10
 * 
 */
public class DeclParam extends AbstractDeclParam {

    // Type of the parameter. 
    private AbstractIdentifier type;
    // Name of the parameter. 
    private AbstractIdentifier name;
    // Index of the parameter. 
    private int index;

    /**
     * @param type Type of the parameter. 
     * @param name Name of the parameter.
     */
    public DeclParam(AbstractIdentifier type, AbstractIdentifier name) {
        Validate.notNull(type);
        Validate.notNull(name);
        this.type = type;
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setIndex(int index) {
        this.index = index;
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
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
        ((ParamDefinition) name.getDefinition()).setOperand(new RegisterOffset(-(index + 3), Register.LB));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void decompile(IndentPrintStream s) {
        this.type.decompile(s);
        s.print(" ");
        this.name.decompile(s);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        name.prettyPrint(s, prefix, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void iterChildren(TreeFunction f) {
        this.type.iter(f);
        this.name.iter(f);

    }
}
