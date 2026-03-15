package deque;

import java.util.Comparator;
import java.util.Iterator;


public class MaxArrayDeque<T> implements Deque<T>, Iterable<T> {
    private T[] items;
    private int length;
    private int size;
    private int front;
    private int rear;

    private Comparator<T> comp;

    public MaxArrayDeque(Comparator<T> c) {
        items = (T[]) new Object[8];
        length = 8;
        size = 0;
        front = 0;
        rear = 1;

        comp = c;
    }


    public T max() {
        if (size == 0) {
            return null;
        }
        T maxValue = get(0);
        for (int i = 0; i < size; i++) {
            T ii  = get(i);
            int isBig = comp.compare(ii, maxValue);
            if (isBig > 0) {
                maxValue = ii;
            }
        }
        return maxValue;
    }

    public T max(Comparator<T> c) {
        if (size == 0) {
            return null;
        }
        T maxValue = get(0);
        for (int i = 0; i < size; i++) {
            T ii  = get(i);
            int isBig = c.compare(ii, maxValue);
            if (isBig > 0) {
                maxValue = ii;
            }
        }
        return maxValue;

    }


    public int size() {
        return size;
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

        if (o == null) {
            return false;
        }

        if (!(o instanceof Deque)) {
            return false;
        }

        Deque<T> d = (Deque<T>) o;

        if (d.size() != size) {return false;}

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
        if ((float) length / (size - 1) > 4 && length > 8) {
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
        if ((float) length / (size - 1) > 4 && length > 8) {
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

    public void resize(int newSize) {
        T[] newitems = (T[]) new Object[newSize];
        int newfront = 0;
        int newrear = 1;
        int currentpos = newrear;

        for (int i = 0; i < size; i++) {
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


    private class MADIterator implements Iterator<T> {
        int index = 0;
        public boolean hasNext() {
            return index < size;
        }

        public T next() {

            return  get(index++);

        }
    }

    public Iterator<T> iterator() {
        return new MaxArrayDeque.MADIterator();
    }


}
