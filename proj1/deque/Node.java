package deque;

public class Node<T> {
    T value;
    Node<T> next;
    Node<T> previous;

    public Node() {
        value = null;
        next = this;
        previous = this;
    }

    public Node(T value) {
        this.value = value;
        next = null;
        previous = null;
    }
}
