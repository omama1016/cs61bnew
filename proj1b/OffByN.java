/**
 * ClassName: OffByN
 * Package: PACKAGE_NAME
 * Description:
 *
 * @Author xyz
 * @Create 2023/10/17 22:04
 * @Version 1.0
 */
public class OffByN implements CharacterComparator{

    private int N;

    public OffByN(int N) {
        this.N = N;
    }

    @Override
    public boolean equalChars(char x, char y) {
        return Math.abs(x - y) == N;
    }
}
