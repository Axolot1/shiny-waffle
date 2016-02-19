package weektwo;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Subset {

    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> que = new RandomizedQueue<>();
        while (!StdIn.isEmpty()) {
            if (que.size() == k) {
                que.dequeue();
            }
            que.enqueue(StdIn.readString());
        }
        for (int i = 0; i < k; i++) {
            StdOut.println(que.dequeue());
        }
    }

}
