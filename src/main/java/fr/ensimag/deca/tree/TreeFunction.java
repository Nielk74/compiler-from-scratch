package fr.ensimag.deca.tree;

/**
 * Function that takes a tree as argument.
 * 
 * @see fr.ensimag.deca.tree.Tree#iter(TreeFunction)
 * 
 * @author gl10
 * @date 25/04/2022
 */
public interface TreeFunction {
    void apply(Tree t);
}
