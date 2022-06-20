package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.STORE;

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

        TypeDefinition def = compiler.environmentType.defOfType(typeSymbol);
        if (def == null) {
            throw new ContextualError("Unknown type: " + typeSymbol.getName(), type.getLocation());
        }
        if (def.getType().isVoid()) {
            throw new ContextualError("Wrong variable type - unexpected: void", type.getLocation());
        }
        TypeDefinition typeDef = compiler.environmentType.defOfType(typeSymbol);
        if (typeDef == null) {
            throw new ContextualError("Unknown type: " + typeSymbol.getName(), type.getLocation());
        }
        Type t = typeDef.getType();

        varName.setDefinition(new VariableDefinition(t, varName.getLocation()));
        type.setDefinition(compiler.environmentType.defOfType(typeSymbol));
        type.setType(t);
        this.initialization.verifyInitialization(compiler, t, localEnv, currentClass);
        try {
            localEnv.declare(varName.getName(), varName.getExpDefinition());
        } catch (DoubleDefException e) {
            throw new ContextualError(e.getMessage(), type.getLocation());
        }
    }

    @Override
    protected void codeGenDeclVar(DecacCompiler compiler) {
        this.initialization.codeGenInitialization(compiler, 2);
        RegisterOffset offset = new RegisterOffset(compiler.stackManager.getLbOffsetCounter(), Register.LB);

        if (this.initialization instanceof Initialization) {
            compiler.addInstruction(new STORE(Register.getR(2), offset),
                    this.varName.getName() + " => " + compiler.stackManager.getLbOffsetCounter() + "(LB)");
        }

        compiler.stackManager.incrementLbOffsetCounter();
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
