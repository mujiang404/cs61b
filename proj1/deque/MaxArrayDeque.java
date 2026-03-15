package deque;

import java.util.Comparator;
import java.util.Iterator;

public class MaxArrayDeque<T> implements Deque<T>, Iterable<T> {
    T[] items;
    int length;
    int size;
    int front;
    int rear;

    Comparator<T> comp;

    public MaxArrayDeque(Comparator<T> c) {
        items = (T[])new Object[8];
        length = 8;
        size = 0;
        front = 3;
        rear = 4;

        comp = c;
    }


    public T max() {
        if (size == 0) {
            return null;
        }
        T maxValue = get(1);
        for (T i : this) {
            int isBig = comp.compare(i, maxValue);
            if (isBig > 0) {
                maxValue = i;
            }
        }
        return maxValue;
    }

    public T max(Comparator<T> c) {
        if (size == 0) {
            return null;
        }
        T maxValue = get(1);
        for (T i : this) {
            int isBig = c.compare(i, maxValue);
            if (isBig > 0) {
                maxValue = i;
            }
        }
        return maxValue;

    }

    @Override
    public int size() {
        return size;
    }

    public MaxArrayDeque() {
        items = (T[])new Object[8];
        length = 8;
        size = 0;
        front = 3;
        rear = 4;


    }



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
            currentpos = (currentpos + 1) % 8;
        }

        length = newSize;
        items = newitems;
        front = newfront;
        rear = currentpos;

    }

    public T get(int index) {
        if (index < 0 || index > size) {
            return null;
        }


        return items[(front + index) % length];
    }

    public class madIterator implements Iterator<T> {
        int index = 1;
        public boolean hasNext() {
            return index <= size;
        }

        public T next() {

            return  get(index++);

        }
    }

    public Iterator<T> iterator() {
        return new MaxArrayDeque.madIterator();
    }
}
