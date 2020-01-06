package Assignment3;

/**
 *
 * @author Khanh Tran
 */
import java.util.Comparator;

public class LinkedHeapPriorityQueue<K,V> extends AbstractPriorityQueue<K,V> {
    
    //---------------- nested Node class ----------------
    protected static class Node<E> implements Position<E> {
        private E element;
        private Node<E> parent;
        private Node<E> left;
        private Node<E> right;
        
        public Node(E e, Node<E> above, Node<E> leftChild, Node<E> rightChild) {
            element = e;
            parent = above;
            left = leftChild;
            right = rightChild;
        }
        
       //accessor methods
        public E getElement( ) { return element; }
        public Node<E> getParent( ) { return parent; }
        public Node<E> getLeft( ) { return left; }
        public Node<E> getRight( ) { return right; }

        //update methods
        public void setElement(E e) { element = e; }
        public void setParent(Node<E> parentNode) { parent = parentNode; }
        public void setLeft(Node<E> leftChild) { left = leftChild; }
        public void setRight(Node<E> rightChild) { right = rightChild; }
    }
    //----------- end of nested Node class -----------
    
    /** Factory function to create a new node storing element e. */
    protected Node<Entry<K,V>> createNode(
                    Entry<K,V> e, 
                    Node<Entry<K,V>> parent, 
                    Node<Entry<K,V>> left, 
                    Node<Entry<K,V>> right) {
        return new Node<Entry<K,V>> (e, parent, left, right);
    }
    
    protected Node<Entry<K,V>> root = null;
    private int size = 0;
    
    //constructors
    public LinkedHeapPriorityQueue() { super(); }
    public LinkedHeapPriorityQueue(Comparator<K> comp) { super(comp); }
    
    /**
     * Checks if a position is valid and returns node representation
     * @param p  the position to be validated
     * @return node returned
     * @throws IllegalArgumentException if position is not valid
     */
    protected Node<Entry<K,V>> validate(Position<Entry<K,V>> p) 
                                throws IllegalArgumentException {
        if (!(p instanceof Node))
            throw new IllegalArgumentException("Not valid position type");
        Node<Entry<K,V>> node = (Node<Entry<K,V>>) p;
        if (node.getParent() == node)
            throw new IllegalArgumentException("p is no longer in the tree");
        return node;
    }
    
    /**
     * 
     * @return size of heap
     */
    public int size() {
        return size;
    }
    
    /**
     * 
     * @return the root of the heap
     */
    //public Position<PQEntry<K,V>> root() {
        //return root;
    //}
    
    /**
     * Gets the parent of the given position
     * @param p  the given position
     * @return the position of parent
     * @throws IllegalArgumentException if position is not valid
     */
    protected Position<Entry<K,V>> parent(Position<Entry<K,V>> p) throws
                                                IllegalArgumentException {
        Node<Entry<K,V>> node = validate(p);
        return node.getParent();
    }
    
    /**
     * Gets the left child of the given position
     * @param p  the given position
     * @return the position of the left child
     * @throws IllegalArgumentException if position is not valid
     */
    protected Position<Entry<K,V>> left(Position<Entry<K,V>> p) throws
                                                IllegalArgumentException {
        Node<Entry<K,V>> node = validate(p);
        return node.getLeft();
    }
    
    /**
     * Gets the right child of the given position
     * @param p  the given position
     * @return the position of the right child
     * @throws IllegalArgumentException if position is not valid
     */
    protected Position<Entry<K,V>> right(Position<Entry<K,V>> p) throws
                                                IllegalArgumentException {
        Node<Entry<K,V>> node = validate(p);
        return node.getRight();
    }
    
    /**
     * Checks if the given position has a left child
     * @param j  the given position
     * @return true if child exists
     */
    protected boolean hasLeft(Position<Entry<K,V>> j) {
        return left(j) != null;
    }
    
    /**
     * Checks if given position has a right child
     * @param j  the given position
     * @return true if child exists
     */
    protected boolean hasRight(Position<Entry<K,V>> j) {
        return right(j) != null;
    }
    
    /**
     * Exchanges the entries at position i and j
     * @param i  first position
     * @param j  second position
     */
    protected void swap(Position<Entry<K,V>> i, Position<Entry<K,V>> j) {
        Entry<K,V> temp = i.getElement();
        Node<Entry<K,V>> node = validate(i);
        node.setElement(j.getElement());
        Node<Entry<K,V>> node2 = validate(j);
        node2.setElement(temp);
    }
    
