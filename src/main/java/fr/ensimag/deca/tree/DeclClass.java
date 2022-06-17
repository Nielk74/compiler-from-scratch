package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.SUBSP;

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

    /* Pass 1 */
    @Override
    protected void verifyClass(DecacCompiler compiler) throws ContextualError {
        // checks if the class name is already used in a definition
        if (compiler.environmentType.defOfType(name.getName()) != null) {
            throw new ContextualError("Wrong class name: " + name.getName() + " is already defined", name.getLocation());
        }

        // if the superclass is Object we have to set its type and definition
        if (superclass.getName().getName().equals("Object")) {
            superclass.setDefinition(compiler.environmentType.OBJECT.getDefinition());
        } else {
            TypeDefinition superClassType = compiler.environmentType.defOfType(superclass.getName());
            if (superClassType == null || !(superClassType instanceof ClassDefinition)) {
                throw new ContextualError("Wrong superclass name: " + superclass.getName() + " is not defined", superclass.getLocation());
            }
            superclass.setDefinition(superClassType);
        }
       
        // set the class type
        ClassType type = compiler.environmentType.addClassType(compiler, name.getName(), superclass.getClassDefinition(), name.getLocation());
        name.setDefinition(type.getDefinition());
        name.getClassDefinition().setLocation(this.getLocation());
    }

    /* Pass 2 */
    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {
        name.getClassDefinition().setNumberOfFields(superclass.getClassDefinition().getNumberOfFields());
        // checks if the fields are well defined
        EnvironmentExp membersEnv = new EnvironmentExp(superclass.getClassDefinition().getMembers());
        fields.verifyListDeclField(compiler, membersEnv, this.name.getClassDefinition());
    }
    
    /* Pass 3 */
    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        ClassDefinition currentClass = this.name.getClassDefinition();
        fields.verifyListDeclFieldInit(compiler, currentClass.getMembers(), currentClass);
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

    @Override
    protected void codeGenClassInit(DecacCompiler compiler) {
        compiler.addComment("Initialisation and methods code of class " + name.getName());
        compiler.addLabel(compiler.labelManager.createLabel("init." + name.getName()));

        // codegen superclass fields init
        if (superclass.getClassDefinition().getType() != compiler.environmentType.OBJECT) {
            RegisterOffset offset = new RegisterOffset(-2, Register.LB);
            compiler.addInstruction(new LOAD(offset, Register.R0));
            compiler.addInstruction(new PUSH(Register.R0));
            compiler.addInstruction(new BSR(compiler.labelManager.createLabel("init." + superclass.getName())));
            compiler.addInstruction(new SUBSP(1));
        }

        // codegen current class fields init
        fields.codeGenListDeclField(compiler);
        compiler.addInstruction(new RTS());
    }
}
