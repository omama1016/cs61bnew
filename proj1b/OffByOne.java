/**
 * ClassName: OffByOne
 * Package: PACKAGE_NAME
 * Description:
 *
 * @Author xyz
 * @Create 2023/10/17 21:41
 * @Version 1.0
 */
public class OffByOne implements CharacterComparator{
    @Override
    public boolean equalChars(char x, char y) {
        return Math.abs(x - y) == 1;
    }
}
