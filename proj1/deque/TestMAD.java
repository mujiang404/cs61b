package deque;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import static org.junit.Assert.*;

public class TestMAD {

    @Test
    public void testMAD() {
        Comparator<Integer> comp = new IntegerComparator();
        MaxArrayDeque<Integer> maxArrayDeque = new MaxArrayDeque<>(comp);

        maxArrayDeque.addLast(10);
        maxArrayDeque.addLast(24);
        maxArrayDeque.addLast(5);


        assertEquals((Integer) 24, maxArrayDeque.max());
    }
}
