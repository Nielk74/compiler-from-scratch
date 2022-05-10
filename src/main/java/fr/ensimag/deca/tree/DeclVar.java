package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * @author gl10
 * @date 25/04/2022
 */
public class DeclVar extends AbstractDeclVar {

    
    final private AbstractIdentifier type;
    final private AbstractIdentifier varName;
    final private AbstractInitialization initialization;

    public DeclVar(AbstractIdentifier type, AbstractIdentifier varName, AbstractInitialization initialization) {
        Validate.notNull(type);
        Validate.notNull(varName);
        Validate.notNull(initialization);
        this.type = type;
        this.varName = varName;
        this.initialization = initialization;
    }

    @Override
    protected void verifyDeclVar(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        Symbol typeSymbol = type.getName();
        Type t;
        switch (typeSymbol.getName()) {
            case "int":
                t = compiler.environmentType.INT;
                break;
            case "bool":
                t = compiler.environmentType.BOOLEAN;
                break;
            case "string":
                t = compiler.environmentType.STRING;
                break;
            case "float":
                t = compiler.environmentType.FLOAT;
                break;
            default:
                throw new ContextualError(typeSymbol.getName() + " is not a valid type", type.getLocation());
        }
        varName.setDefinition(new VariableDefinition(t, varName.getLocation()));
        type.setDefinition(new TypeDefinition(t, type.getLocation()));
        // TODO : verify initialization if initialization is not of type NoInitialization
        try {
            localEnv.declare(varName.getName(), varName.    getExpDefinition());
        } catch (DoubleDefException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            throw new ContextualError(typeSymbol.getName() + " is already defined", type.getLocation());
        }
    }

    
    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        type.iter(f);
        varName.iter(f);
        initialization.iter(f);
    }
    
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        varName.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
    }
}
