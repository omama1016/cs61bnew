import java.util.NoSuchElementException;

/**
 * ClassName: Deque
 * Package: PACKAGE_NAME
 * Description:
 *
 * @Author xyz
 * @Create 2023/10/17 17:05
 * @Version 1.0
 */
public interface Deque<Item> {
    public void printDeque();
    public void addFirst(Item item);
    public void addLast(Item item);
    //public Item getRecursive(int i);
    public Item get(int i);
    public Item removeFirst();
    public Item removeLast();
    public int size();
    public boolean isEmpty();
}
