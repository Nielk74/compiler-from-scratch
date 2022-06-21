package fr.ensimag.deca.context;

import java.util.HashMap;
import java.util.Map;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.Location;
import fr.ensimag.ima.pseudocode.Label;

/**
 * Environment containing types. Initially contains predefined identifiers, more
 * classes can be added with addClassType().
 *
 * @author gl10
 * 
 */
public class EnvironmentType {
    /** 
     * @param compiler
     */
    public EnvironmentType(DecacCompiler compiler) {

        envTypes = new HashMap<Symbol, TypeDefinition>();

        Symbol intSymb = compiler.createSymbol("int");
        INT = new IntType(intSymb);
        envTypes.put(intSymb, new TypeDefinition(INT, Location.BUILTIN));

        Symbol floatSymb = compiler.createSymbol("float");
        FLOAT = new FloatType(floatSymb);
        envTypes.put(floatSymb, new TypeDefinition(FLOAT, Location.BUILTIN));

        Symbol voidSymb = compiler.createSymbol("void");
        VOID = new VoidType(voidSymb);
        envTypes.put(voidSymb, new TypeDefinition(VOID, Location.BUILTIN));

        Symbol booleanSymb = compiler.createSymbol("boolean");
        BOOLEAN = new BooleanType(booleanSymb);
        envTypes.put(booleanSymb, new TypeDefinition(BOOLEAN, Location.BUILTIN));
        
        Symbol objectSymb = compiler.createSymbol("Object");
        OBJECT = new ClassType(objectSymb, Location.BUILTIN, null);
        envTypes.put(objectSymb, OBJECT.getDefinition());

        // not added to envTypes, it's not visible for the user.
        Symbol stringSymb = compiler.createSymbol("string");
        STRING = new StringType(stringSymb);
        
        Symbol nullSymbol = compiler.createSymbol("null");
        NULL = new NullType(nullSymbol);

        // to complete Object's definition with its method
        declareObjectMethod(compiler);
    }

    /** 
     * @param envTypes Data structure representing the types of an environment (association name -> TypeDefinition).
     */
    private final Map<Symbol, TypeDefinition> envTypes;

    
    /** 
     * Return the TypeDefinition corresponding to the parameter
     * @param s
     * @return TypeDefinition
     */
    public TypeDefinition defOfType(Symbol s) {
        return envTypes.get(s);
    }
    
    /** 
     * Return the classType added to the hashmap
     * @param compiler
     * @param className name of the class
     * @param superDef Definition of the super class
     * @param location Location of the declaration in the code
     * @return ClassType
     */
    public ClassType addClassType(DecacCompiler compiler, Symbol className, ClassDefinition superDef,
            Location location) {
        ClassType classType = new ClassType(className, location, superDef);
        envTypes.put(className, classType.getDefinition());
        return classType;
    }

    /**
     * Declare the method equals of Object
     * @param compiler
     */
    private void declareObjectMethod(DecacCompiler compiler) {
        Symbol equalSymb = compiler.createSymbol("equals");
        Signature equalsSignature = new Signature();
        equalsSignature.add(OBJECT);
        MethodDefinition equalDef = new MethodDefinition(BOOLEAN, Location.BUILTIN, equalsSignature, 0);
        Label label = compiler.labelManager.createLabel("code.Object.equals");
        equalDef.setLabel(label);
        try {
            OBJECT.getDefinition().getMembers().declare(equalSymb, equalDef);
        } catch (DoubleDefException e) {
            // should not happen as this is the first method declared
        }

    }

    public final VoidType VOID;
    public final IntType INT;
    public final FloatType FLOAT;
    public final StringType STRING;
    public final BooleanType BOOLEAN;
    public final ClassType OBJECT;
    public final NullType NULL;
}
