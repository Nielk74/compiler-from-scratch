package fr.ensimag.deca.tree;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.Validate;

/**
 * Tree of list, which is inherited by all the list.
 *
 * @author gl10
 * 
 */
public abstract class TreeList<TreeType extends Tree> extends Tree {
    /**
     * We could allow external iterators by adding
     * "implements Iterable<AbstractInst>" but it's cleaner to provide our own
     * iterators, to make sure all callers iterate the same way (Main,
     * IfThenElse, While, ...). If external iteration is needed, use getList().
     */

     /**
      * The list of tree.
      */
    private List<TreeType> list = new ArrayList<TreeType>();

    /**
     * Add the tree to the list.
     * 
     * @param i The tree to add to the list.
     */
    public void add(TreeType i) {
        Validate.notNull(i);
        list.add(i);
    }

    /**
     * Return the list contained in the class, read-only. Use getModifiableList()
     * if you need to change elements of the list.
     * @return List<TreeType>
     */
    public List<TreeType> getList() {
        return Collections.unmodifiableList(list);
    }

    /**
     * Set the tree to the specified index, and return the element previously at this position.
     * 
     * @param index Index where to add the tree.
     * @param element The tree to add. 
     * @return TreeType
     */
    public TreeType set(int index, TreeType element) {
        return list.set(index, element);
    }

    /**
     * Return true if the list is empty, otherwise false.
     */
    public boolean isEmpty() {
        return list.isEmpty();
    }

    /**
     * Returns a list iterator over the elements in the list.
     * 
     * @return Iterator<TreeType>
     */
    public Iterator<TreeType> iterator() {
        return list.iterator();
    }

    /**
     * Return the size of the list.
     */
    public int size() {
        return list.size();
    }

    /**
     * Do not check anything about the location.
     * 
     * It is possible to use setLocation() on a list, but it is also OK not to
     * set it.
     */
    @Override
    protected void checkLocation() {
        // nothing
    }

    /**
     * @see fr.ensimag.deca.tree.Tree#prettyPrintNode
     */
    @Override
    protected String prettyPrintNode() {
        return super.prettyPrintNode() +
                " [List with " + getList().size() + " elements]";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        int count = getList().size();
        for (TreeType i : getList()) {
            i.prettyPrint(s, prefix, count == 1, true);
            count--;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void iterChildren(TreeFunction f) {
        for (TreeType i : getList()) {
            i.iter(f);
        }
    }

}
