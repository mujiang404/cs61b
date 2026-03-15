package deque;

import org.junit.Test;

public class LLDTesst {
    @Test
    public void test() {
        LinkedListDeque<Integer> linkedListDeque = new LinkedListDeque<>();
        linkedListDeque.addLast(1);
        linkedListDeque.addLast(2);
        linkedListDeque.addLast(3);

    }


    @Test
    public void test1() {
        ArrayDeque<Integer> linkedListDeque = new ArrayDeque<>();
        linkedListDeque.addLast(1);
        linkedListDeque.addLast(2);
        linkedListDeque.addLast(3);

        System.out.println(linkedListDeque.get(3));
    }
}
