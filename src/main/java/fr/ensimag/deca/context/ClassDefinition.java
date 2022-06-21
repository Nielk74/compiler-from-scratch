package fr.ensimag.deca.context;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.tree.Location;
import fr.ensimag.ima.pseudocode.DAddr;

/**
 * Definition of a class.
 *
 * @author gl10
 * 
 */
public class ClassDefinition extends TypeDefinition {

    /** 
     * Set the operand.
     * @param operand address of the class in method table 
     */
    public void setOperand(DAddr operand) {
        this.operand = operand;
    }

    
    /** 
     * Return the operand.
     * @return DAddr
     */
    public DAddr getOperand() {
        return operand;
    }
    
    /** 
     * @param operand Address of the class in method table.
     */
    private DAddr operand;
    
    
    /** 
     * Set the number of fields of the class.
     * @param numberOfFields
     */
    public void setNumberOfFields(int numberOfFields) {
        this.numberOfFields = numberOfFields;
    }

    
    /** 
     * Return the number of fields of the class.
     * @return int
     */
    public int getNumberOfFields() {
        return numberOfFields;
    }

    /** 
     * Increments the number of fields.
     */
    public void incNumberOfFields() {
        this.numberOfFields++;
    }

    
    /** 
     * Return the number of methods.
     * @return int
     */
    public int getNumberOfMethods() {
        return numberOfMethods;
    }

    
    /** 
     * Set the number of methods of the class.
     * @param n
     */
    public void setNumberOfMethods(int n) {
        Validate.isTrue(n >= 0);
        numberOfMethods = n;
    }
    
    
    /** 
     * Increments the number of methods.
     * @return int
     */
    public int incNumberOfMethods() {
        numberOfMethods++;
        return numberOfMethods;
    }

    /** 
     * @param numberOfFields Number of fields of the class.
     */
    private int numberOfFields = 0;
    /** 
     * @param numberOfMethods Number of methods of the class.
     */
    private int numberOfMethods = 0;
    
    
    /** 
     * {@inheritDoc}
     */
    @Override
    public boolean isClass() {
        return true;
    }
    
    
    /** 
     * {@inheritDoc}
     */
    @Override
    public ClassType getType() {
        // Cast succeeds by construction because the type has been correctly set
        // in the constructor.
        return (ClassType) super.getType();
    };

    
    /** 
     * Return the super class.
     * @return ClassDefinition
     */
    public ClassDefinition getSuperClass() {
        return superClass;
    }

    /** 
     * @param members Fields and methods of the class.
     */
    private final EnvironmentExp members;
    /** 
     * @param superClass The super class of the class.
     */
    private final ClassDefinition superClass; 

    
    /** 
     * Return the fields ans methods of the class.
     * @return EnvironmentExp
     */
    public EnvironmentExp getMembers() {
        return members;
    }

    /** 
     * @param type Type of the class.
     * @param location Location in the code of the declaration.
     * @param superClass the super class of the class.
     */
    public ClassDefinition(ClassType type, Location location, ClassDefinition superClass) {
        super(type, location);
        EnvironmentExp parent;
        if (superClass != null) {
            parent = superClass.getMembers();
        } else {
            parent = null;
        }
        members = new EnvironmentExp(parent);
        this.superClass = superClass;

    }
    
}
