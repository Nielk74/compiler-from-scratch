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
 * classes can be added with declareClass().
 *
 * @author gl10
 * @date 25/04/2022
 */
public class EnvironmentType {
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

        Symbol stringSymb = compiler.createSymbol("string");
        STRING = new StringType(stringSymb);
        
        Symbol nullSymbol = compiler.createSymbol("null");
        NULL = new NullType(nullSymbol);

        Symbol objectSymb = compiler.createSymbol("Object");
        OBJECT = new ClassType(objectSymb, Location.BUILTIN, null);
        envTypes.put(objectSymb, OBJECT.getDefinition());

        // TODO need to move
        Symbol equalSymb = compiler.createSymbol("equals");
        Signature equalsSignature = new Signature();
        equalsSignature.add(OBJECT);
        EQUALS_DEF = new MethodDefinition(BOOLEAN, Location.BUILTIN, equalsSignature, 0);
        // need to put into labelManager
        Label label = new Label("code.Object.equals");
        EQUALS_DEF.setLabel(label);
        try {
            OBJECT.getDefinition().getMembers().declare(equalSymb, EQUALS_DEF);
        } catch (DoubleDefException e) {
            // should not happen
        }
    }

    private final Map<Symbol, TypeDefinition> envTypes;

    public TypeDefinition defOfType(Symbol s) {
        return envTypes.get(s);
    }

    public ClassType addClassType(DecacCompiler compiler, Symbol className, ClassDefinition superDef,
            Location location) {
        ClassType classType = new ClassType(className, location, superDef);
        envTypes.put(className, classType.getDefinition());
        return classType;
    }

    public final VoidType VOID;
    public final IntType INT;
    public final FloatType FLOAT;
    public final StringType STRING;
    public final BooleanType BOOLEAN;
    public final ClassType OBJECT;
    public final NullType NULL;
    public final MethodDefinition EQUALS_DEF;
}
