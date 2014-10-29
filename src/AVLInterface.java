import java.util.List;

/**
 * The interface the for an AVL Tree
 * @version 1.0
 */
public interface AVLInterface<T extends Comparable<T>> {
    /**
     * Add the data as a leaf in the BST.  Should traverse the tree to find the
     * appropriate location.  You may assume null is never passed in.
     * Should have a worst case running time of O(logn)
     * If the data is already in the tree, do nothing.
     * @param data the data to be added
     * @throws IllegalArgumentException if {@code data} is null
     */
    void add(T data);

    /**
     * Removes the data from the tree.  There are 3 cases to consider:
     * 1: the data is a leaf.  In this case, simply remove it
     * 2: the data has one child.  In this case, simply replace it with its
     * child, then remove the child.
     * 3: the data has 2 children.  There are generally two approaches:
     * replacing the data with either the next smallest element in the tree
     * (commonly called the predecessor), or replacing it with the next largest
     * element in the tree (commonly called the successor). For this
     * assignment, use the successor.
     * Should have a worst case running time of O(logn)
     * @param data
     * @return the data removed from the tree.
     * @throws IllegalArgumentException if {@code data} is null
     */
    T remove(T data);

    /**
     * Returns the data in the tree matching the parameter passed in (think
     * carefully: should you use .equals or == ?).
     * Should have a worst case running time of O(logn)
     * @param data
     * @return the data in the tree equal to the parameter
     * @throws IllegalArgumentException if {@code data} is null
     */
    T get(T data);

    /**
     * Returns whether or not the parameter is contained within the tree.
     * Should have a worst case running time of O(logn)
     * @param data
     * @return whether or not the parameter is contained within the tree
     * @throws IllegalArgumentException if {@code data} is null
     */
    boolean contains(T data);

    /**
     * Checks if the tree is empty or not
     * Should run in O(1)
     * @return true if this tree is empty, false otherwise
     */
    boolean isEmpty();

    /**
     * Should run in O(1)
     * @return the number of elements in the tree
     */
    int size();

    /**
     * Should run in O(n)
     * @return a preorder traversal of the tree
     */
    List<T> preorder();

    /**
     * Should run in O(n)
     * @return a postorder traversal of the tree
     */
    List<T> postorder();

    /**
     * Should run in O(n)
     * @return an inorder traversal of the tree
     */
    List<T> inorder();

    /**
     * Should run in O(n)
     * @return a level order traversal of the tree
     */
    List<T> levelorder();

    /**
     * Clear the tree.  Should be O(1)
     */
    void clear();

    /**
     * Calculate and return the height of the root of the tree. A node's height
     * is defined as max(left.height, right.height) + 1. A leaf node has a
     * height of 0. An empty tree should return -1.
     * Should be calculated in O(1)
     * @return the height of the root of the tree
     */
    int height();
}
