/**
 * ClassName: Palindrome
 * Package: PACKAGE_NAME
 * Description:
 *
 * @Author xyz
 * @Create 2023/10/17 17:15
 * @Version 1.0
 */
public class Palindrome {

    public Deque<Character> wordToDeque(String word) {
        LinkedListDeque<Character> deque = new LinkedListDeque<>();
        for (int i = 0; i < word.length(); i++) {
            deque.addLast(word.charAt(i));
        }
        return deque;
    }

    private boolean isPalindromeHelper(Deque<Character> deque) {
        if (deque.size() == 1 || deque.size() == 0) {
            return true;
        }

        if (deque.removeFirst().equals(deque.removeLast())) {
            return isPalindromeHelper(deque);
        } else {
            return false;
        }
    }
    public boolean isPalindrome(String word) {
        Deque<Character> deque = wordToDeque(word);
        return isPalindromeHelper(deque);
    }

    private boolean isPalindromeOffByOne(Deque<Character> deque, CharacterComparator cc) {
        if (deque.size() == 1 || deque.size() == 0) {
            return true;
        }

        if (cc.equalChars(deque.removeFirst(), deque.removeLast())) {
            return isPalindromeOffByOne(deque, cc);
        } else {
            return false;
        }
    }

    public boolean isPalindrome(String word, CharacterComparator cc) {
        Deque<Character> deque = wordToDeque(word);
        return isPalindromeOffByOne(deque, cc);
    }

}
