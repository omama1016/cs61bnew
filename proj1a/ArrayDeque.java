import sun.management.snmp.jvmmib.JvmThreadInstanceTableMeta;

/**
 * ClassName: ArrayDeque
 * Package: PACKAGE_NAME
 * Description:
 *
 * @Author xyz
 * @Create 2023/10/16 20:42
 * @Version 1.0
 */
public class ArrayDeque <T>{
    private T[] items;

    private int first;
    private int last;
    private int size;
    private final double USAGE_FACTOR = 0.25;

    public ArrayDeque() {
        items = (T[]) new Object[8];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    private void decFirst() {
        if (first - 1 < 0)
            first = first - 1 + items.length;
        else {
            first = first - 1;
        }
    }
    public void addFirst(T item) {

        if (!isEmpty()) {
            if (size >= items.length)
                resize(items);
            decFirst();
        }
        items[first] = item;
        size++;
    }

    public void resize(T[] a) {

        int re_lenght = 0;

        if (size == items.length) {
            re_lenght = items.length * 2;
        }else {
            re_lenght = items.length / 2;
        }

        T[] copys = (T[]) new Object[re_lenght];

        for (int i = 0; i < size; i++) {
            copys[i] = items[first];
            first = (first + 1) % items.length;
        }
        items = copys;
        first = 0;
        if (isEmpty())
            last = 0;
        else
            last = size- 1;
    }

    public T removeFirst() {

        if (isEmpty()) {
            return null;
        }

        size--;
        T res = items[first];
        items[first] = null;
        first = (first + 1) % items.length;

        if ((size * 1.0 / items.length) < USAGE_FACTOR) {
            resize(items);
        }
        return res;
    }

    public void printDeque() {
        int j = first;
        for (int i = 0; i < size; i++) {
            System.out.print(items[j] + " ");
            j = (j + 1) % items.length;
        }
    }


    public void addLast(T item) {
        if (!isEmpty()) {
            if (size >= items.length)
                resize(items);
            last = (last + 1) % items.length;
        }
        items[last] = item;
        size++;
    }


    public T removeLast() {

        if (isEmpty()) {
            return null;
        }

        size--;
        T res = items[last];
        items[last] = null;
        last = (last - 1);
        if (last < 0) last = last + items.length;
        if ((size * 1.0 / items.length) < USAGE_FACTOR) {
            resize(items);
        }
        return res;
    }


    public T get(int index) {
        if (isEmpty()) return null;
        return items[(first + index) % items.length];
    }
}
