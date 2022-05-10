package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;

/**
 * Assignment, i.e. lvalue = expr.
 *
 * @author gl10
 * @date 25/04/2022
 */
public class Assign extends AbstractBinaryExpr {

    @Override
    public AbstractLValue getLeftOperand() {
        // The cast succeeds by construction, as the leftOperand has been set
        // as an AbstractLValue by the constructor.
        return (AbstractLValue)super.getLeftOperand();
    }

    public Assign(AbstractLValue leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        ExpDefinition leftDef = localEnv.get(((AbstractIdentifier) this.getLeftOperand()).getName());
        if (leftDef == null)
            throw new ContextualError("Lvalue " + ((AbstractIdentifier) this.getLeftOperand()).getName().getName() + " does not exist", this.getLeftOperand().getLocation());
        
        ((AbstractIdentifier) this.getLeftOperand()).setDefinition(leftDef);
        Type leftType = leftDef.getType();
        this.getRightOperand().verifyRValue(compiler, localEnv, currentClass, leftType);
        return leftType;
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        if (getRightOperand().getType().isInt()) {
            // getRightOperand n'est pas forcément un IntLiteral (cas d'une affectation de variable à variable)
            IntLiteral intLiteral = (IntLiteral) getRightOperand();
            compiler.addInstruction(new LOAD(new ImmediateInteger(intLiteral.getValue()), Register.getR(2)));
            //Ne pas oublier de décaler le offset en fonction des autres
            RegisterOffset offset = new RegisterOffset(1, Register.LB);
            compiler.addInstruction(new STORE(Register.getR(2), offset));
            ((AbstractIdentifier) this.getLeftOperand()).getExpDefinition().setOperand(offset);
        }
    }

    @Override
    protected String getOperatorName() {
        return "=";
    }

}
