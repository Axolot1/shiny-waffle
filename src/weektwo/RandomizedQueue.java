package weektwo;

import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] a;
    private int N;

    public RandomizedQueue() {
        a = (Item[]) new Object[2];
        N = 0;
    }

    public boolean isEmpty() {
        return N == 0;
    }

    public int size() {
        return N;
    }

    private void resize(int capacity) {
        assert capacity >= N;
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < N; i++) {
            temp[i] = a[i];
        }
        a = temp;
    }

    public void enqueue(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }
        if (N == a.length) {
            resize(N * 2);
        }
        a[N++] = item;
    }

    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        int index = StdRandom.uniform(N);
        swap(index, N - 1);
        Item item = a[--N];
        a[N] = null;

        if (N > 0 && N == a.length / 4) {
            resize(a.length / 2);
        }
        return item;
    }

    private void swap(int index, int i) {
        Item t = a[index];
        a[index] = a[i];
        a[i] = t;
    }

    // return (but do not remove) a random item
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return a[StdRandom.uniform(N)];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomIterator(N, a);
    }

    private class RandomIterator implements Iterator<Item> {

        private int len;
        private Item[] b;

        RandomIterator(int len, Item[] a) {
            this.len = len;
            b = (Item[]) new Object[len];
            for (int i = 0; i < len; i++) {
                b[i] = a[i];
            }
        }

        @Override
        public boolean hasNext() {
            return len != 0;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            int index = StdRandom.uniform(len);
            swap(index, len - 1);
            Item item = b[--len];
            b[len] = null;
            return item;
        }

        private void swap(int i, int j) {
            Item t = b[i];
            b[i] = b[j];
            b[j] = t;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    public static void main(String[] args) {
        RandomizedQueue<Integer> que = new RandomizedQueue<>();
        for (int i = 0; i < 10; i++) {
            que.enqueue(i);
        }
        for (int i : que) {
            StdOut.println(i);
        }
        StdOut.println("------");
        while (!que.isEmpty()) {
            StdOut.println(que.dequeue());
        }
    }
}
