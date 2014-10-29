/**
 * The interface for making Homework assignments gradable.
 *
 * The data structure typically doesn't require the functions defined
 *     in this interface, but they allow for easier grading,
 *    therefore, you are responsible for making your deliverables
 *     implement gradable
 *
 * @version 1.0
 *
 */
public interface Gradable<T extends Comparable<T>> {
    /**
     * Returns the root of the tree
     * DO NOT USE THIS METHOD IN YOUR CODE! IT IS STRICTLY FOR TESTING PURPOSES
     * Should run in O(1)
     * @return the root of the tree
     */
    public Node<T> getRoot();
}