    /**
     * Moves the entry at position j higher, if necessary, to maintain heap
     * @param j  the position to start at
     */
    protected void upheap(Position<Entry<K,V>> j) {
        while (validate(j)!=root) {
            Position<Entry<K,V>> p = parent(j);
            if (compare(j.getElement(),p.getElement()) >= 0) break;
            swap(j,p);
            j=p;
        }
    }
    
    /**
     * Moves the entry at position j lower, if necessary, to maintain heap
     * @param j  the position to start at
     */
    protected void downheap(Position<Entry<K,V>> j) {
        while (hasLeft(j)) {
            Position<Entry<K,V>> leftPosition = left(j);
            Position<Entry<K,V>> smallChildPosition = leftPosition;
            if (hasRight(j)) {
                Position<Entry<K,V>> rightPosition = right(j);
                if (compare(leftPosition.getElement(),rightPosition.getElement())
                                                      > 0)
                    smallChildPosition = rightPosition;
            }
            if (compare(smallChildPosition.getElement(),j.getElement()) >=0)
                break;
            swap(j, smallChildPosition);
            j=smallChildPosition;
        }
    }
    
    /**
     * Returns (but does not remove) an entry with minimal key (if any)
     * @return the entry with minimal key
     */
    public Entry<K,V> min() {
        if (isEmpty()) return null;
        return root.getElement();
    }
    
    /**
     * Inserts a key-value pair and returns the entry created.
     * @param key     the key of the new entry
     * @param value   the associated value of the new entry
     * @return the entry storing the new key-value pair
     * @throws IllegalArgumentException if key is not valid
     */
    public Entry<K,V> insert(K key, V value) throws IllegalArgumentException {
        checkKey(key);
        Entry<K,V> newest = new PQEntry<>(key, value);
        Node<Entry<K,V>> current = root;
        String path = Integer.toBinaryString(size+1);
        if (size == 0) {
            root = createNode(newest, null, null, null);
            size = 1;
            return root.getElement();
        }
        else if (size == 1) {
            Node<Entry<K,V>> child = createNode(newest, root, null, null);
            root.setLeft(child);
            size++;
            upheap(child);
            return child.getElement();
        }
        else if (size == 2) {
            Node<Entry<K,V>> child = createNode(newest, root, null, null);
            root.setRight(child);
            size++;
            upheap(child);
            return child.getElement();
        }
        else {
            for (int i = 1; i < path.length()-1; i++) {
                if (path.charAt(i) == '0') {
                    current = current.getLeft();
                }
                else if (path.charAt(i) == '1') {
                    current = current.getRight();
                }   
            }
            Node<Entry<K,V>> child = createNode(newest, current, null, null);
            if (path.charAt(path.length()-1) == '0') {
                current.setLeft(child);
            }
            else if (path.charAt(path.length()-1) ==  '1') {
                current.setRight(child);
            }
            size++;
            upheap(child);
            return child.getElement();
        }
    }
    
    /**
     * Removes and returns an entry with minimal key.
     * @return the removed entry (or null if empty)
     */
    public Entry<K,V> removeMin() {
        if (size == 0) return null;
        Node<Entry<K,V>> current = root;
        String path = Integer.toBinaryString(size);
        String leftOrRightChild = null;
        if (size == 1) {
            Entry<K,V> answer = root.getElement();
            root.setElement(null);
            root = null;
            size--;
            return answer;
        }
        else {
            for (int i = 1; i < path.length(); i++) {
                if (path.charAt(i) == '0') {
                    current = current.getLeft();
                    leftOrRightChild = "left";
                }
                else if ( path.charAt(i) == '1') {
                    current.getRight();
                    leftOrRightChild = "right";
                }
            }
            size--;
            Entry<K,V> answer = root.getElement();
            swap(root, current);
            Node<Entry<K,V>> parent = current.getParent();
            if (leftOrRightChild.equals("left"))
                parent.setLeft(null);
            else if (leftOrRightChild.equals("right"))
                parent.setRight(null);
            current.setElement(null);
            current.setParent(current);
            downheap(root);
            return answer;
        }
    }
    
    public static void main(String[] args) {
        //Airline standby simulation
        LinkedHeapPriorityQueue heap = new LinkedHeapPriorityQueue();
        heap.insert(4, "Joe");
        heap.insert(5, "Maria");
        heap.insert(3, "Sally");
        heap.insert(1, "Bob");
        //Who is at the front of the priority queue?
        System.out.println(heap.min().getValue() + " is at the front");
        //Let the person with top priority board
        System.out.println(heap.removeMin().getValue() + " boarded the plane");
        //Who is at the front of the priorty queue?
        System.out.println(heap.min().getValue() + " is at the front");
        //Let the next top priority board (program encounters an error here)
        //System.out.println(heap.removeMin().getValue() + " boarded the plane");
    }
}