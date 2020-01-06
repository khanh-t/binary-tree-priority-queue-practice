package Assignment3;

/**
 *
 * @author Khanh Tran
 */
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Scanner;

/** Concrete implementation of a binary tree using a node-based, linked structure. */
public class LinkedBinaryTree<E> extends AbstractBinaryTree<E> {
    //---------------- nested Node class ----------------
    protected static class Node<E> implements Position<E> {
        private E element;
        private Node<E>  parent;
        private Node<E> left;
        private Node<E> right;
        
        public Node(E e, Node<E> above, Node<E> leftChild, Node<E> rightChild) {
            element = e;
            parent = above;
            left = leftChild;
            right = rightChild;
        }
        // accessor methods
        public E getElement() { return element; }
        public Node<E> getParent() { return parent; }
        public Node<E> getLeft()  { return left; }
        public Node<E> getRight()    { return right; }
        // update methods
        public void setElement(E e) { element = e; }
        public void setParent(Node<E> parentNode) { parent = parentNode; }
        public void setLeft(Node<E> leftChild) { left = leftChild; }
        public void setRight(Node<E> rightChild) { right = rightChild; }
        }//----------- end of nested Node class -----------
    
    /** Factory function to create a new node storing element e. */
    protected Node<E> createNode(E e, Node<E> parent, Node<E> left, Node<E> right) {
        return new Node<E> (e, parent, left, right);
    }
    
    // LinkedBinaryTree instance variables
    protected Node<E> root = null;
    private int size = 0;
    
    public LinkedBinaryTree() {}
    
    /** 
     * Validates the position and returns it as a node. 
     * @param p  a position of the list
     * @return the position as node
     * @throws IllegalArgumentException if position is not valid
     */
    protected Node<E> validate(Position<E> p) throws IllegalArgumentException {
        if (!(p instanceof Node))
            throw new IllegalArgumentException("Not valid position type");
        Node<E> node = (Node<E>) p;
        if (node.getParent() == node)
            throw new IllegalArgumentException("p is no longer in the tree");
        return node;
    }
    
    /**
     * @return the size of linked binary tree
     */
    public int size() {
        return size;
    }
    
    /**
     * @return the root of the tree
     */
    public Position<E> root() {
        return root;
    }
    
    /**
     * Get the parent of given position
     * @param p  the position given
     * @return the position of the parent
     * @throws IllegalArgumentException if position is not valid
     */
    public Position<E> parent(Position<E> p) throws IllegalArgumentException {
        Node<E> node = validate(p);
        return node.getParent();
    }
    
    /**
     * Get the left child of given position
     * @param p  the position given
     * @return the position of the left child
     * @throws IllegalArgumentException if position is not valid
     */
    public Position<E> left(Position<E> p) throws IllegalArgumentException {
        Node<E> node = validate(p);
        return node.getLeft();
    }
    
    /**
     * Get the right child of given position
     * @param p  the position given
     * @return the position of right child
     * @throws IllegalArgumentException if position is not valid
     */
    public Position<E> right(Position<E> p) throws IllegalArgumentException {
        Node<E> node = validate(p);
        return node.getRight();
    }
    
    /**
     * Add a new root position
     * @param e  the element assigned to position
     * @return the position of root
     * @throws IllegalStateException 
     */
    public Position<E> addRoot(E e) throws IllegalStateException {
        if (!isEmpty()) throw new IllegalStateException("Tree is not empty");
        root = createNode(e, null, null, null);
        size = 1;
        return root;
    }
    
    /**
     * Add left child to given position
     * @param p  position to add child to
     * @param e  element assigned to child
     * @return the position of new child
     * @throws IllegalStateException 
     */
    public Position<E> addLeft(Position<E> p, E e) throws IllegalStateException {
        Node<E> parent = validate(p);
        if (parent.getLeft() !=null)
            throw new IllegalArgumentException("p already has a left child");
        Node<E> child = createNode(e, parent, null, null);
        parent.setLeft(child);
        size++;
        return child;
    }
    
    /**
     * Add right child to given position
     * @param p  position to add child to
     * @param e  element assigned to child
     * @return the position of new child
     * @throws IllegalStateException 
     */
    public Position<E> addRight(Position<E> p, E e) throws IllegalStateException {
        Node<E> parent = validate(p);
        if (parent.getRight() !=null)
            throw new IllegalArgumentException("p already has a right child");
        Node<E> child = createNode(e, parent, null, null);
        parent.setRight(child);
        size++;
        return child;
    }
    
