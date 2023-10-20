package synthesizer;

/**
 * ClassName: AbstractBoundedQueue
 * Package: synthesizer
 * Description:
 *
 * @Author xyz
 * @Create 2023/10/20 14:55
 * @Version 1.0
 */
public abstract class AbstractBoundedQueue<T> implements BoundedQueue<T>{
    protected int fillCount;
    protected int capacity;

    public int capacity() {
        return capacity;
    }
    public int fillCount() {
        return fillCount;
    }
}
