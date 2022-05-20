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
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.STORE;

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
            case "float":
                t = compiler.environmentType.FLOAT;
                break;
            default:
                throw new ContextualError(typeSymbol.getName() + " is not a valid type", type.getLocation());
        }
        varName.setDefinition(new VariableDefinition(t, varName.getLocation()));
        type.setDefinition(new TypeDefinition(t, type.getLocation()));
        type.setType(t);
        this.initialization.verifyInitialization(compiler, t, localEnv, currentClass);
        try {
            localEnv.declare(varName.getName(), varName.getExpDefinition());
        } catch (DoubleDefException e) {
            throw new ContextualError(typeSymbol.getName() + " is already defined", type.getLocation());
        }
    }

    @Override
    protected void codeGenDeclVar(DecacCompiler compiler) {
        this.initialization.codeGenInitialization(compiler, 2);
        RegisterOffset offset = new RegisterOffset(Register.getLbOffsetCounter(), Register.LB);
        Register.incrementLbOffsetCounter();
        compiler.addInstruction(new STORE(Register.getR(2), offset));
        ((AbstractIdentifier) this.varName).getExpDefinition().setOperand(offset);                
    }

    @Override
    public void decompile(IndentPrintStream s) {
        type.decompile(s);
        s.print(" ");
        varName.decompile(s);
        initialization.decompile(s);
        s.println(";");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
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
