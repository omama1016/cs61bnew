package synthesizer;
import org.junit.Test;

import javax.swing.text.html.HTMLDocument;

import java.util.Iterator;

import static org.junit.Assert.*;

/** Tests the ArrayRingBuffer class.
 *  @author Josh Hug
 */

public class TestArrayRingBuffer {
    @Test
    public void someTest() {
        ArrayRingBuffer<Integer> arb = new ArrayRingBuffer(10);
        arb.enqueue(10);
        arb.enqueue(8);
        arb.enqueue(7);
        arb.enqueue(6);
        arb.enqueue(5);
        arb.enqueue(4);
        arb.enqueue(3);

        Iterator<Integer> iterator = arb.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }

//        System.out.println(arb.fillCount());
//        System.out.println(arb.dequeue());
//        System.out.println(arb.dequeue());
//        System.out.println(arb.dequeue());
//        System.out.println(arb.dequeue());
//        System.out.println(arb.peek());
//        System.out.println(arb.peek());
//        System.out.println(arb.dequeue());
//        System.out.println(arb.dequeue());
//        System.out.println(arb.dequeue());
//        System.out.println(arb.dequeue());
    }



    /** Calls tests for ArrayRingBuffer. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestArrayRingBuffer.class);
    }
} 
