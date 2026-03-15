package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
    T[] items;
    int length;
    int size;
    int front;
    int rear;

    @Override
    public int size() {
        return size;
    }

    public ArrayDeque() {
        items = (T[])new Object[8];
        length = 8;
        size = 0;
        front = 3;
        rear = 4;


    }

    public ArrayDeque(int length) {
        items = (T[])new Object[length];
        this.length = length;
        size = 0;
        front = 0;
        rear = 1;
    }



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
        Deque<T> d = (Deque<T>) o;
        for (int i = 0; i < size; i++) {
            if (!(get(i).equals(d.get(i)))) {
                return false;
            }

        }
        return true;
    }

    public void addFirst(T item) {
        if (size == length) {
            resize(length * 2);
        }

        items[front] = item;
        size++;
        front = (front - 1 + length) % length;
    }

    public void addLast(T item) {
        if (size == length) {
            resize(length * 2);
        }
        items[rear] = item;
        rear = (rear + 1) % length;
        size++;
    }

    public T removeFirst() {
        if ((float)length / (size - 1) > 4 && length > 8) {
            resize(length / 2);
        }
        if (size == 0) {
            return null;
        }
        T item = items[(front + 1) % length];
        size--;
        front = (front + 1) % length;
        return item;
    }

    public T removeLast() {
        if ((float)length / (size - 1) > 4 && length > 8) {
            resize(length / 2);
        }
        if (size == 0) {
            return null;
        }
        T item = items[(rear - 1 + length) % length];
        size--;
        rear = (rear - 1 + length) % length;
        return item;
    }

    private void resize(int newSize) {
        T[] newitems = (T[])new Object[newSize];
        int newfront = newSize / 2 - 1;
        int newrear = newSize / 2;
        int currentpos = newrear;

        for (int i = 1; i <= size; i++) {
            newitems[currentpos] = get(i);
            currentpos = (currentpos + 1) % newSize;
        }

        length = newSize;
        items = newitems;
        front = newfront;
        rear = currentpos;

    }

    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        return items[(front + index + 1) % length];
    }

    public class adIterator implements Iterator<T> {
        int index = 0;
        public boolean hasNext() {
            return index < size;
        }

        public T next() {

            return  get(index++);

        }
    }

    public Iterator<T> iterator() {
        return new adIterator();
    }


}
