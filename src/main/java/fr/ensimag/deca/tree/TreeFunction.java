package fr.ensimag.deca.tree;

/**
 * Function that takes a tree as argument.
 * 
 * @see fr.ensimag.deca.tree.Tree#iter(TreeFunction)
 * 
 * @author gl10
 * 
 */
public interface TreeFunction {
    /**
     * Apply a methode to all the nodes of the tree.
     * 
     * @param t The tree to whom we apply.
     */
    void apply(Tree t);
}