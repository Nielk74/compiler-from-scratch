package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * @author gl10
 * @date 25/04/2022
 */
public class UnaryMinus extends AbstractUnaryExpr {

    public UnaryMinus(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type typeOperand = getOperand().verifyExpr(compiler, localEnv, currentClass);
        if (!typeOperand.isFloat() && !typeOperand.isInt()){
            throw new ContextualError("Unary minus cannot be applied to a value other than float or int ", getLocation());
        }
        setType(typeOperand);
        return typeOperand;
    }


    @Override
    protected String getOperatorName() {
        return "-";
    }

    @Override
    protected void codeGenExp(DecacCompiler compiler, int register_name) {
        Minus minusObject;
        if (getOperand().getType().isInt()){
            minusObject= new Minus(new IntLiteral(0), getOperand());
        }
        else {
            minusObject= new Minus(new FloatLiteral(0), getOperand());
        }
        
        minusObject.codeGenExp(compiler, register_name);
    }

}
