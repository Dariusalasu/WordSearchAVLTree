package AVLTree;

public class Node<K> {
    private K key;
    private int height;
    private Node<K> leftNode, rightNode;
    private Data data;

    public Node(K key, Data data) {
        this.key = key;
        this.height = 0;
        this.leftNode = null;
        this.rightNode = null;
        this.data = data;
    }

    public void setLeftNode(Node<K> node) { this.leftNode = node; }
    public void setRightNode(Node<K> node) { this.rightNode = node; }
    public void setHeight(int height) { this.height = height; }

    public K getKey() { return key; }
    public Node<K> getLeftNode() { return leftNode; }
    public Node<K> getRightNode() { return rightNode; }
    public int getHeight() { return height; }
    public Data getData() { return data; }

    public int compareTo(K other) throws Exception {
        if(key instanceof String) return key.hashCode() - other.hashCode();
        else if(key instanceof Integer) return ((Integer) key ) - ((Integer) other);
        else if(key instanceof Double) return ((Double)((Double)key - (Double)other)).hashCode();

        throw new Exception("Error comparing keys"); // Throw error for invalid result
    }
}
