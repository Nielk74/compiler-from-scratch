package fr.ensimag.deca.tree;

/**
 * Abstract class to factorize the StringLiteral code.
 *
 * @author gl10
 * 
 */
public abstract class AbstractStringLiteral extends AbstractExpr {

    /**
     * Return the string of the string literal.
     */
    public abstract String getValue();
    
}
