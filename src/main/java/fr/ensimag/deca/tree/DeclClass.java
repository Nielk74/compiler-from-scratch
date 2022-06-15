package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.STORE;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 * 
 * @author gl10
 * @date 25/04/2022
 */
public class DeclClass extends AbstractDeclClass {

    final private AbstractIdentifier name;
    final private AbstractIdentifier superclass;
    final private ListDeclField fields;
    final private ListDeclMethod methods;

    public DeclClass(AbstractIdentifier name, AbstractIdentifier superclass, ListDeclField fields, ListDeclMethod methods) {
        Validate.notNull(name);
        Validate.notNull(superclass);
        Validate.notNull(fields);
        Validate.notNull(methods);
        this.name = name;
        this.superclass = superclass;
        this.fields = fields;
        this.methods = methods;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("class ");
        name.decompile(s);
        s.print(" extends ");
        superclass.decompile(s);
        s.println(" {");
        s.indent();
        fields.decompile(s);
        methods.decompile(s);
        s.unindent();
        s.println("}");
        
    }

    @Override
    protected void verifyClass(DecacCompiler compiler) throws ContextualError {
        // Passe 1
        // checks if the class name is already used in a definition
        if (compiler.environmentType.defOfType(name.getName()) != null) {
            throw new ContextualError("Wrong class name: " + name.getName() + " is already defined", name.getLocation());
        }

        // if the superclass is Object we have to set its type and definition
        if (superclass.getName().getName().equals("Object")) {
            superclass.setType(compiler.environmentType.OBJECT);
            superclass.setDefinition(compiler.environmentType.OBJECT.getDefinition());
        }

        // set the class type
        ClassType type = compiler.environmentType.addClassType(compiler, name.getName(), superclass.getClassDefinition(), name.getLocation());
        name.setType(type);
        name.setDefinition(type.getDefinition());
        name.getClassDefinition().setLocation(this.getLocation());
        // TODO : pour le extends il faudra vérifer que superclass est bien dans EnvExp et que c'est bien une ClassType
    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }
    
    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }


    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        name.prettyPrint(s, prefix, false);
        superclass.prettyPrint(s, prefix, false);
        fields.prettyPrint(s, prefix, false);
        methods.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        this.name.iter(f);
        this.superclass.iter(f);
        this.fields.iter(f);
        this.methods.iter(f);
    }

    @Override
    protected void codeGenDeclClass(DecacCompiler compiler) {
        name.codeGenDeclClass(compiler);

        DAddr offset = new RegisterOffset(compiler.stackManager.getGbOffsetCounter(), Register.GB);
        compiler.stackManager.incrementGbOffsetCounter();
        compiler.addInstruction(new STORE(Register.R0, offset));
        name.getClassDefinition().setOperand(offset);

        // TODO : incrémenter compiler.stackManager.incrementVarCounter(); pour chaque méthode
    }
}
