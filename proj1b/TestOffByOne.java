import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByOne {

    // You must use this CharacterComparator and not instantiate
    // new ones, or the autograder might be upset.
    static CharacterComparator offByOne = new OffByOne();

    // Your tests go here.
    //Uncomment this class once you've created your CharacterComparator interface and OffByOne class.

    @Test
    public void testEqualOffByOne() {
        assertTrue(offByOne.equalChars('a', 'b'));
        assertTrue(offByOne.equalChars('b', 'a'));

        assertTrue(offByOne.equalChars('r', 'q'));
        assertTrue(offByOne.equalChars('q', 'r'));
        assertTrue(offByOne.equalChars('&', '%'));

        assertFalse(offByOne.equalChars('a', 'a'));
        assertFalse(offByOne.equalChars('a', 'c'));
        assertFalse(offByOne.equalChars('a', 'A'));


    }

}