    /**
     * Replace element at given position
     * @param p  position to be affected
     * @param e  the new element
     * @return the old element
     * @throws IllegalArgumentException if position is not valid
     */
    public E set(Position<E> p, E e) throws IllegalArgumentException {
        Node<E> node = validate(p);
        E temp = node.getElement();
        node.setElement(e);
        return temp;
    }
    
    /**
     * Attach subtrees to given position
     * @param p   position to be affected
     * @param t1  the left subtree to be attached
     * @param t2  the right subtree to be attached
     * @throws IllegalArgumentException if position is not valid
     */
    public void attach(Position<E> p, LinkedBinaryTree<E> t1,
                        LinkedBinaryTree<E> t2) throws IllegalArgumentException {
        Node<E> node = validate(p);
        if (isInternal(p)) throw new IllegalArgumentException("p must be a leaf");
        size += t1.size() + t2.size();
        if (!t1.isEmpty()) {
            t1.root.setParent(node);
            node.setLeft(t1.root);
            t2.root = null; 
            t2.size = 0;
        }
    }
    
    /**
     * Remove the given position
     * @param p  position to be removed
     * @return the the element of removed position
     * @throws IllegalArgumentException if position is not valid
     */
    public E remove(Position<E> p) throws IllegalArgumentException {
        Node<E> node = validate(p);
        if (numChildren(p) == 2)
            throw new IllegalArgumentException("p has two children");
        Node<E> child = (node.getLeft() != null ? node.getLeft() : node.getRight());
        if (child != null)
            child.setParent(node.getParent());
        if (node == root)
            root = child;
        else {
            Node<E> parent = node.getParent();
            if (node == parent.getLeft())
                parent.setLeft(child);
            else
                parent.setRight(child);
        }
        size--;
        E temp = node.getElement();
        node.setElement(null);
        node.setLeft(null);
        node.setRight(null);
        node.setParent(node);
        return temp;
    }
    
    //The following are used to create an iterator
    private class ElementIterator implements Iterator<E> {
        Iterator<Position<E>> posIterator = positions().iterator();
        public boolean hasNext() { return posIterator.hasNext(); }
        public E next() { return posIterator.next().getElement(); }
        public void remove() { posIterator.remove(); }
    }
    
    public Iterator<E> iterator() { return new ElementIterator(); }
   
    public Iterable<Position<E>> positions() { return preorder(); }
    
    private void preorderSubtree(Position<E> p, List<Position<E>> snapshot) {
        snapshot.add(p);
        for (Position<E> c: children(p))
            preorderSubtree(c, snapshot);
    }
    
    public Iterable<Position<E>> preorder() {
        List<Position<E>> snapshot = new ArrayList<>();
        if (!isEmpty())
            preorderSubtree(root(), snapshot);
        return snapshot;
    }
    
    public static void main(String[] args) {
        LinkedBinaryTree decisions = new LinkedBinaryTree();
        Position root = decisions.addRoot(new String("Do you have a dog?"));
        Position firstYes = decisions.addLeft(root, new String(
                                              "We can be friends"));
        Position firstNo = decisions.addRight(root, new String(
                                              "Are you allergic to dogs?"));
        Position secondYes = decisions.addLeft(firstNo, new String(
                                              "Sorry for your loss"));
        Position secondNo = decisions.addRight(firstNo, new String(
                                              "Do you want a dog?"));
        Position thirdYes = decisions.addLeft(secondNo, new String(
                                              "Pug life!"));
        Position thirdNo = decisions.addRight(secondNo, new String(
                                              "This conversation is over"));
        
        Scanner kb = new Scanner(System.in);
        
        System.out.println("Please answer 'Yes' or 'No' to the following.");
        System.out.println(root.getElement());
        
        if (kb.next().equals("Yes")) {
            System.out.println(firstYes.getElement());
        }
        else {
            System.out.println(firstNo.getElement());
            if (kb.next().equals("Yes")) {
                System.out.println(secondYes.getElement());
            }
            else {
                System.out.println(secondNo.getElement());
                if (kb.next().equals("Yes")) {
                    System.out.println(thirdYes.getElement());
                }
                else {
                    System.out.println(thirdNo.getElement());
                }
            }
        }
    }
}
