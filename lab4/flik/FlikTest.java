package flik;

import org.junit.Test;
import edu.princeton.cs.algs4.StdRandom;
import static org.junit.Assert.assertTrue;

public class FlikTest {
    @Test
    public void fliktest1() {
        System.out.println(11);

        for (int i = 1; i <= 100; i++) {
            int operationNumber = StdRandom.uniform(0, 1000000);
            assertTrue(Flik.isSameNumber(operationNumber, operationNumber));

        }
    }

}
