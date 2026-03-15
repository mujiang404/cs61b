package deque;


import java.util.Iterator;

public class LinkedListDeque<T> implements Iterable<T>, Deque<T> {
    int size;
    Node<T> sentinel;



    public void printDeque() {
        for (int i = 1; i <= size; i++) {
            System.out.print(get(i));
            if (i == size) {
                break;
            }
            System.out.print(" ");
        }
    }

    public boolean equals(Object o) {
        Deque<T> d = (Deque<T>) o;
        for (int i = 1; i <= size; i++) {
            if (!(get(i).equals(d.get(i)))) {
                return false;
            }
        }
        return true;
    }

    public LinkedListDeque() {
        size = 0;
        sentinel = new Node<T>();
    }

    public void addFirst(T value) {
        Node<T> node = new Node<T>(value);
        sentinel.next.previous = node;
        node.next = sentinel.next;
        node.previous = sentinel;
        sentinel.next = node;
        size++;
    }

    public void addLast(T value) {
        Node<T> node = new Node<T>(value);
        sentinel.previous.next = node;
        node.previous = sentinel.previous;
        sentinel.previous = node;
        node.next = sentinel;
        size++;



    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        Node<T> node = sentinel.next;
        sentinel.next = node.next;
        node.next.previous = sentinel;
        size--;
        return node.value;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }
        Node<T> node = sentinel.previous;

        sentinel.previous = node.previous;
        node.previous.next = sentinel;

        size--;
        return node.value;
    }

    public int size() {
        return size;
    }

    public T get(int index) {


        if  (index > size) {
            return null;
        }
        Node<T> currentNode = sentinel;
        for (int i = index; i > 0; i--) {
            currentNode = currentNode.next;
        }
        return currentNode.value;


    }

    private T getRecursiveHelper(int index, Node<T> currentnode) {


        index = size + 1 - index > index ? index : size + 1 - index;
        if (index == 0) {
            return currentnode.value;
        } else {
            return getRecursiveHelper(index - 1, currentnode.next);
        }
    }

    public T getRecursive(int index) {
        if  (index > size) {
            return null;
        }

        return getRecursiveHelper(index, sentinel);
    }

    public class LLDequeIterator implements Iterator<T> {
        int index = 1;
        public boolean hasNext() {
            return index <= size;
        }
        public T next() {
            return get(index++);
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public Iterator<T> iterator() {
        return new LLDequeIterator();
    }



}
