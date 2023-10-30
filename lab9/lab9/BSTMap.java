package lab9;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Implementation of interface Map61B with BST as core data structure.
 *
 * @author Your name here
 */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private class Node {
        /* (K, V) pair stored in this Node. */
        private K key;
        private V value;

        /* Children of this Node. */
        private Node left;
        private Node right;

        private Node parent;

        private Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    private Node root;  /* Root node of the tree. */
    private int size; /* The number of key-value pairs in the tree */

    /* Creates an empty BSTMap. */
    public BSTMap() {
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /** Returns the value mapped to by KEY in the subtree rooted in P.
     *  or null if this map contains no mapping for the key.
     */
    private V getHelper(K key, Node p) {
        if (p == null) {
            return null;
        } else if (p.key.compareTo(key) == 0) {
            return p.value;
        } else if (p.key.compareTo(key) < 0) {
            return getHelper(key, p.right);
        } else {
            return getHelper(key, p.left);
        }
    }

    /** Returns the value to which the specified key is mapped, or null if this
     *  map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        return getHelper(key, root);
    }

    /** Returns a BSTMap rooted in p with (KEY, VALUE) added as a key-value mapping.
      * Or if p is null, it returns a one node BSTMap containing (KEY, VALUE).
     */
    private Node putHelper(K key, V value, Node p) {
        if (p == null) return new Node(key, value);
        else if (p.key.compareTo(key) == 0) {
            p.value = value;
        } else if (p.key.compareTo(key) < 0) {
            Node node = putHelper(key, value, p.right);
            p.right = node;
            node.parent = p;
        } else {
            Node node = putHelper(key, value, p.left);
            p.left = node;
            node.parent = p;
        }
        return p;
    }

    /** Inserts the key KEY
     *  If it is already present, updates value to be VALUE.
     */
    @Override
    public void put(K key, V value) {
        Node node = putHelper(key, value, root);
        root = node;
        node.parent = root;
        size++;
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    /* Returns a Set view of the keys contained in this map. */
    private void keySetHelper(Set<K> set, Node p) {
        if (p != null) {
            set.add(p.key);
            keySetHelper(set, p.left);
            keySetHelper(set, p.right);
        }
    }

    @Override
    public Set<K> keySet() {
       Set<K> set = new HashSet<>();
       keySetHelper(set, root);
       return set;
    }

    /** Removes KEY from the tree if present
     *  returns VALUE removed,
     *  null on failed removal.
     */

    private Node find(K key, Node p) {
        if (p == null) return null;
        else if (p.key.compareTo(key) == 0) return p;
        else if (p.key.compareTo(key) < 0) return find(key, p.right);
        else return find(key, p.left);
    }

    private Node findSuccessor(Node p) {
        if (p == null || p.right == null) return null;
        Node curP = p.right;
        while (curP.left != null) {
            curP = curP.left;
        }
        return curP;
    }

    private boolean isLeaf(Node p) {
        return p.left == null && p.right == null;
    }

    @Override
    public V remove(K key) {
        Node keyNode = find(key, root);
        V res = keyNode.value;

        // delete leaf node
        if (keyNode.left == null && keyNode.right == null) {
            if (keyNode.parent.left == keyNode) {
                keyNode.parent.left = null; // delete the left child
            } else {
                keyNode.parent.right = null; // delete the right child
            }
        } else if (keyNode.left == null) {
            if (keyNode.parent.left == keyNode) {
                keyNode.parent.left = keyNode.right; // delete the left child
            } else {
                keyNode.parent.right = keyNode.right; // delete the right child
                keyNode.right.parent = keyNode.parent;
            }
        } else if (keyNode.right == null) {
            if (keyNode.parent.left == keyNode) {
                keyNode.parent.left = keyNode.left; // delete the left child
            } else {
                keyNode.parent.right = keyNode.left; // delete the right child
                keyNode.left.parent = keyNode.parent;
            }
        } else {
            Node successor = findSuccessor(keyNode);
            if (isLeaf(successor)) {
                keyNode.key = successor.key;
                keyNode.value = successor.value;
                if (successor.parent.left == successor) {
                    successor.parent.left = null;
                }
                else {
                    successor.parent.right = null;
                }
            } else {
                successor.right.parent = successor.parent;
                successor.parent.right = successor.right;
            }
        }

        return res;
    }

    /** Removes the key-value entry for the specified key only if it is
     *  currently mapped to the specified value.  Returns the VALUE removed,
     *  null on failed removal.
     **/
    @Override
    public V remove(K key, V value) {
        Node node = find(key, root);
        if (node.value.equals(value)) {
            return remove(key);
        }
        return null;
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }
}
