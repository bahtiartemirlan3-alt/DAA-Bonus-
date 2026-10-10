public class BST {
    private static class Node {
        int key;
        Node left, right;
        Node(int key) { this.key = key; }
    }

    private Node root;
    private int size;

    public int size() { return size; }

    public void insert(int key) {
        if (root == null) { root = new Node(key); size++; return; }
        Node cur = root;
        while (true) {
            if (key == cur.key) return;             
            if (key < cur.key) {
                if (cur.left == null) { cur.left = new Node(key); size++; return; }
                cur = cur.left;
            } else {
                if (cur.right == null) { cur.right = new Node(key); size++; return; }
                cur = cur.right;
            }
        }
    }

    public boolean search(int key) {
        Node cur = root;
        while (cur != null) {
            if (key == cur.key) return true;
            cur = key < cur.key ? cur.left : cur.right;
        }
        return false;
    }

    public boolean delete(int key) {
        Node parent = null, cur = root;
        while (cur != null && cur.key != key) {
            parent = cur;
            cur = key < cur.key ? cur.left : cur.right;
        }
        if (cur == null) return false;

        if (cur.left != null && cur.right != null) {
            Node sp = cur, s = cur.right;
            while (s.left != null) { sp = s; s = s.left; }
            cur.key = s.key;
            parent = sp;
            cur = s;
        }
        Node child = cur.left != null ? cur.left : cur.right;
        if (parent == null) root = child;
        else if (parent.left == cur) parent.left = child;
        else parent.right = child;
        size--;
        return true;
    }

    public int height() {
        int h = 0;
        java.util.ArrayDeque<Node> level = new java.util.ArrayDeque<>();
        if (root != null) level.add(root);
        while (!level.isEmpty()) {
            h++;
            for (int i = level.size(); i > 0; i--) {
                Node n = level.poll();
                if (n.left != null) level.add(n.left);
                if (n.right != null) level.add(n.right);
            }
        }
        return h;
    }
}
