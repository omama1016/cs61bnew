/**
 * ClassName: LinkedListDeque
 * Package: PACKAGE_NAME
 * Description:
 *
 * @Author xyz
 * @Create 2023/10/16 20:42
 * @Version 1.0
 */
public class LinkedListDeque<T> implements Deque<T>{

    private class Node {
        public T item;
        public Node next;
        public Node prev;
        public  Node(T item) {
            this.item = item;
        }

        public  Node() {
        }
    }

    private Node sentinel;
    private int size;

    public LinkedListDeque() {
        sentinel = new Node();
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
    }

    private void addWhenEmpty(Node node) {
        sentinel.next = node;
        sentinel.prev = node;
        node.prev = sentinel;
        node.next = sentinel;
    }

    @Override
    public void addFirst(T item) {
        Node node = new Node(item);
        if (isEmpty()) {
            addWhenEmpty(node);
        } else {
            node.next = sentinel.next;
            sentinel.next.prev = node;
            node.prev = sentinel;
            sentinel.next = node;
        }
        size += 1;
    }

    @Override
    public void addLast(T item) {
        Node node = new Node(item);
        if (size == 0) {
            addWhenEmpty(node);
        } else {
            node.prev = sentinel.prev;
            sentinel.prev = node;
            node.prev.next = node;
            node.next = sentinel;
        }
        size ++;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        Node p = sentinel.next;
        while (p != sentinel) {
            System.out.print(p.item + " ");
            p = p.next;
        }
        System.out.println();
    }

    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        size --;
        T res = sentinel.next.item;
        sentinel.next = sentinel.next.next;
        sentinel.next.prev = sentinel;
        return res;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        size --;
        T res = sentinel.prev.item;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;
        return res;
    }

    @Override
    public T get(int index) {
        if (isEmpty() || index > size - 1) {
            return null;
        }
        int count = 0;
        Node p = sentinel.next;
        while (count < index) {
            p = p.next;
            count ++;
        }
        return p.item;
    }

    private T getRecursiveHelper(Node node, int index) {
        if (index == 0) return node.item;
        return getRecursiveHelper(node.next, --index);
    }

    public T getRecursive(int index) {
        if (isEmpty() || index > size - 1) {
            return null;
        }
        return getRecursiveHelper(sentinel.next, index);
    }

}
