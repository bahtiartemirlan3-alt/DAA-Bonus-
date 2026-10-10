public class HashTable {
    private static class Node {
        int key;
        Node next;
        Node(int key, Node next) { this.key = key; this.next = next; }
    }

    private Node[] buckets;
    private int size;

    public HashTable() { this(8); }
    public HashTable(int capacity) { buckets = new Node[capacity]; }

    public int size() { return size; }

    private int index(int key, int capacity) {
        int h = Integer.hashCode(key);
        h ^= (h >>> 16);
        return (h & 0x7fffffff) % capacity;
    }

    private void resize() {
        Node[] old = buckets;
        buckets = new Node[old.length * 2];
        for (Node head : old) {
            for (Node n = head; n != null; n = n.next) {
                int i = index(n.key, buckets.length);
                buckets[i] = new Node(n.key, buckets[i]);
            }
        }
    }

    public void insert(int key) {
        int i = index(key, buckets.length);
        for (Node n = buckets[i]; n != null; n = n.next)
            if (n.key == key) return;
        buckets[i] = new Node(key, buckets[i]);
        size++;
        if ((double) size / buckets.length > 0.75) resize();
    }

    public boolean search(int key) {
        for (Node n = buckets[index(key, buckets.length)]; n != null; n = n.next)
            if (n.key == key) return true;
        return false;
    }

    public boolean delete(int key) {
        int i = index(key, buckets.length);
        Node prev = null;
        for (Node n = buckets[i]; n != null; prev = n, n = n.next) {
            if (n.key == key) {
                if (prev == null) buckets[i] = n.next; else prev.next = n.next;
                size--;
                return true;
            }
        }
        return false;
    }
}
