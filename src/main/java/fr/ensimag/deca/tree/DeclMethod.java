package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.Label;

/**
 * Declaration of a method.
 * 
 * @author gl10
 * 
 */
public class DeclMethod extends AbstractDeclMethod {

    // Type of the method. 
    private AbstractIdentifier type;
    // Name of the method. 
    private AbstractIdentifier name;
    // Parameters of the method. 
    private ListDeclParam params;
    // Body of the method. 
    private AbstractMethodBody body;

    /**
     * @param type Type of the method. 
     * @param name Name of the method. 
     * @param params Parameters of the method.
     * @param body Body of the method.
     * 
     */
    public DeclMethod(AbstractIdentifier type, AbstractIdentifier name, ListDeclParam params, AbstractMethodBody body) {
        Validate.notNull(type);
        Validate.notNull(name);
        Validate.notNull(params);
        Validate.notNull(body);
        this.type = type;
        this.name = name;
        this.params = params;
        this.body = body;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void verifyDeclMethod(DecacCompiler compiler, ClassDefinition currentClass)
            throws ContextualError {
        // verify return type
        Symbol typeSymbol = type.getName();

        TypeDefinition typeDef = compiler.environmentType.defOfType(typeSymbol);
        if (typeDef == null) {
            throw new ContextualError("Unknown type: " + typeSymbol.getName(), type.getLocation());
        }

        Type t = typeDef.getType();
        type.setType(t);
        type.setDefinition(typeDef);
        Signature sig = params.verifyListDeclParam(compiler);

        ExpDefinition superMethodDefTest = currentClass.getSuperClass().getMembers().get(name.getName());
        int methodIndex;
        if (superMethodDefTest != null) {
            // check if the signatures are the same
            MethodDefinition superMethodDef = superMethodDefTest
                    .asMethodDefinition("Wrong method definition: Method name " + name.getName().getName()
                            + " is already defined as a ", name.getLocation());
            if (!superMethodDef.getSignature().sameSignature(sig)) {
                throw new ContextualError("Wrong method definition: Method name " + name.getName().getName()
                        + " is already taken with a different signature", getLocation());
            }

            // check if the return type is the same as the super method
            if (!type.getType().sameType(superMethodDef.getType())) {
                String errorMessage = "Wrong method definition: Return type of " + name.getName().getName()
                        + " doesn't match";
                ClassType superMethodType = superMethodDef.getType().asClassType(errorMessage, getLocation());
                ClassType currentMethodType = type.getType().asClassType(errorMessage, getLocation());
                if (!currentMethodType.isSubClassOf(superMethodType)) {
                    throw new ContextualError(errorMessage, getLocation());
                }
            }

            methodIndex = superMethodDef.getIndex();

        } else {
            // new method definition
            currentClass.incNumberOfMethods();
            methodIndex = currentClass.getNumberOfMethods();
        }
        MethodDefinition newMethodDef = new MethodDefinition(t, getLocation(), sig, methodIndex);
        Label label = compiler.labelManager
                .createLabel("code." + currentClass.getType().getName().getName() + "." + name.getName().getName()+"."+methodIndex);
        newMethodDef.setLabel(label);
        try {
            currentClass.getMembers().declare(name.getName(), newMethodDef);
        } catch (DoubleDefException e) {
            throw new ContextualError(
                    "Wrong method definition: Method name " + name.getName().getName() + " is already taken",
                    getLocation());
        }
        name.setDefinition(newMethodDef);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void verifyDeclMethodBody(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError {
        // new environment with parameters
        EnvironmentExp paramEnv = new EnvironmentExp(currentClass.getMembers());
        params.verifyListDeclParamEnv(compiler, paramEnv);
        body.verifyMethodBody(compiler, paramEnv, currentClass, type.getType());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void codeGenImplementMethod(DecacCompiler compiler) {
        Label label = name.getMethodDefinition().getLabel();
        compiler.addLabel(label);
        // generate code for the body
        body.codeGenMethodBody(compiler, type.getType());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void decompile(IndentPrintStream s) {
        this.type.decompile(s);
        s.print(" ");
        this.name.decompile(s);
        s.print("(");
        this.params.decompile(s);
        s.print(") ");
        this.body.decompile(s);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        name.prettyPrint(s, prefix, false);
        params.prettyPrint(s, prefix, false);
        body.prettyPrint(s, prefix, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void iterChildren(TreeFunction f) {
        this.type.iter(f);
        this.name.iter(f);
        this.params.iter(f);
        this.body.iter(f);
    }

}
