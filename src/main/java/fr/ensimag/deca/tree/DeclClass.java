package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.ErrorCatcher;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.SUBSP;
import fr.ensimag.ima.pseudocode.instructions.TSTO;

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

        // sets the definition of the superclass

        TypeDefinition superClassType = compiler.environmentType.defOfType(superclass.getName());
        if (superClassType == null || !(superClassType.isClass())) {
            throw new ContextualError("Wrong superclass name: " + superclass.getName() + " is not defined", superclass.getLocation());
        }
        superclass.setDefinition(superClassType);
       
        // set the class definition
        ClassType type = compiler.environmentType.addClassType(compiler, name.getName(), superclass.getClassDefinition(), name.getLocation());
        name.setDefinition(type.getDefinition());
        name.getClassDefinition().setLocation(this.getLocation());
        name.setType(type);
    }

    /* Pass 2 */
    @Override
    protected void verifyClassMembers(DecacCompiler compiler) throws ContextualError {
        // update the number of fields and methods of the current class
        name.getClassDefinition().setNumberOfFields(superclass.getClassDefinition().getNumberOfFields());
        name.getClassDefinition().setNumberOfMethods(superclass.getClassDefinition().getNumberOfMethods());

        // checks if the fields are well defined
        fields.verifyListDeclField(compiler, name.getClassDefinition());
        methods.verifyListDeclMethod(compiler, name.getClassDefinition());
    }
    
    /* Pass 3 */
    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        ClassDefinition currentClass = this.name.getClassDefinition();
        fields.verifyListDeclFieldInit(compiler, currentClass);
        methods.verifyListDeclMethodBody(compiler, currentClass);
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
        // load superclass
        name.codeGenDeclClass(compiler);
        DAddr offset = new RegisterOffset(compiler.stackManager.getGbOffsetCounter(), Register.GB);
        compiler.stackManager.incrementGbOffsetCounter();
        compiler.addInstruction(new STORE(Register.R0, offset));
        name.getClassDefinition().setOperand(offset);
        compiler.addComment("Table of method for class "+ name.getName().getName());
        methods.codeGenListDeclMethod(compiler, name.getClassDefinition());
    }

    @Override
    protected void codeGenClassInit(DecacCompiler compiler) {
        // reset the var counter to use it in init bloc
        compiler.stackManager.resetVarCounter();
        compiler.addLabel(compiler.labelManager.createLabel("init." + name.getName()));

        TSTO tsto = new TSTO(0);
        compiler.addInstruction(tsto);
        if (!compiler.getCompilerOptions().getNocheck()) {
            compiler.addInstruction(new BOV(compiler.labelManager.getLabel(ErrorCatcher.SO_ERROR)));
        }

        // save registers R2 to RMAX
        for(int i = 2; i <= compiler.registerManager.getNbRegisterMax(); i++) {
            compiler.addInstruction(new PUSH(Register.getR(i)));
            compiler.stackManager.incrementVarCounter();
        }

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

        // restore registers R2 to RMAX
        for(int i = compiler.registerManager.getNbRegisterMax(); i > 1; i--) {
            compiler.addInstruction(new POP(Register.getR(i)));
        }

        compiler.addInstruction(new RTS());

        // update the tsto value
        tsto.setValue(compiler.stackManager.getStackSize());
    }

    @Override
    protected void codeGenMethodImplementation(DecacCompiler compiler) {
        compiler.addComment("Initialisation and methods code of class " + name.getName());
        codeGenClassInit(compiler);
        methods.codeGenImplementMethod(compiler, name.getClassDefinition());        
    }
}
