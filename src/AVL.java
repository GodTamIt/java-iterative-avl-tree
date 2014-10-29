import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

/**
 * A completely iterative AVL implementation (excluding certain order
 * traversals).
 *
 * @author Christopher Tam
 */
public class AVL<T extends Comparable<T>> implements AVLInterface<T> {

    // Do not add additional instance variables
    private Node<T> root = null;
    private int size = 0;

    /**
     * The type of child in a binary tree.
     */
    private enum ChildType {
        LEFT, RIGHT
    }

    @Override
    public void add(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Argument cannot be null!");
        } else if (this.size == 0) {
            this.root = new Node<T>(data);
        } else {
            // Calculate maximum parents to visit
            int worstCase = this.size / 2 + 1;

            // Parallel arrays tracking node ancestors
            @SuppressWarnings("unchecked")
            Node<T>[] parents = new Node[worstCase];
            ChildType[] childTypes = new ChildType[worstCase];

            int parentCount = 0;
            Node<T> current = root;

            // Search for location to add array
            while (current != null) {
                int comp = data.compareTo(current.getData());
                if (comp == 0) {
                    return; // Value already exists
                } else {
                    parents[parentCount] = current;
                    if (comp < 0) {
                        current = current.getLeft();
                        childTypes[parentCount] = ChildType.LEFT;
                    } else {
                        current = current.getRight();
                        childTypes[parentCount] = ChildType.RIGHT;
                    }
                    parentCount++;
                }
            }

            // Add node at location found
            current = parents[parentCount - 1];
            if (childTypes[parentCount - 1] == ChildType.LEFT) {
                current.setLeft(new Node<T>(data));
            } else {
                current.setRight(new Node<T>(data));
            }

            // Iterate back through parents, updating fields and balancing
            for (int i = parentCount - 1; i > 0; i--) {
                current = parents[i];
                this.updateFactorAndHeight(current);
                this.rebalance(current, parents[i - 1], childTypes[i - 1]);
            }

            // Edge case (root)
            this.updateFactorAndHeight(this.root);
            this.rebalance(this.root, null, null);
        }
        this.size++;
    }

    private void updateFactorAndHeight(Node<T> target) {
        // Get children
        Node<T> left = target.getLeft();
        Node<T> right = target.getRight();

        // Calculate balance factor [height(left) - height(right)]
        int numLeft = (left == null ? -1 : left.getHeight());
        int numRight = (right == null ? -1 : right.getHeight());
        target.setBalanceFactor(numLeft - numRight);

        // Calculate height
        numLeft = (left == null ? -1 : left.getHeight());
        numRight = (right == null ? -1 : right.getHeight());
        target.setHeight(Math.max(numLeft, numRight) + 1);
    }

    private void rebalance(Node<T> target, Node<T> parent, ChildType type) {
        int balanceFactor = target.getBalanceFactor();

        if (balanceFactor > 1) {
            // ~~Subtree is LEFT heavy~~

            if (target.getLeft().getBalanceFactor() < 0) {
                // Left rotation of left child required before right rotation
                // (L-R)
                this.rotateLeft(target.getLeft(), target, ChildType.LEFT);
            }

            // Right rotation
            this.rotateRight(target, parent, type);
        } else if (balanceFactor < -1) {
            // ~~Subtree is RIGHT heavy

            if (target.getRight().getBalanceFactor() > 0) {
                // Right rotation of right child required before left rotation
                // (R-L)
                rotateRight(target.getRight(), target, ChildType.RIGHT);
            }

            // Left rotation
            this.rotateLeft(target, parent, type);
        }
    }

    /**
     * Rotates a given node to the right or clockwise and then sets the new root
     * node as the child of a given parent.
     *
     * @param target
     *            the node to rotate.
     * @param parent
     *            the parent of <code>target</code>, the node to rotate.
     * @param type
     *            the child type of <code>target</code> in relation to
     *            <code>parent</code>.
     */
    private void rotateRight(Node<T> target, Node<T> parent, ChildType type) {
        // Get the node to pivot around
        Node<T> pivot = target.getLeft();

        // Ensure nothing stupid happens
        if (pivot == null) {
            return;
        }

        // Right rotation
        target.setLeft(pivot.getRight());
        pivot.setRight(target);

        // Update parent node connection
        if (parent == null) {
            this.root = pivot;
        } else if (type == ChildType.LEFT) {
            parent.setLeft(pivot);
        } else {
            parent.setRight(pivot);
        }

        // Update 'target' then 'pivot' (order important)
        this.updateFactorAndHeight(target);
        this.updateFactorAndHeight(pivot);
    }

    /**
     * Rotates a given node to the left or counterclockwise and then sets the
     * new root node as the child of a given parent.
     *
     * @param target
     *            the node to rotate.
     * @param parent
     *            the parent of <code>target</code>, the node to rotate.
     * @param type
     *            the child type of <code>target</code> in relation to
     *            <code>parent</code>.
     */
    private void rotateLeft(Node<T> target, Node<T> parent, ChildType type) {
        Node<T> pivot = target.getRight();

        // Ensure nothing stupid happens
        if (pivot == null) {
            return;
        }

        // Left rotation
        target.setRight(pivot.getLeft());
        pivot.setLeft(target);

        // Update parent node connection
        if (parent == null) {
            this.root = pivot;
        } else if (type == ChildType.LEFT) {
            parent.setLeft(pivot);
        } else {
            parent.setRight(pivot);
        }

        // Update 'target' then 'pivot' (order important)
        this.updateFactorAndHeight(target);
        this.updateFactorAndHeight(pivot);
    }

    @Override
    public T remove(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Argument cannot be null!");
        } else if (this.isEmpty()) {
            return null;
        }

        // Calculate maximum parents to visit
        int worstCase = this.size / 2 + 1;

        // Parallel arrays tracking node ancestors
        @SuppressWarnings("unchecked")
        Node<T>[] parents = new Node[worstCase];
        ChildType[] childTypes = new ChildType[worstCase];

        int parentCount = 0;
        Node<T> current = root;
        int comp;

        do {
            comp = data.compareTo(current.getData());
            parents[parentCount] = current;
            if (comp < 0) {
                current = current.getLeft();
                childTypes[parentCount] = ChildType.LEFT;
            } else {
                current = current.getRight();
                childTypes[parentCount] = ChildType.RIGHT;
            }
            parentCount++;
        } while (current != null && comp != 0);

        if (current == null) {
            // Node does not exist
            return null;
        }

        // Make sure to get result from node
        T result = current.getData();

        // Get children of deleted node
        Node<T> left = current.getLeft();
        Node<T> right = current.getRight();

        // Handle the 3 distinct delete cases
        if (left == null && right == null) {
            // CASE: no children

            if (parentCount > 0) {
                // Node to delete is a leaf
                Node<T> parent = parents[parentCount - 1];
                if (childTypes[parentCount - 1] == ChildType.LEFT) {
                    parent.setLeft(null);
                } else {
                    parent.setRight(null);
                }
            } else {
                // The node to delete is the root
                this.root = null;
            }
        } else if (left == null || right == null) {
            // CASE: one child

            // Retrieve the lone child
            Node<T> loneChild = (left == null ? right : left);

            if (parentCount > 1) {
                Node<T> parent = parents[parentCount - 1];
                if (childTypes[parentCount - 2] == ChildType.LEFT) {
                    parent.setLeft(loneChild);
                } else {
                    parent.setRight(loneChild);
                }
            } else {
                this.root = loneChild;
            }
        } else {
            // CASE: two children

            // Find successor
            Node<T> successor = right;
            Node<T> successorParent = current;
            ChildType type = ChildType.RIGHT;
            while (successor.getLeft() != null) {
                successorParent = successor;
                parents[parentCount++] = successorParent;
                successor = successor.getLeft();
                type = ChildType.LEFT;
            }

            // Replace deleted data with successor data
            current.setData(successor.getData());

            // Trim off successor
            if (type == ChildType.RIGHT) {
                successorParent.setRight(null);
            } else {
                successorParent.setLeft(null);
            }
        }

        // Update fields and rebalance if necessary
        for (int i = parentCount - 1; i > 0; i--) {
            current = parents[i];
            this.updateFactorAndHeight(current);
            this.rebalance(current, parents[i - 1], childTypes[i - 1]);
        }

        // Edge-case rebalance
        if (parentCount > 0) {
            this.updateFactorAndHeight(this.root);
            this.rebalance(this.root, null, null);
        }

        // Decrement size
        size--;

        return result;
    }

    @Override
    public T get(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Argument cannot be null.");
        }
        Node<T> current = root;
        while (current != null) {
            int comp = data.compareTo(current.getData());
            if (comp == 0) {
                return current.getData();
            } else if (comp < 0) {
                current = current.getLeft();
            } else {
                current = current.getRight();
            }
        }
        return null;
    }

    @Override
    public boolean contains(T data) {
        return this.get(data) != null;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0 && this.root == null;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public List<T> preorder() {
        List<T> results = new ArrayList<T>(size);
        if (root != null) {
            this.preorder(results, root);
        }
        return results;
    }

    /**
     * Recursively builds preorder list from the current node.
     *
     * @param existing
     *            the existing list to add to.
     * @param current
     *            the node to start preorder from.
     */
    private void preorder(List<T> existing, Node<T> current) {
        existing.add(current.getData());
        if (current.getLeft() != null) {
            this.preorder(existing, current.getLeft());
        }
        if (current.getRight() != null) {
            this.preorder(existing, current.getRight());
        }
    }

    @Override
    public List<T> postorder() {
        List<T> results = new ArrayList<T>(size);
        if (root != null) {
            this.postorder(results, root);
        }
        return results;
    }

    /**
     * Recursively builds postorder list from the current node.
     *
     * @param existing
     *            the existing list to add to.
     * @param current
     *            the node to start postorder from.
     */
    private void postorder(List<T> existing, Node<T> current) {
        if (current.getLeft() != null) {
            this.postorder(existing, current.getLeft());
        }
        if (current.getRight() != null) {
            this.postorder(existing, current.getRight());
        }
        existing.add(current.getData());
    }

    @Override
    public List<T> inorder() {
        List<T> results = new ArrayList<T>(size);
        if (root != null) {
            this.inorder(results, root);
        }
        return results;
    }

    /**
     * Recursively builds inorder list from the current node.
     *
     * @param existing
     *            the existing list to add to.
     * @param current
     *            the node to start inorder from.
     */
    private void inorder(List<T> existing, Node<T> current) {
        if (current.getLeft() != null) {
            this.inorder(existing, current.getLeft());
        }
        existing.add(current.getData());
        if (current.getRight() != null) {
            this.inorder(existing, current.getRight());
        }
    }

    @Override
    public List<T> levelorder() {
        List<T> results = new ArrayList<T>(this.size);
        LinkedList<Node<T>> nodes = new LinkedList<Node<T>>();
        nodes.add(this.root);
        Node<T> current;
        while (!nodes.isEmpty()) {
            current = nodes.remove();
            results.add(current.getData());
            if (current.getLeft() != null) {
                nodes.add(current.getLeft());
            }
            if (current.getRight() != null) {
                nodes.add(current.getRight());
            }
        }
        return results;
    }

    @Override
    public void clear() {
        this.root = null;
        this.size = 0;
    }

    @Override
    public int height() {
        return (this.isEmpty() ? -1 : this.root.getHeight());
    }
	
	@Override
    public String toString() {
        StringBuilder build = new StringBuilder(this.size * 3);
        LinkedList<Node<T>> nodes = new LinkedList<Node<T>>();
        nodes.add(this.root);
        Node<T> current;
        while (!nodes.isEmpty()) {
            current = nodes.remove();
            if (current == null) {
                build.append("\r\n");
            } else {
                build.append(current.getData().toString() + " ");
                if (!nodes.contains(null)) {
                    nodes.add(null);
                }
                if (current.getLeft() != null) {
                    nodes.add(current.getLeft());
                }
                if (current.getRight() != null) {
                    nodes.add(current.getRight());
                }
            }
        }
        return build.toString();
    }
}