package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import net.sf.saxon.om.Item;
import org.junit.Test;
import timingtest.AList;

import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
  // YOUR TESTS HER
//    @Test
//    public void testThreeAddThreeRemove() {
//        AListNoResizing<Integer> anr = new AListNoResizing<Integer>();
//        BuggyAList<Integer> ba =  new BuggyAList<Integer>();
//        anr.addLast(5);
//        anr.addLast(10);
//        anr.addLast(15);
//        ba.addLast(5);
//        ba.addLast(10);
//        ba.addLast(15);
//
//        int a = anr.removeLast();
//        int b = ba.removeLast();
//        assertEquals(a,b);
//
//        a = anr.removeLast();
//        b = ba.removeLast();
//        assertEquals(a,b);
//
//        a = anr.removeLast();
//        b = ba.removeLast();
//        assertEquals(a,b);
//    }

    @Test
    public void testThreeAddThreeRemove() {
        AListNoResizing<Integer> correct = new AListNoResizing<>();
        BuggyAList<Integer> broken = new BuggyAList<>();

        correct.addLast(5);
        correct.addLast(10);
        correct.addLast(15);

        broken.addLast(5);
        broken.addLast(10);
        broken.addLast(15);

        assertEquals(correct.size(), broken.size());

        assertEquals(correct.removeLast(), broken.removeLast());
        assertEquals(correct.removeLast(), broken.removeLast());
        assertEquals(correct.removeLast(), broken.removeLast());
    }
    @Test
    public void randomizedtest() {
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> broken = new BuggyAList<>();

        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);

            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);

                broken.addLast(randVal);

                System.out.println("addLast(" + randVal + ")");
                System.out.println("addLast(" + randVal + ")");

            } else if (operationNumber == 1) {
                // size
                int size = L.size();
                System.out.println("size: " + size);

                int buggysize = broken.size();
                System.out.println("buggysize: " + buggysize);

            } else if (operationNumber == 2 && L.size() > 0) {
                // size
                int rl = L.removeLast();
                System.out.println("rmlast: " + rl);

                int buggysize = broken.removeLast();
                System.out.println("buggyremove: " + buggysize);

            } else if (operationNumber == 3 && L.size() > 0) {
                // size
                int getlastt = L.getLast();
                System.out.println("geilastt: " + getlastt);

                int buggysize = broken.size();
                System.out.println("buggylast: " + buggysize);
            }
        }
    }


}
