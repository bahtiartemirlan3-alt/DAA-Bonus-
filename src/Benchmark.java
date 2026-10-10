import java.io.PrintWriter;
import java.util.*;

public class Benchmark {
    static final int[] SIZES = {100, 1000, 10000};
    static final int REPEATS = 7;

    interface Structure {
        void insert(int k);
        boolean search(int k);
        boolean delete(int k);
    }

    static Structure newBST() {
        BST t = new BST();
        return new Structure() {
            public void insert(int k) { t.insert(k); }
            public boolean search(int k) { return t.search(k); }
            public boolean delete(int k) { return t.delete(k); }
        };
    }

    static Structure newHash() {
        HashTable t = new HashTable();
        return new Structure() {
            public void insert(int k) { t.insert(k); }
            public boolean search(int k) { return t.search(k); }
            public boolean delete(int k) { return t.delete(k); }
        };
    }

    static int sink;
    static double[] run(Structure s, int[] keys) {
        long t0 = System.nanoTime();
        for (int k : keys) s.insert(k);
        long t1 = System.nanoTime();
        for (int k : keys) if (s.search(k)) sink++;
        long t2 = System.nanoTime();
        for (int k : keys) if (s.delete(k)) sink++;
        long t3 = System.nanoTime();
        return new double[]{(t1 - t0) / 1e6, (t2 - t1) / 1e6, (t3 - t2) / 1e6};
    }

    static double[] bestOf(boolean bst, int[] keys) {
        double[] best = {1e18, 1e18, 1e18};
        for (int r = 0; r < REPEATS; r++) {
            double[] t = run(bst ? newBST() : newHash(), keys);
            for (int i = 0; i < 3; i++) best[i] = Math.min(best[i], t[i]);
        }
        return best;
    }

    public static void main(String[] args) throws Exception {
        BST b = new BST(); HashTable h = new HashTable();
        int[] test = {5, 3, 8, 1, 4, 7, 9};
        for (int k : test) { b.insert(k); h.insert(k); }
        for (int k : test) if (!b.search(k) || !h.search(k)) throw new AssertionError("search");
        if (!b.delete(5) || !h.delete(5) || b.search(5) || h.search(5)) throw new AssertionError("delete");
        System.out.println("tests OK");

        for (int i = 0; i < 20; i++) { bestOf(true, new int[]{1, 2, 3, 4, 5}); bestOf(false, new int[]{1, 2, 3, 4, 5}); }

        System.out.printf("%-10s %-7s %7s %10s %10s %10s  (ms for n ops)%n",
                "struct", "order", "n", "insert", "search", "delete");
        try (PrintWriter csv = new PrintWriter("results.csv")) {
            csv.println("structure,input_order,n,insert_ms,search_ms,delete_ms");
            for (int n : SIZES) {
                Random rnd = new Random(42);
                Set<Integer> uniq = new LinkedHashSet<>();
                while (uniq.size() < n) uniq.add(rnd.nextInt(n * 10));
                int[] random = uniq.stream().mapToInt(Integer::intValue).toArray();
                int[] sorted = new int[n];
                for (int i = 0; i < n; i++) sorted[i] = i;   // BST worst case

                String[] orders = {"random", "sorted"};
                int[][] data = {random, sorted};
                for (int d = 0; d < 2; d++) {
                    for (boolean bst : new boolean[]{true, false}) {
                        double[] t = bestOf(bst, data[d]);
                        String name = bst ? "BST" : "HashTable";
                        System.out.printf(Locale.US, "%-10s %-7s %7d %10.3f %10.3f %10.3f%n",
                                name, orders[d], n, t[0], t[1], t[2]);
                        csv.printf(Locale.US, "%s,%s,%d,%.3f,%.3f,%.3f%n",
                                name, orders[d], n, t[0], t[1], t[2]);
                    }
                }
            }
        }
    }
}
