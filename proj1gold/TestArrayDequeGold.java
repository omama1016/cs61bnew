/**
 * ClassName: TestArrayDequeGold
 * Package: PACKAGE_NAME
 * Description:
 *
 * @Author xyz
 * @Create 2023/10/18 7:08
 * @Version 1.0
 */
import static org.junit.Assert.*;
import org.junit.Test;


public class TestArrayDequeGold {
    @Test
    public void testAddLastFirstRemove() {

        ArrayDequeSolution<Integer> good = new ArrayDequeSolution<>();
        StudentArrayDeque<Integer> bad = new StudentArrayDeque<>();
        String s = "";

        while (true) {
            double number = StdRandom.uniform();
            if (number < 0.25) {
                Integer num = StdRandom.uniform(100);
                //System.out.println("addFirst(" + num + ")");
                s += "\naddFirst(" + num + ")";
                good.addFirst(num);
                bad.addFirst(num);

                Integer exp = good.get(0);
                Integer acu = bad.get(0);

                assertEquals(s, exp, acu);
            } else if (number < 0.5) {
                Integer num = StdRandom.uniform(100);
                //System.out.println("addLast(" + num + ")");
                s += "\naddLast(" + num + ")";
                good.addLast(num);
                bad.addLast(num);
                Integer exp = good.get(good.size()-1);
                Integer acu = bad.get(bad.size()-1);
                assertEquals(s, exp, acu);
            } else if (number < 0.75) {
                s += "\nremoveLast()";
                if (!good.isEmpty() && !bad.isEmpty()) {
                    Integer exp = good.removeLast();
                    Integer stu = bad.removeLast();
                    assertEquals(s, exp, stu);
                }
            } else {
                s += "\nremoveFirst()";
                if (!good.isEmpty() && !bad.isEmpty()) {
                    Integer exp = good.removeFirst();
                    Integer stu = bad.removeFirst();
                    assertEquals(s, exp, stu);
                }
            }
        }
    }
}