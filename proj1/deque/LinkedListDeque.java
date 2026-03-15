package deque;


import java.util.Iterator;

public class LinkedListDeque<T> implements Iterable<T>, Deque<T> {
    private int size;
    private Node<T> sentinel;



    public void printDeque() {
        for (int i = 0; i < size; i++) {
            System.out.print(get(i));
            if (i == size) {
                break;
            }
            System.out.print(" ");
        }
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (!(o instanceof Deque)) {
            return false;
        }

        Deque<T> d = (Deque<T>) o;

        if (d.size() != size) {
            return false;
        }

        for (int i = 0; i < size; i++) {
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


        if  (index >= size &&  index < 0) {
            return null;
        }
        Node<T> currentNode = sentinel;
        for (int i = index; i >= 0; i--) {
            currentNode = currentNode.next;
        }
        return currentNode.value;


    }

    private T getRecursiveHelper(int index, Node<T> currentnode, boolean order) {



        if (index <= 0) {
            return currentnode.value;
        } else if (order) {
            return getRecursiveHelper(index - 1, currentnode.next, order);

        } else  {
            return getRecursiveHelper(index - 1, currentnode.previous, order);
        }
    }

    public T getRecursive(int index) {
        if  (index >= size &&  index < 0) {
            return null;
        }
        boolean order = size - index > index + 1;
        index = order ? index + 1 : size - index;

        return getRecursiveHelper(index, sentinel, order);
    }

    private class LLDequeIterator implements Iterator<T> {
        int index = 0;
        public boolean hasNext() {
            return index < size;
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
