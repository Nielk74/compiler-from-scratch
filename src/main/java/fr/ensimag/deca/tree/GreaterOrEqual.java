package fr.ensimag.deca.tree;


/**
 * Operator "x >= y"
 * 
 * @author gl10
 * @date 25/04/2022
 */
public class GreaterOrEqual extends AbstractOpIneq {

    public GreaterOrEqual(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return ">=";
    }

}
