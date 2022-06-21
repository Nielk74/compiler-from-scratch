package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;

public class DeclField extends AbstractDeclField {

    final private AbstractIdentifier type;
    final private AbstractIdentifier name;
    final private AbstractInitialization init;
    final private Visibility visibility;

    public DeclField(AbstractIdentifier type, AbstractIdentifier name, AbstractInitialization init,
            Visibility visibility) {
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
    protected void verifyDeclField(DecacCompiler compiler, ClassDefinition currentClass)
            throws ContextualError {
        Symbol typeSymbol = type.getName();

        TypeDefinition typeDef = compiler.environmentType.defOfType(typeSymbol);
        if (typeDef == null) {
            throw new ContextualError("Unknown type: " + typeSymbol.getName(), type.getLocation());
        }
        
        // check if type == void
        if (typeDef.getType().isVoid()) {
            throw new ContextualError("Wrong type: void is forbidden", type.getLocation());
        }

        // check if the field name is already defined in the superclass env
        // and that it is a FieldDefinition
        EnvironmentExp superEnvironment = currentClass.getSuperClass().getMembers();
        ExpDefinition superDef = superEnvironment.get(name.getName());
        if (superDef != null && !(superDef.isField())) {
            throw new ContextualError("Wrong field name: " + name.getName()
                    + " is already defined in the super class as a: " + superDef.getClass(), name.getLocation());
        }

        // set the type and the definition of the type identifier
        Type t = typeDef.getType();
        type.setDefinition(typeDef);
        type.setType(t);

        currentClass.incNumberOfFields();
        FieldDefinition fieldDefinition = new FieldDefinition(t, name.getLocation(), visibility, currentClass,
                currentClass.getNumberOfFields());
        name.setDefinition(fieldDefinition);

        // declare the new field definition in the current class environment
        try {
            currentClass.getMembers().declare(name.getName(), fieldDefinition);
        } catch (DoubleDefException e) {
            throw new ContextualError("Wrong field name: " + name.getName() + " is already defined",
                    name.getLocation());
        }
    }

    /* Pass 3 */
    @Override
    protected void verifyDeclFieldInit(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError {
        Type t = type.getType();
        this.init.verifyInitialization(compiler, t, currentClass.getMembers(), currentClass);
    }

    @Override
    protected void codeGenDeclField(DecacCompiler compiler) {
        compiler.addComment("attribute " + name.getName() + " of type " + type.getType());
        // TODO register allocation
        init.codeGenInitialization(compiler, 2, type.getType());
        RegisterOffset offset = new RegisterOffset(-2, Register.LB);
        compiler.addInstruction(new LOAD(offset, Register.R1));
        offset = new RegisterOffset(name.getFieldDefinition().getIndex(), Register.R1);
        compiler.addInstruction(new STORE(Register.getR(2), offset));
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(this.visibility + " ");
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
        init.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {

        this.type.iter(f);
        this.name.iter(f);
        this.init.iter(f);
    }
}
